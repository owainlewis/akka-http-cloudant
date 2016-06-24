package io.forward.cloudant.http.client.internal

import scala.util.control.NoStackTrace

sealed trait CloudantError extends Exception with NoStackTrace

object CloudantError {
  case class Generic(status: Int, message: String) extends CloudantError
}

