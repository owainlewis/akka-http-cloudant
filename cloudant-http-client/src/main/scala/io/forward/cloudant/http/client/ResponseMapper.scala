package io.forward.cloudant.http.client

import akka.http.scaladsl.model.{ContentTypes, HttpResponse, ResponseEntity}
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.Materializer
import cats.data.Xor
import io.forward.cloudant.http.client.internal._

import scala.concurrent.{ExecutionContext, Future}

object ResponseMapper {

  private val successCodes = Set(200, 201, 202)

  private val failureCodes = Set(500, 503)

  def transform[T](response: HttpResponse)
                  (implicit ec: ExecutionContext, mat: Materializer, um: Unmarshaller[ResponseEntity, T]) = {
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

  private def entityAs[T](response: HttpResponse)
                         (implicit ec: ExecutionContext, mat: Materializer, um: Unmarshaller[ResponseEntity, T]): Future[T] =
    Unmarshal(response.entity.withContentType(ContentTypes.`application/json`)).to[T]
}
