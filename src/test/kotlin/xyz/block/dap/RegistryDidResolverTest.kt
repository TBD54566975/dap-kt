package xyz.block.dap

import web5.sdk.dids.didcore.Did
import java.net.URL
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RegistryDidResolverTest {

  @BeforeTest
  fun beforeTest() {
  }

  @AfterTest
  fun afterTest() {
  }

  @Test
  fun testResolvingDidFromRegistryUrl() {
    val registryDidResolver = RegistryDidResolver()
    val did = registryDidResolver.getDid(VALID_URL, VALID_DAP)
    assertEquals(VALID_DID.toString(), did.toString())
  }

  companion object {
    val VALID_URL = URL("https://didpay.me")
    val VALID_DAP = Dap("moegrammer", "didpay.me")
    val VALID_DID = Did.parse("did:web:didpay.me:moegrammer")
  }
}
