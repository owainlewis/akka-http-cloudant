package io.forward.cloudant.http.client.operations

import akka.http.scaladsl.model._
import io.forward.cloudant.http.client.CloudantConfig

final class DocumentOperations(config: CloudantConfig) {

  def create(dbName: String, document: String) =
    HttpRequest(HttpMethods.POST, uriFor(config, dbName), entity = HttpEntity(ContentTypes.`application/json`, document))

  def read(dbName: String, id: String, query: Map[String, String] = Map.empty) =
    HttpRequest(HttpMethods.GET, uriFor(config, s"$dbName/$id").withQuery(Uri.Query(query)))

  def update(dbName: String, document: String) =
    HttpRequest(HttpMethods.PUT, uriFor(config, dbName), entity = HttpEntity(ContentTypes.`application/json`, document))

  def delete(dbName: String, id: String, rev: String) =
    HttpRequest(HttpMethods.DELETE, uriFor(config, s"$dbName/$id").withQuery(Uri.Query(Map("rev" -> rev))))
}
