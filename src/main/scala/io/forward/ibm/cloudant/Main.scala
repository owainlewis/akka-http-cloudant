package io.forward.ibm.cloudant

import cats.Id
import io.forward.cloudant.http.client._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshalling.PredefinedToEntityMarshallers._

import scala.concurrent.Future

object Main extends App {

  val client = Client("https://owainlewis.cloudant.com", "owainlewis", "venom123")

  val future: Future[CloudantResponse[String]]=
    client.run[String](client.database.getDatabases)

  future.onSuccess { case result =>
    println(result)
  }
}
