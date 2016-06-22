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

  /**
    * Get information about a database
    *
    * @param dbName The database name
    */
  def read(dbName: String): HttpRequest =
    HttpRequest(HttpMethods.GET, uri = uriFor(config, dbName))

  /**
    * Delete a database
    *
    * @param dbName THe database name
    */
  def delete(dbName: String): HttpRequest =
    HttpRequest(HttpMethods.DELETE, uri = uriFor(config, dbName))

  /**
    * Get all databases
    *
    */
  def getDatabases: HttpRequest =
    HttpRequest(HttpMethods.GET, uri = uriFor(config, "_all_dbs"))

  /**
    * Get all documents in a database
    *
    * @param dbName The database name
    * @param query Additional query params
    */
  def getDocuments(dbName: String, query: Map[String, String] = Map.empty): HttpRequest =
    HttpRequest(HttpMethods.GET, uri = uriFor(config, s"$dbName/_all_docs")
      .withQuery(Uri.Query(query)))

  /**
    * Get all changes
    *
    * @param dbName The database name
    * @param query Additional query params
    */
  def getChanges(dbName: String, query: Map[String, String] = Map.empty): HttpRequest =
    HttpRequest(HttpMethods.GET, uri = uriFor(config, s"$dbName/_changes")
      .withQuery(Uri.Query(query)))
}
