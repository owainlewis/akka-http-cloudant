package io.forward.cloudant.http.client.operations

import akka.http.scaladsl.model._
import cats.data.Reader
import io.forward.cloudant.http.client.CloudantConfig

final class DocumentOperations {
  /**
    * Create a document
    *
    * @param dbName The database name
    * @param document The document to create (raw JSON)
    */
  def create(dbName: String, document: String): Reader[CloudantConfig, HttpRequest] =
    Reader((c: CloudantConfig) =>
      HttpRequest(HttpMethods.POST, uriFor(c, dbName),
       entity = HttpEntity(ContentTypes.`application/json`, document)))

  /**
    * Read a document
    *
    * @param dbName The document name
    * @param id The document ID
    * @param query Additional query params
    */
  def read(dbName: String, id: String, query: Map[String, String] = Map.empty): Reader[CloudantConfig, HttpRequest] =
    Reader((c: CloudantConfig) =>
    HttpRequest(HttpMethods.GET, uriFor(c, s"$dbName/$id")
      .withQuery(Uri.Query(query))))

  /**
    * Update a document
    *
    * @param dbName The database name
    * @param document The document to update (raw JSON)
    */
  def update(dbName: String, document: String): Reader[CloudantConfig, HttpRequest] =
    Reader((c: CloudantConfig) =>
      HttpRequest(HttpMethods.PUT, uriFor(c, dbName),
        entity = HttpEntity(ContentTypes.`application/json`, document)))

  /**
    * Delete a document
    *
    * @param dbName The database name
    * @param id The document ID
    * @param rev The document revision
    */
  def delete(dbName: String, id: String, rev: String): Reader[CloudantConfig, HttpRequest] =
    Reader((c: CloudantConfig) =>
      HttpRequest(HttpMethods.DELETE, uriFor(c, s"$dbName/$id")
        .withQuery(Uri.Query(Map("rev" -> rev)))))

  /**
    * Bulk update documents (see docs for more information about payload structure)
    *
    * @param dbName The database name
    * @param documents Raw JSON documents to create
    */
  def bulkCreate(dbName: String, documents: String): Reader[CloudantConfig, HttpRequest] =
    Reader((c: CloudantConfig) =>
      HttpRequest(HttpMethods.POST, uriFor(c, s"$dbName/_bulk_docs"),
        entity = HttpEntity(ContentTypes.`application/json`, documents)))
}
