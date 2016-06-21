package io.forward.ibm.cloudant

import akka.http.scaladsl.model.Uri

package object operations {

  def uriFor(config: CloudantConfig, path: String) = Uri(s"${config.host}/$path")
}
