package xyz.block.dap

import org.junit.jupiter.api.assertThrows
import web5.sdk.dids.DidResolutionResult
import web5.sdk.dids.DidResolvers
import web5.sdk.dids.didcore.DidDocument
import web5.sdk.dids.didcore.Service
import web5.sdk.dids.methods.web.DidWeb
import java.util.UUID
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class RegistryResolverTest {

  @BeforeTest
  fun beforeTest() {
    stubDidResolver(listOf(serviceForEndpoint(VALID_SERVICE_ENDPOINT)))
  }

  @AfterTest
  fun afterTest() {
    DidResolvers.addResolver(DidWeb.methodName, DidWeb.Default::resolve)
  }

  @Test
  fun testResolvingRegistryUrlFromDap() {
    val registryResolver = RegistryResolver()
    val url = registryResolver.resolveRegistryUrl(VALID_DAP)
    assertEquals(VALID_SERVICE_ENDPOINT, url.toString())
  }

  @Test
  fun testResolvingRegistryUrlWhenDidResolutionFails() {
    DidResolvers.addResolver(DidWeb.methodName) { _ ->
      throw RuntimeException("boom!")
    }
    val registryResolver = RegistryResolver()
    val exception = assertThrows<RegistryResolutionException> {
      registryResolver.resolveRegistryUrl(VALID_DAP)
    }
    assertNotNull(exception.message)
    assertContains(exception.message!!, "DID resolution failed")
    assertEquals("boom!", exception.cause?.message)
  }

  @Test
  fun testResolvingRegistryUrlWhenDidHasNoDapRegistryService() {
    stubDidResolver(emptyList())
    val registryResolver = RegistryResolver()
    val exception = assertThrows<RegistryResolutionException> {
      registryResolver.resolveRegistryUrl(VALID_DAP)
    }
    assertNotNull(exception.message)
    assertContains(exception.message!!, "DID document has no DAP registry service")
  }

  @Test
  fun testResolvingRegistryUrlWhenDapRegistryHasInvalidServiceEndpoint() {
    stubDidResolver(listOf(serviceForEndpoint("not-a-valid-service-endpoint")))
    val registryResolver = RegistryResolver()
    val exception = assertThrows<RegistryResolutionException> {
      registryResolver.resolveRegistryUrl(VALID_DAP)
    }
    assertNotNull(exception.message)
    assertContains(exception.message!!, "Invalid DAP registry url")
    assertEquals("no protocol: not-a-valid-service-endpoint", exception.cause?.message)
  }

  private fun serviceForEndpoint(serviceEndpoint: String): Service {
    return Service.Builder()
      .id(UUID.randomUUID().toString())
      .type(Dap.SERVICE_TYPE)
      .serviceEndpoint(listOf(serviceEndpoint))
      .build()
  }

  private fun stubDidResolver(services: List<Service>) {
    DidResolvers.addResolver(DidWeb.methodName) { _ ->
      DidResolutionResult(
        didDocument = DidDocument(
          id = UUID.randomUUID().toString(),
          service = services
        )
      )
    }
  }

  companion object {
    val VALID_DAP = Dap("valid-dap-handle", "valid-dap-domain")
    const val VALID_SERVICE_ENDPOINT = "https://dap.didpay.xyz"
  }
}
