package io.forward.cloudant.http.client.internal

import scala.util.control.NoStackTrace

sealed trait CloudantError

sealed trait CloudantException extends Exception with NoStackTrace

case class CloudantErrorMessage(error: String, reason: String)

object CloudantError {
  case class Generic(status: Int, message: String) extends CloudantError
}

object CloudantException {
  case class Generic(status: Int) extends CloudantException
}
