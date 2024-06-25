package xyz.block.dap

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import org.junit.jupiter.api.assertThrows
import web5.sdk.dids.didcore.Did
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals

class RegistryDidResolverTest {

  private val registryDidResolver = RegistryDidResolver { engine = mockEngine() }

  @Test
  fun testResolvingDidFromRegistryUrl() {
    val did = registryDidResolver.getUnprovenDid(VALID_URL, VALID_DAP)
    assertEquals(VALID_DID.toString(), did.toString())
  }

  @Test
  fun testResolvingDidWithExtraFieldsFromRegistryUrl() {
    val did = registryDidResolver.getUnprovenDid(VALID_URL, VALID_DAP_WITH_EXTRA_FIELDS)
    assertEquals(VALID_DID.toString(), did.toString())
  }

  @Test
  fun testResolvingDidWithProofFromRegistryUrl() {
    val didWithProof = registryDidResolver.getDidWithProof(VALID_URL, VALID_DAP_WITH_PROOF)
    assertEquals(VALID_DID.toString(), didWithProof.did.toString())
    assertEquals(VALID_PROOF, didWithProof.proof)
  }

  @Test
  fun testErrorsFetchingFromRegistry() {
    val errorDaps = listOf(
      UNKNOWN_DAP to "Error fetching DAP from registry",
      EMPTY_RESPONSE_DAP to "DAP registry did not return a DID",
      INVALID_RESPONSE_DAP to "Failed to parse DAP registry response",
      INVALID_DID_DAP to "Failed to parse DID from DAP registry response",
    )
    errorDaps.forEach { (dap, expectedMessage) ->
      val exception = assertThrows<RegistryDidResolutionException> {
        registryDidResolver.getUnprovenDid(VALID_URL, dap)
      }
      assertEquals(expectedMessage, exception.message)
    }
  }

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
                "extra-field": "extra-field"
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
              "extra-field": "extra-field"
            }
            """.trimMargin()
          ),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
      }

      "$VALID_URL/daps/${EMPTY_RESPONSE_DAP.handle}" -> {
        respond(
          content = ByteReadChannel("""{}"""),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
      }

      "$VALID_URL/daps/${INVALID_RESPONSE_DAP.handle}" -> {
        respond(
          content = ByteReadChannel("""invalid-response"""),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
      }

      "$VALID_URL/daps/${INVALID_DID_DAP.handle}" -> {
        respond(
          content = ByteReadChannel("""{"did": "invalid-did"}"""),
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
    val VALID_DAP_WITH_EXTRA_FIELDS = Dap("extra-field", "didpay.me")

    val VALID_DID = Did.parse("did:web:didpay.me:moegrammer")
    val VALID_PROOF = Proof(
      did = VALID_DID.toString(),
      domain = "didpay.me",
      handle = "moegrammer",
      id = "reg_01j13n944betnsesdve4ewc9c7",
      signature = "eyJhbGciOiJFZERTQSIsImtpZCI6ImRpZDp3ZWI6ZGlkcGF5Lm1lOm1vZWdyYW1tZXIjMCJ9.." +
        "jD38MMEGR4q9mtupJZ_kNEI3RoqKhhBh3fpdkRUskhLIm0Mr3-5Egm0XflSxkFxyby3Mq8DhL71QImojWJQZCw",
    )

    val UNKNOWN_DAP = Dap("i-don't-know-you", "didpay.me")
    val EMPTY_RESPONSE_DAP = Dap("empty-response", "didpay.me")
    val INVALID_RESPONSE_DAP = Dap("invalid-response", "didpay.me")
    val INVALID_DID_DAP = Dap("invalid-did", "didpay.me")
  }
}
