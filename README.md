# Akka HTTP Cloudant

A pure Akka HTTP async client for IBM Cloudant.

## Example

```scala

val client = Client("https://user.cloudant.com", "username", "password")

client.database.create("users")

```