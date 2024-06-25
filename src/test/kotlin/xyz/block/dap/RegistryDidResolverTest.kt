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
    val registryDidResolver = customRegistryDidResolver { engine = mockEngine() }
    val did = registryDidResolver.getUnprovenDid(VALID_URL, VALID_DAP)
    assertEquals(VALID_DID.toString(), did.toString())
  }

  @Test
  fun testResolvingDidWithProofFromRegistryUrl() {
    val registryDidResolver = customRegistryDidResolver { engine = mockEngine() }
    val didWithProof = registryDidResolver.getDidWithProof(VALID_URL, VALID_DAP_WITH_PROOF)
    assertEquals(VALID_DID.toString(), didWithProof.did.toString())
    assertEquals(VALID_PROOF, didWithProof.proof)
  }

  @Test
  fun testResolvingDidWithExtraFieldsFromRegistryUrl() {
    val registryDidResolver = customRegistryDidResolver { engine = mockEngine() }
    val did = registryDidResolver.getUnprovenDid(VALID_URL, VALID_DAP_WITH_EXTRA_FIELDS)
    assertEquals(VALID_DID.toString(), did.toString())
  }

  // TODO - tests for error cases

  private fun mockEngine() = MockEngine { request ->
    when (request.url.toString()) {
      "$VALID_URL/daps/${VALID_DAP.handle}" -> {
        respond(
          content = ByteReadChannel("""{"did": "$VALID_DID"}"""),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
      }

      "$VALID_URL/daps/${VALID_DAP_WITH_PROOF.handle}" -> {
        respond(
          content = ByteReadChannel(
            """
            {
              "did": "$VALID_DID",
              "proof": {
                "id": "${VALID_PROOF.id}",
                "handle": "${VALID_PROOF.handle}",
                "did": "${VALID_PROOF.did}",
                "domain": "${VALID_PROOF.domain}",
                "signature": "${VALID_PROOF.signature}",
                "extrafield": "extrafield"
              }
            }
            """.trimMargin()
          ),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
      }

      "$VALID_URL/daps/${VALID_DAP_WITH_EXTRA_FIELDS.handle}" -> {
        respond(
          content = ByteReadChannel(
            """
            {
              "did": "$VALID_DID",
              "extrafield": "extrafield"
            }
            """.trimMargin()
          ),
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
    val VALID_DAP_WITH_PROOF = Dap("proof", "didpay.me")
    val VALID_DAP_WITH_EXTRA_FIELDS = Dap("extrafields", "didpay.me")

    val VALID_DID = Did.parse("did:web:didpay.me:moegrammer")
    val VALID_PROOF = Proof(
      did = VALID_DID.toString(),
      domain = "didpay.me",
      handle = "moegrammer",
      id = "reg_01j13n944betnsesdve4ewc9c7",
      signature = "signature",
    )
  }
}
