# Akka HTTP Cloudant

A functional async client for IBM Cloudant based on Akka HTTP.

![](https://cloudant.com/wp-content/themes/cloudant/images/ibm_cloudant.png)

The library is split into distinct modules

+ A simple raw HTTP wrapper for Cloudant that abstracts all core operations
+ A higher level wrapper for Spray JSON

If you don't want to use Spray JSON the lower level client can be used without any issues.

## Example

### Creating a client instance

```scala

val host = System.getenv("CLOUDANT_HOST")
val user = System.getenv("CLOUDANT_USERNAME")
val pass = System.getenv("CLOUDANT_PASSWORD")

val cloudant = Cloudant(host, user, pass)
```

### Creating documents

Create a database and then insert a document

```scala
val example2 = for {
  _ <- cloudant.run(cloudant.database.create("foobar"))
  response <- cloudant.run(cloudant.document.create("foobar", """{"message": "hello"}"""))
} yield response
```

Creating documents with implicit marshaller (spray JSON)


```scala
case class User(firstName: String, lastName: String)

object User {
  implicit val format = jsonFormat2(User.apply)
}

val example4 = for {
  _ <- cloudant.run(cloudant.database.create("users"))
  _ <- cloudant.run(cloudant.document.create("users", User("Jack", "Dorsey")))
} yield ()
```

### Casting results

As long as you have an implicit ToEntityMarshaller for a type available you can cast the results

```scala
val example3: Future[Xor[CloudantError, List[String]]] =
  cloudant.runAs[List[String]](cloudant.database.getDatabases)
```

## Full example

```scala
package io.forward.ibm.cloudant

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.stream.ActorMaterializer
import cats.data.Xor
import io.forward.cloudant.http.client._
import io.forward.cloudant.http.client.internal.CloudantError
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future

object Main extends App {

  // Required implicits
  implicit private val system = ActorSystem()
  implicit private val materializer = ActorMaterializer()

  import system.dispatcher

  val cloudant = Cloudant(
    System.getenv("CLOUDANT_HOST"),
    System.getenv("CLOUDANT_USERNAME"),
    System.getenv("CLOUDANT_PASSWORD"))

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

```

