package io.forward.cloudant.http.client.operations

import akka.http.scaladsl.marshalling.{Marshal, ToEntityMarshaller}
import akka.http.scaladsl.model._
import cats.Id
import cats.data.{Kleisli, Reader}
import io.forward.cloudant.http.client.CloudantConfig

import scala.concurrent.Future

final class DocumentOperations {
  /**
    * Create a document
    *
    * @param dbName The database name
    * @param document The document to create (raw JSON)
    */
  def create(dbName: String, document: String): Kleisli[Id, CloudantConfig, HttpRequest] =
    Reader((c: CloudantConfig) =>
      HttpRequest(HttpMethods.POST, uriFor(c, dbName),
       entity = HttpEntity(ContentTypes.`application/json`, document)))

  /**
    * Create a document using a generic to entity marshaller
    *
    * @param dbName The database name
    * @param document A document type
    * @param em An implicit entity marshaller
    * @tparam A The document type
    */
  def create[A](dbName: String, document: A)(implicit em: ToEntityMarshaller[A]): Kleisli[Id, CloudantConfig, Future[HttpRequest]] =
    Reader { (c: CloudantConfig) =>
      Marshal(document).to[MessageEntity] map { e =>
        HttpRequest(HttpMethods.POST, uriFor(c, dbName), entity = e)
      }
    }

  /**
    * Read a document
    *
    * @param dbName The document name
    * @param id The document ID
    * @param query Additional query params
    */
  def read(dbName: String, id: String, query: Map[String, String] = Map.empty): Kleisli[Id, CloudantConfig, HttpRequest] =
    Reader((c: CloudantConfig) =>
    HttpRequest(HttpMethods.GET, uriFor(c, s"$dbName/$id")
      .withQuery(Uri.Query(query))))

  /**
    * Update a document
    *
    * @param dbName The database name
    * @param document The document to update (raw JSON)
    */
  def update(dbName: String, document: String): Kleisli[Id, CloudantConfig, HttpRequest] =
    Reader((c: CloudantConfig) =>
      HttpRequest(HttpMethods.PUT, uriFor(c, dbName),
        entity = HttpEntity(ContentTypes.`application/json`, document)))

  /**
    * Update a document using an implicit ToEntityMarshaller
    *
    * @param dbName The database name
    * @param document A document type that can be marshalled
    * @param em An implicit to entity marshaller
    * @tparam A Document type
    */
  def update[A](dbName: String, document: A)(implicit em: ToEntityMarshaller[A]): Kleisli[Id, CloudantConfig, Future[HttpRequest]] =
    Reader { (c: CloudantConfig) =>
      Marshal(document).to[MessageEntity] map { e =>
        HttpRequest(HttpMethods.PUT, uriFor(c, dbName), entity = e)
      }
    }

  /**
    * Delete a document
    *
    * @param dbName The database name
    * @param id The document ID
    * @param rev The document revision
    */
  def delete(dbName: String, id: String, rev: String): Kleisli[Id, CloudantConfig, HttpRequest] =
    Reader((c: CloudantConfig) =>
      HttpRequest(HttpMethods.DELETE, uriFor(c, s"$dbName/$id")
        .withQuery(Uri.Query(Map("rev" -> rev)))))

  /**
    * Bulk update documents (see docs for more information about payload structure)
    *
    * @param dbName The database name
    * @param documents Raw JSON documents to create
    */
  def bulkCreate(dbName: String, documents: String): Kleisli[Id, CloudantConfig, HttpRequest] =
    Reader((c: CloudantConfig) =>
      HttpRequest(HttpMethods.POST, uriFor(c, s"$dbName/_bulk_docs"),
        entity = HttpEntity(ContentTypes.`application/json`, documents)))
}
