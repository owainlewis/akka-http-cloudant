package io.forward.cloudant.http.client.operations

import akka.http.scaladsl.model.{HttpMethods, HttpRequest, Uri}
import io.forward.cloudant.http.client.CloudantConfig

final class DatabaseOperations(config: CloudantConfig) {
  /**
    * Create a database
    *
    * The database name must start with a lowercase letter and contain only the following characters:
    *
    * Lowercase characters (a-z)
    * Digits (0-9)
    * Any of the characters _, $, (, ), +, -, and /
    *
    * @param dbName The name of the database to create
    */
  def create(dbName: String): HttpRequest =
    HttpRequest(HttpMethods.PUT, uri = uriFor(config, dbName))

  def read(dbName: String): HttpRequest =
    HttpRequest(HttpMethods.GET, uri = uriFor(config, dbName))

  def delete(dbName: String): HttpRequest =
    HttpRequest(HttpMethods.DELETE, uri = uriFor(config, dbName))

  def getDatabases: HttpRequest =
    HttpRequest(HttpMethods.GET, uri = uriFor(config, "_all_dbs"))

  def getDocuments(dbName: String): HttpRequest =
    HttpRequest(HttpMethods.GET, uri = uriFor(config, s"$dbName/_all_docs"))

  def getChanges(dbName: String, query: Map[String, String]): HttpRequest =
    HttpRequest(HttpMethods.GET, uri = uriFor(config, s"$dbName/_changes").withQuery(Uri.Query(query)))
}
