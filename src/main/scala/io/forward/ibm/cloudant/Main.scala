package io.forward.ibm.cloudant

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import cats.data.Xor
import io.forward.cloudant.http.client._
import io.forward.cloudant.http.client.internal.CloudantError
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future

object Main extends App {

  val cloudant = Cloudant(
    System.getenv("CLOUDANT_HOST"),
    System.getenv("CLOUDANT_USERNAME"),
    System.getenv("CLOUDANT_PASSWORD"))

  implicit val ex = cloudant.ec

  // Create a database

  val example1 = cloudant.run(cloudant.database.create("foobar"))

  // Create a database and then insert a document

  val example2 = for {
    _ <- cloudant.run(cloudant.database.create("foobar"))
    response <- cloudant.run(cloudant.document.create("foobar", """{"message": "hello"}"""))
  } yield response

  // Cast to type when running requests

  val example3: Future[Xor[CloudantError, List[String]]] =
    cloudant.runAs[List[String]](cloudant.database.getDatabases)

  // Creating documents with implicit marshaller (spray JSON)

  case class User(firstName: String, lastName: String)

  object User {
    implicit val format = jsonFormat2(User.apply)
  }

  val example4 = for {
    _ <- cloudant.run(cloudant.database.create("users"))
    _ <- cloudant.run(cloudant.document.create("users", User("Jack", "Dorsey")))
  } yield ()

  // Fetch a document and unmarshall to Scala

  val getDoc: Future[Xor[CloudantError, User]] =
   cloudant.runAs[User](cloudant.document.read("users", "4ae5791f12636e35b4accb2cc386ce29"))


  val example5 = cloudant.run(cloudant.database.getDocuments("users")).onSuccess { case result =>
    println(result)
  }
}