package io.forward.cloudant.http.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.{Authorization, BasicHttpCredentials}
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import io.forward.cloudant.http.client.operations.{DatabaseOperations, DocumentOperations}

import scala.concurrent.Future

final class Client(config: CloudantConfig) {

  implicit private val system = ActorSystem()
  implicit private val materializer = ActorMaterializer()

  val database = new DatabaseOperations(config)
  val document = new DocumentOperations(config)

  def run(req: HttpRequest): Future[CloudantResponse] =
    runRequest(req) flatMap { response =>
      Unmarshal(response.entity).to[String] map { body =>
        CloudantResponse(response.status.intValue, body)
      }
    }

  private def runRequest(req: HttpRequest): Future[HttpResponse] = {
    val authHeader = Authorization(BasicHttpCredentials(config.username, config.password))
    val authenticatedRequest =
      req.withHeaders(authHeader)
    Http().singleRequest(authenticatedRequest)
  }
}

object Client {
  def apply(host: String, username: String, password: String) =
    new Client(CloudantConfig(host, username, password))
}
