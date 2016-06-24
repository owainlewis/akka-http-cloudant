package io.forward.cloudant.http.client

import org.scalatest._

class CloudantConfigSpec extends WordSpecLike with Matchers {

  "The CloudantConfig" should {

    "get constructed correctly" in {
      val config = CloudantConfig("http://user.cloudant.com", "username", "password")
      config.host shouldBe "http://user.cloudant.com"
      config.username shouldBe "username"
      config.password shouldBe "password"
    }
  }
}
