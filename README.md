# Akka HTTP Cloudant

A functional async client for IBM Cloudant based on Akka HTTP.

## Example

```scala

val client = Client("https://user.cloudant.com", "username", "password")

client.database.create("users")

```