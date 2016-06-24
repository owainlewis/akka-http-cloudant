package io.forward.ibm.cloudant

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.HttpResponse
import cats.data.{Xor, Reader}
import io.forward.cloudant.http.client._
import io.forward.cloudant.http.client.internal.CloudantError
import spray.json.DefaultJsonProtocol._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Main extends App {

  val cloudant = Cloudant(
    System.getenv("CLOUDANT_HOST"),
    System.getenv("CLOUDANT_USERNAME"),
    System.getenv("CLOUDANT_PASSWORD"))

  val future1: Future[Xor[CloudantError, List[String]]] =
    cloudant.runAs[List[String]](cloudant.database.getDatabases)

  val future = for {
    _ <- cloudant.run(cloudant.database.create("foobar"))
   response <- cloudant.run(cloudant.document.create("foobar", """{"message": "hello"}"""))
  } yield response

  future.onSuccess { case result =>
    println(result)
  }

  future.onFailure { case fe =>
    println(fe.getMessage)
  }
}