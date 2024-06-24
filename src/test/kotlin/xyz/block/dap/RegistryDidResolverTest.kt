package xyz.block.dap

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
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
    val engine = mockEngine()
    val registryDidResolver = RegistryDidResolver { engine }
    val did = registryDidResolver.getDid(VALID_URL, VALID_DAP)
    assertEquals(VALID_DID.toString(), did.toString())
  }

  // TODO - tests for error cases

  private fun mockEngine() = MockEngine { request ->
    when (request.url.toString()) {
      "$VALID_URL/daps/${VALID_DAP.handle}" -> {
        respond(
          content = ByteReadChannel("""{did: "$VALID_DID"}"""),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
      }

      else -> error("Unhandled ${request.url}")
    }
  }

  companion object {
    val VALID_URL = URL("https://didpay.me")
    val VALID_DAP = Dap("moegrammer", "didpay.me")
    val VALID_DID = Did.parse("did:web:didpay.me:moegrammer")
  }
}
