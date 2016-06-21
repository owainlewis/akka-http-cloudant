package io.forward.ibm.cloudant.operations

import io.forward.ibm.cloudant.CloudantConfig
import akka.http.scaladsl.model.{Uri, HttpMethods, HttpRequest}

class AccountOperations(config: CloudantConfig) {
  /**
    * To see if your Cloudant account is accessible, make a GET against https://$USERNAME.cloudant.com.
    *
    * If you misspelled your account name, you might get a 503 ‘service unavailable’ error.
    *
    */
  def ping = HttpRequest(HttpMethods.GET, uriFor(config, ""))
}
