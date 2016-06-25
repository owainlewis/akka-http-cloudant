package io.forward.cloudant.http.client.operations

import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import cats.Id
import cats.data.{Kleisli, Reader}
import cats.kernel.std.StringMonoid
import io.forward.cloudant.http.client.CloudantConfig

final class AccountOperations {

  val ping: Kleisli[Id, CloudantConfig, HttpRequest] =
    Reader((c: CloudantConfig) => HttpRequest(HttpMethods.GET, uriFor(c, implicitly[StringMonoid].empty)))
}
