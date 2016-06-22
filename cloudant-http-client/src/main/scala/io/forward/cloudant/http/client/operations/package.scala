package io.forward.cloudant.http.client

import akka.http.scaladsl.model.Uri

package object operations {
  /**
    * Helper to return the correct URI for a request
    *
    * @param config A [[CloudantConfig]] object
    * @param path The path i.e $DB/_all_docs
    */
  def uriFor(config: CloudantConfig, path: String) = Uri(s"${config.host}/$path")
}
