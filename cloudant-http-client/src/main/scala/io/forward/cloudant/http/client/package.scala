package io.forward.cloudant.http

import akka.http.scaladsl.model.HttpRequest
import cats.Id
import cats.data.Kleisli

package object client {

  type CloudantOperation = Kleisli[Id, CloudantConfig, HttpRequest]
}
