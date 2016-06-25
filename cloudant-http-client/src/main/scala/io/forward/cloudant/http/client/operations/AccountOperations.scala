package io.forward.cloudant.http.client.operations

import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import cats.data.Reader
import io.forward.cloudant.http.client.CloudantConfig

import scala.concurrent.Future

final class AccountOperations {

  val ping: CloudantKleisli =
    Reader((c: CloudantConfig) =>
      Future.successful(HttpRequest(HttpMethods.GET, uriFor(c, ""))))
}
