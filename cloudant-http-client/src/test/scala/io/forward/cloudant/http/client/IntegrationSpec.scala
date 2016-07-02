package io.forward.cloudant.http.client

import org.scalatest._

class IntegrationSpec extends WordSpecLike with Matchers {

  private val cloudant = Cloudant(
    System.getenv("CLOUDANT_HOST"),
    System.getenv("CLOUDANT_USERNAME"),
    System.getenv("CLOUDANT_PASSWORD"))

  "The HTTP client" when {
    "creating documents" should {

      "create a document" in {

      }
    }
  }
}
