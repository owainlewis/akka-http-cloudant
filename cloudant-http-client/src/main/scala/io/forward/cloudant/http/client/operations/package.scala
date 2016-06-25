package io.forward.cloudant.http.client

import akka.http.scaladsl.model.{HttpRequest, Uri}
import cats.Id
import cats.data.Kleisli

import scala.concurrent.Future

package object operations {

  def uriFor(config: CloudantConfig, path: String) =
    Uri(s"${config.host}/$path")

  /** All cloudant operations take this form. It takes config and returns a future HTTP request to execute */
  type CloudantKleisli = Kleisli[Id, CloudantConfig, Future[HttpRequest]]
}
