package io.forward.ibm.cloudant

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.{Authorization, BasicHttpCredentials}
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer
import cats.data.Xor
import io.forward.ibm.cloudant.operations._

import scala.concurrent.Future

class Client(config: CloudantConfig) {

  implicit private val system = ActorSystem()
  implicit private val materializer = ActorMaterializer()

  val database = new DatabaseOperations(config)

  def run(req: HttpRequest): Future[HttpResponse] = {
    val authenticatedRequest =
      req.withHeaders(Authorization(BasicHttpCredentials(config.username, config.password)))
    Http().singleRequest(authenticatedRequest)
  }
}

object Client {
  def apply(host: String, username: String, password: String) =
    new Client(CloudantConfig(host, username, password))
}
