package io.forward.cloudant.http.client

import akka.http.scaladsl.model.{ContentTypes, HttpResponse, ResponseEntity}
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.Materializer
import cats.data.Xor
import io.forward.cloudant.http.client.internal.CloudantError.Generic
import io.forward.cloudant.http.client.internal._

import scala.concurrent.{ExecutionContext, Future}

object ResponseMapper {
  /**
    * Transforms a HttpResponse into some concrete type T
    *
    * This can only happen if the response is a success.
    * If the response fails (i.e 500 or 503) thefuture fails.
    * In the case of a generic user error (i.e database not found) then a left Xor is returned.
    * If the response is a success then a right Xor is returned containing the unmarshalled type T
    *
    * @param response A HttpResponse
    * @param ec An implicit execution context
    * @param mat An implicit stream materializer
    * @param um An implicit unmarshaller for the entity
    * @tparam T The type to create if response is successful
    */
  def transform[T](response: HttpResponse)
                  (implicit ec: ExecutionContext, mat: Materializer, um: Unmarshaller[ResponseEntity, T]): Future[Xor[Generic, T]] = {
    val status = response.status.intValue
    if (failureCodes.contains(status))
      Future.failed(CloudantException.Generic(status))
    else if (successCodes.contains(status))
      entityAs[T](response) map Xor.Right.apply
    else
      entityAs[String](response) map { resp =>
        Xor.Left(CloudantError.Generic(status, resp))
      }
  }

  def entityAs[T](response: HttpResponse)
                         (implicit ec: ExecutionContext, mat: Materializer, um: Unmarshaller[ResponseEntity, T]): Future[T] =
    Unmarshal(response.entity.withContentType(ContentTypes.`application/json`)).to[T]

  private val successCodes = Set(200, 201, 202)

  private val failureCodes = Set(500, 503)
}
