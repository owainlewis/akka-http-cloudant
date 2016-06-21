package io.forward.ibm.cloudant

import io.forward.cloudant.http.client._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshalling.PredefinedToEntityMarshallers._

object Main extends App {

  val client = Client("https://owainlewis.cloudant.com", "", "")

  val future = client.run[String](client.database.getDatabases)

  future.onSuccess { case result =>
    println(result)
  }
}
