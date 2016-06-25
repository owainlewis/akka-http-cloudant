package io.forward.ibm.cloudant

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import cats.data.Xor
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

  val future2 = for {
    _ <- cloudant.run(cloudant.database.create("foobar"))
   response <- cloudant.run(cloudant.document.create("foobar", """{"message": "hello"}"""))
  } yield response

  val future = cloudant.run(cloudant.document.read("foobar", "123"))

  case class User(firstName: String, lastName: String)

  object User {
    implicit val format = jsonFormat2(User.apply)
  }

  val createDoc = cloudant.run(cloudant.document.create("users", User("Jack", "Dorsey")))

  createDoc.onSuccess { case r =>
      println(r)
  }

  val getDoc = cloudant.runAsUnsafe[User](cloudant.document.read("users", "4ae5791f12636e35b4accb2cc386ce29"))

  getDoc.onSuccess { case r =>
      println(r)
  }

  
}