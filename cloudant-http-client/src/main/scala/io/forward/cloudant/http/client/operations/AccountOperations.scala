package io.forward.cloudant.http.client.operations

import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import cats.data.Reader
import io.forward.cloudant.http.client.CloudantConfig

final class AccountOperations {
  /**
    * To see if your Cloudant account is accessible, make a GET against https://$USERNAME.cloudant.com.
    *
    * If you misspelled your account name, you might get a 503 ‘service unavailable’ error.
    */
  val ping: Reader[CloudantConfig, HttpRequest] =
    Reader((c: CloudantConfig) =>
      HttpRequest(HttpMethods.GET, uriFor(c, "")))
}
