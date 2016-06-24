package io.forward.cloudant.http.client.internal

import scala.util.control.NoStackTrace

sealed trait CloudantError

sealed trait CloudantException extends Exception with NoStackTrace

object CloudantError {
  case class Generic(status: Int, message: String) extends CloudantError
}

object CloudantException {
  case class Generic(status: Int) extends CloudantException
}

