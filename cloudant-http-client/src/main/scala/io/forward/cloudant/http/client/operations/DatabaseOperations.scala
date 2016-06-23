package io.forward.cloudant.http.client.operations

import akka.http.scaladsl.model.{HttpMethods, HttpRequest, Uri}
import cats.data.Reader
import io.forward.cloudant.http.client.CloudantConfig

class DatabaseOperations {
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
  def create(dbName: String): Reader[CloudantConfig, HttpRequest] =
    Reader((c: CloudantConfig) =>
      HttpRequest(HttpMethods.PUT, uri = uriFor(c, dbName)))

  /**
    * Get information about a database
    *
    * @param dbName The database name
    */
  def read(dbName: String): Reader[CloudantConfig, HttpRequest] =
    Reader((c: CloudantConfig) =>
    HttpRequest(HttpMethods.GET, uri = uriFor(c, dbName)))

  /**
    * Delete a database
    *
    * @param dbName THe database name
    */
  def delete(dbName: String): Reader[CloudantConfig, HttpRequest] =
    Reader((c: CloudantConfig) =>
      HttpRequest(HttpMethods.DELETE, uri = uriFor(c, dbName)))

  /**
    * Get all databases
    *
    */
  def getDatabases: Reader[CloudantConfig, HttpRequest] =
    Reader((c: CloudantConfig) =>
      HttpRequest(HttpMethods.GET, uri = uriFor(c, "_all_dbs")))

  /**
    * Get all documents in a database
    *
    * @param dbName The database name
    * @param query Additional query params
    */
  def getDocuments(dbName: String, query: Map[String, String] = Map.empty): Reader[CloudantConfig, HttpRequest] =
    Reader((c: CloudantConfig) =>
      HttpRequest(HttpMethods.GET, uri = uriFor(c, s"$dbName/_all_docs")
        .withQuery(Uri.Query(query))))

  /**
    * Get all changes
    *
    * @param dbName The database name
    * @param query Additional query params
    */
  def getChanges(dbName: String, query: Map[String, String] = Map.empty): Reader[CloudantConfig, HttpRequest] =
    Reader((c: CloudantConfig) =>
      HttpRequest(HttpMethods.GET, uri = uriFor(c, s"$dbName/_changes")
        .withQuery(Uri.Query(query))))
}
