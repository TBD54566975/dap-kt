package xyz.block.dap

import web5.sdk.dids.DidResolutionResult
import web5.sdk.dids.DidResolvers
import web5.sdk.dids.didcore.Did
import web5.sdk.dids.didcore.DidDocument
import web5.sdk.dids.didcore.Service
import web5.sdk.dids.methods.web.DidWeb
import xyz.block.moneyaddress.MoneyAddress
import xyz.block.moneyaddress.urn.Urn
import java.util.UUID
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MoneyAddressResolverTest {

  @BeforeTest
  fun beforeTest() {
  }

  @AfterTest
  fun afterTest() {
    DidResolvers.addResolver(DidWeb.methodName, DidWeb.Default::resolve)
  }

  @Test
  fun testResolvingMoneyAddressFromDid() {
    stubDidResolver(
      listOf(
        serviceForEndpoint("btc-on-chain", VALID_BITCOIN_ADDRESS_URN.toString())
      )
    )

    val moneyAddressResolver = MoneyAddressResolver()
    val moneyAddresses = moneyAddressResolver.resolveMoneyAddresses(VALID_DID)
    assertEquals(1, moneyAddresses.size)
    assertEquals(
      MoneyAddress(
        id = "btc-on-chain",
        urn = VALID_BITCOIN_ADDRESS_URN,
        currency = VALID_BITCOIN_ADDRESS_URN_CURRENCY,
        css = VALID_BITCOIN_ADDRESS_URN_CSS,
      ), moneyAddresses[0]
    )
  }

  @Test
  fun testResolvingMultipleMoneyAddressServicesFromDid() {
    stubDidResolver(
      listOf(
        serviceForEndpoint("btc-on-chain", VALID_BITCOIN_ADDRESS_URN.toString()),
        serviceForEndpoint("btc-lnurl", VALID_BITCOIN_LNURL_URN),
        serviceForEndpoint("usdc-eth", VALID_USDC_URN)
      )
    )

    val moneyAddressResolver = MoneyAddressResolver()
    val moneyAddresses = moneyAddressResolver.resolveMoneyAddresses(VALID_DID)
    assertEquals(3, moneyAddresses.size)
  }

  @Test
  fun testResolvingMultipleMoneyAddressWithMultipleUrnsFromDid() {
    stubDidResolver(
      listOf(
        serviceForEndpoints(
          "multiple-urns",
          listOf(VALID_BITCOIN_ADDRESS_URN.toString(), VALID_BITCOIN_LNURL_URN, VALID_USDC_URN)
        )
      )
    )

    val moneyAddressResolver = MoneyAddressResolver()
    val moneyAddresses = moneyAddressResolver.resolveMoneyAddresses(VALID_DID)
    assertEquals(3, moneyAddresses.size)
  }

  // TODO - tests for error cases

  private fun serviceForEndpoint(id: String, serviceEndpoint: String): Service {
    return Service.Builder()
      .id(id)
      .type(MoneyAddress.KIND)
      .serviceEndpoint(listOf(serviceEndpoint))
      .build()
  }

  private fun serviceForEndpoints(id: String, serviceEndpoints: List<String>): Service {
    return Service.Builder()
      .id(id)
      .type(MoneyAddress.KIND)
      .serviceEndpoint(serviceEndpoints)
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
    val VALID_DID = Did.parse("did:web:didpay.me")
    val VALID_BITCOIN_ADDRESS_URN = Urn.parse("urn:btc:addr:fakeAddress")
    const val VALID_BITCOIN_ADDRESS_URN_CURRENCY = "btc"
    const val VALID_BITCOIN_ADDRESS_URN_CSS = "addr:fakeAddress"
    val VALID_BITCOIN_LNURL_URN = "urn:btc:lnurl:fakeLnurl"
    val VALID_USDC_URN = "urn:usdc:eth:fakeEth"
  }
}
