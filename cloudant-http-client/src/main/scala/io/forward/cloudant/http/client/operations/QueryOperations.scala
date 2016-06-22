package io.forward.cloudant.http.client.operations

import akka.http.scaladsl.model._
import io.forward.cloudant.http.client.CloudantConfig

final class QueryOperations(config: CloudantConfig) {

  /**
    * Create a new index
    *
    * @param dbName
    * @param index A raw JSON string
    */
  def createIndex(dbName: String, index: String): HttpRequest =
    HttpRequest(HttpMethods.POST, uriFor(config, s"$dbName/_index"), entity = HttpEntity(ContentTypes.`application/json`, index))

  /**
    * List all Cloudant Query indexes
    *
    * @param dbName The database name
    */
  def listIndexes(dbName: String): HttpRequest =
    HttpRequest(HttpMethods.GET, uriFor(config, s"$dbName/_index"))

  /**
    * Delete an index
    *
    * @param dbName The database name
    * @param designDocId The ID of the design document
    * @param indexType The type of the index (for example "json")
    * @param indexName The name of the index
    */
  def deleteIndex(dbName: String, designDocId: String, indexType: String, indexName: String): HttpRequest =
    HttpRequest(HttpMethods.DELETE, uriFor(config, s"$dbName/_index/$designDocId/$indexType/$indexName"))

  /**
    * Search for documents using an index
    *
    * {
    *     "selector": {
    *         "year": {"$gt": 2010}
    *     },
    *     "fields": ["_id", "_rev", "year", "title"],
    *     "sort": [{"year": "asc"}],
    *     "limit": 10,
    *     "skip": 0
    * }
    *
    * @param dbName The database name
    * @param query A raw JSON index query
    */
  def searchIndex(dbName: String, query: String): HttpRequest =
    HttpRequest(HttpMethods.POST, uriFor(config, s"$dbName/_find"), entity = HttpEntity(ContentTypes.`application/json`, query))
}
