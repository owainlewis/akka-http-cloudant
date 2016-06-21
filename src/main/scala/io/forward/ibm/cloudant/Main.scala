package io.forward.ibm.cloudant

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.Await

object Main extends App {

  val client = Client("https://owainlewis.cloudant.com", "", "")

  import scala.concurrent.duration._

  println(Await.result(client.run(client.database.create("foo")), 5.seconds))

  println(Await.result(client.run(client.database.read("foo")), 5.seconds))

}
