package io.forward.cloudant.http.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.{RawHeader, Authorization, BasicHttpCredentials}
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.{Unmarshaller, Unmarshal}
import akka.stream.ActorMaterializer
import cats.Id
import cats.data.{Kleisli, Reader}
import io.forward.cloudant.http.client.operations._

import scala.concurrent.{ExecutionContext, Future}

final class Client(config: CloudantConfig) {

  implicit private val system = ActorSystem()
  implicit private val materializer = ActorMaterializer()

  val account = new AccountOperations
  val attachment = new AttachmentOperations(config)
  val database = new DatabaseOperations(config)
  val document = new DocumentOperations(config)
  val query = new QueryOperations(config)
  val search = new SearchOperations(config)
  val view = new ViewOperations(config)

  def exec(req: Reader[CloudantConfig, HttpRequest]) = runR(req).run(config)

  def runR[T](op: Reader[CloudantConfig, HttpRequest])(implicit
                               ec: ExecutionContext,
                               um: Unmarshaller[ResponseEntity, T])
  : Kleisli[Id, CloudantConfig, Future[CloudantResponse[T]]] =
    op.map(runRequest).map(_.flatMap { response =>
      Unmarshal(response.entity).to[T] map { body =>
        CloudantResponse(response.status.intValue, body)
      }
    })

  def run[T](req: HttpRequest)(implicit ec: ExecutionContext, um: Unmarshaller[ResponseEntity, T]): Future[CloudantResponse[T]] =
    runRequest(req) flatMap { response =>
      Unmarshal(response.entity.withContentType(ContentTypes.`application/json`)).to[T] map { body =>
        CloudantResponse(response.status.intValue, body)
      }
    }

  private def runRequest(req: HttpRequest): Future[HttpResponse] = {
    val authHeader = Authorization(BasicHttpCredentials(config.username, config.password))
    val authenticatedRequest =
      req.withHeaders(authHeader, RawHeader("Content-Type", "application/json"))

    Http().singleRequest(authenticatedRequest)
  }
}

object Client {
  def apply(host: String, username: String, password: String) =
    new Client(CloudantConfig(host, username, password))
}
