# Cloudant HTTP Client

A minimal HTTP client for IBM Cloudant based on Akka HTTP.

## Example

### Creating a client instance

```scala

val host = System.getenv("CLOUDANT_HOST")
val user = System.getenv("CLOUDANT_USERNAME")
val pass = System.getenv("CLOUDANT_PASSWORD")

val cloudant = Cloudant(host, user, pass)
```

### Creating documents

```scala

val documentCreateFuture = for {
  _ <- cloudant.run(cloudant.database.create("foobar"))
  response <- cloudant.run(cloudant.document.create("foobar", """{"message": "hello"}"""))
} yield response

// CloudantOperationResponse(201,{"ok":true,"id":"c017a78480d0de37bf48ae0c1ea78497","rev":"1-acc307ed2aedd491f0267c9c9b623388"})

```

### Deserializing responses

```scala
val future1: Future[Xor[CloudantError, List[String]]] =
  cloudant.runAs[List[String]](cloudant.database.getDatabases, List(200))
```

Sample responses 

```scala
CloudantOperationResponse(201, """{"ok":true,"id":"c017a78480d0de37bf48ae0c1ea78497","rev":"1-acc307ed2aedd491f0267c9c9b623388"}""")
```
