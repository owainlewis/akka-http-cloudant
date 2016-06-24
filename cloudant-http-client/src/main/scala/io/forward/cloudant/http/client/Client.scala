package io.forward.cloudant.http.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{Authorization, BasicHttpCredentials}
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.ActorMaterializer
import cats.Id
import cats.data.{Kleisli, Reader, Xor}
import io.forward.cloudant.http.client.internal.CloudantError
import io.forward.cloudant.http.client.operations._

import scala.concurrent.{ExecutionContext, Future}

final class Client(config: CloudantConfig) {

  implicit private val system = ActorSystem()
  implicit private val materializer = ActorMaterializer()

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
  def runResponse(op: CloudantOperation)
            (implicit ec: ExecutionContext): Future[HttpResponse] =
    op.map(runRequest).run(config)

  /**
    * Run an operation and return the status code and response body wrapped in a [[CloudantOperationResponse]]
    *
    * @param op A Cloudant operation
    * @param ec An implicit execution context
    */
  def run(op: CloudantOperation)(implicit ec: ExecutionContext): Future[CloudantOperationResponse] =
    runResponse(op) flatMap { response =>
      Unmarshal(response.entity.withContentType(ContentTypes.`application/json`)).to[String] map {
        CloudantOperationResponse(response.status.intValue(), _)
      }
    }

  /**
    * Run a request. If the status codes match the ones provided (un)marshall else return a failure
    *
    * @param op An operation to run
    * @param statusCodes A list of status codes representing a success case
    * @param ec An implicit execution context
    * @param um An implicit entity (un)marshaller
    */
  def runAs[T](op: CloudantOperation, statusCodes: List[Int])
              (implicit ec: ExecutionContext, um: Unmarshaller[ResponseEntity, T]): Future[Xor[CloudantError, T]] =
    op.map(runRequest).map { futureResponse =>
      futureResponse flatMap { response =>
        if (statusCodes.contains(response.status.intValue))
          Unmarshal(response.entity.withContentType(ContentTypes.`application/json`))
            .to[T] map Xor.Right.apply
        else
          Future.successful {
            Xor.Left(CloudantError.Generic(response.status.intValue, ""))
          }
      }
    }.run(config)

  def runAsEither[T](op: CloudantOperation, statusCodes: List[Int]): Future[Either[CloudantError, T]] =
    runAs[T](op, statusCodes) map (_.toEither)

  private def runRequest(req: HttpRequest): Future[HttpResponse] = {
    val authHeader = Authorization(BasicHttpCredentials(config.username, config.password))
    val authenticatedRequest = req.withHeaders(authHeader)
    Http().singleRequest(authenticatedRequest)
  }
}

object Client {
  def apply(host: String, username: String, password: String) =
    new Client(CloudantConfig(host, username, password))
}
