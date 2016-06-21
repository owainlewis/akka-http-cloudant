package io.forward.cloudant.http.client

trait CloudantConfig {
  val host: String
  val username: String
  val password: String
}

object CloudantConfig {

  def apply(cloudantHost: String, cloudantUsername: String, cloudantPassword: String) = new CloudantConfig {
    val host = cloudantHost
    val username = cloudantUsername
    val password = cloudantPassword
  }
}
