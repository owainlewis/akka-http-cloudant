package io.forward.cloudant.http.client.operations

import akka.http.scaladsl.model._
import cats.data.Reader
import io.forward.cloudant.http.client.CloudantConfig

import scala.concurrent.Future

final class AttachmentOperations {
  /**
    * Read an attachment
    *
    * @param dbName The database name
    * @param documentId The document ID
    * @param attachment The attachment
    */
  def readAttachment(dbName: String, documentId: String, attachment: String): CloudantKleisli =
    Reader((c: CloudantConfig) =>
      Future.successful(HttpRequest(HttpMethods.GET, uriFor(c, s"$dbName/$documentId/$attachment"))))

  /**
    * Delete an attachment
    *
    * @param dbName The database name
    * @param documentId The document ID
    * @param attachment The attachment
    * @param rev A document revision
    */
  def deleteAttachment(dbName: String,
                       documentId: String,
                       attachment: String,
                       rev: String): CloudantKleisli =
    Reader((c: CloudantConfig) =>
      Future.successful {
        HttpRequest(HttpMethods.DELETE, uriFor(c, s"$dbName/$documentId/$attachment")
          .withQuery(Uri.Query(Map("rev" -> rev))))
      })
}

