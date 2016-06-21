package io.forward.cloudant.http.client

case class CloudantResponse[T](code: Int, body: T)
