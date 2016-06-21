package io.forward.cloudant.http.client.operations

import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import io.forward.cloudant.http.client.CloudantConfig

final class AccountOperations(config: CloudantConfig) {
  /**
    * To see if your Cloudant account is accessible, make a GET against https://$USERNAME.cloudant.com.
    *
    * If you misspelled your account name, you might get a 503 ‘service unavailable’ error.
    *
    */
  def ping = HttpRequest(HttpMethods.GET, uriFor(config, ""))
}
