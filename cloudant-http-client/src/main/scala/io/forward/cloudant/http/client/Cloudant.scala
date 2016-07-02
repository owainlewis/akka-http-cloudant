package io.forward.cloudant.http.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{Authorization, BasicHttpCredentials}
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.ActorMaterializer
import cats.data.Xor
import io.forward.cloudant.http.client.internal.CloudantError
import io.forward.cloudant.http.client.operations._

import scala.concurrent.{ExecutionContext, Future}

final class Cloudant(config: CloudantConfig)
                    (implicit system: ActorSystem, materializer: ActorMaterializer, ec: ExecutionContext) {

  val account = new AccountOperations
  val attachment = new AttachmentOperations
  val database = new DatabaseOperations
  val document = new DocumentOperations
  val query = new QueryOperations
  val search = new SearchOperations
  val view = new ViewOperations

  /**
    * Run a HTTP request and return the raw Akka HTTP response.
    *
    * @param op A Cloudant operation
    * @param ec An implicit execution context
    */
  def runResponse(op: CloudantKleisli): Future[HttpResponse] =
    op.run(config) flatMap runRequest

  /**
    * Run an operation and return the status code and response body
    *
    * @param op A Cloudant operation
    * @param ec An implicit execution context
    */
  def run(op: CloudantKleisli): Future[CloudantOperationResponse] =
    runResponse(op) flatMap { response =>
      Unmarshal(response.entity.withContentType(ContentTypes.`application/json`)).to[String] map {
        CloudantOperationResponse(response.status.intValue(), _)
      }
    }

  /**
    * Run a request.
    *
    * The response mapper handles transforming responses into an Xor representing success or failure.
    *
    * If a 500 status is returned the future will fail.
    *
    * @param op          An operation to run
    * @param ec          An implicit execution context
    * @param um          An implicit entity (un)marshaller
    */
  def runAs[T](op: CloudantKleisli)
              (implicit um: Unmarshaller[ResponseEntity, T]): Future[Xor[CloudantError, T]] = for {
    req <- op.run(config)
    response <- runRequest(req)
    result <- ResponseMapper.transform[T](response)
  } yield result

  /**
    * Same as above but doesn't check the response code for errors.
    */
  def runAsUnsafe[T](op: CloudantKleisli)
              (implicit um: Unmarshaller[ResponseEntity, T]): Future[T] = for {
    req <- op.run(config)
    response <- runRequest(req)
    result <- ResponseMapper.entityAs[T](response)
  } yield result

  def runAsEither[T](op: CloudantKleisli)
                    (implicit um: Unmarshaller[ResponseEntity, T]): Future[Either[CloudantError, T]] =
    runAs[T](op) map (_.toEither)

  private def runRequest(req: HttpRequest): Future[HttpResponse] = {
    val authHeader = Authorization(BasicHttpCredentials(config.username, config.password))
    val authenticatedRequest = req.withHeaders(authHeader)
    Http().singleRequest(authenticatedRequest)
  }
}

object Cloudant {
  def apply(host: String, username: String, password: String)
           (implicit system: ActorSystem, materializer: ActorMaterializer, ec: ExecutionContext) = {
    new Cloudant(CloudantConfig(host, username, password))
  }
}
