package xyz.block.moneyaddress.filter

import xyz.block.moneyaddress.currency.BTC
import xyz.block.moneyaddress.protocol.ADDR
import xyz.block.moneyaddress.toMoneyAddress
import xyz.block.moneyaddress.urn.DapUrn
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class FilterTest {

  @Test
  fun filterByCurrency() {
    val bitcoinAddresses = manyMoneyAddresses.filter { it.hasCurrency(BTC) }
    assertEquals(
      listOf(
        btcOnChainMoneyAddress1,
        btcOnChainMoneyAddress2,
        btcLightningMoneyAddress1,
        btcLightningMoneyAddress2
      ),
      bitcoinAddresses
    )
  }

  @Test
  fun filterByCurrencyString() {
    val zzzAddresses = manyMoneyAddresses.filter { it.hasCurrency("zzz") }
    assertEquals(
      listOf(
        zzzUnrecognizedMoneyAddress1,
        zzzUnrecognizedMoneyAddress2
      ),
      zzzAddresses
    )
  }

  @Test
  fun filterByProtocol() {
    val bitcoinAddresses = manyMoneyAddresses.filter { it.hasProtocol(ADDR) }
    assertEquals(
      listOf(
        btcOnChainMoneyAddress1,
        btcOnChainMoneyAddress2,
      ),
      bitcoinAddresses
    )
  }

  @Test
  fun filterByProtocolString() {
    val zzzAddresses = manyMoneyAddresses.filter { it.hasProtocol("zzz") }
    assertEquals(
      listOf(
        zzzUnrecognizedMoneyAddress1,
        zzzUnrecognizedMoneyAddress2
      ),
      zzzAddresses
    )
  }

  @Test
  fun filterByCurrencyAndProtocol() {
    val bitcoinAddresses = manyMoneyAddresses.filter {
      it.hasCurrency(BTC) && it.hasProtocol(ADDR)
    }
    assertEquals(
      listOf(
        btcOnChainMoneyAddress1,
        btcOnChainMoneyAddress2,
      ),
      bitcoinAddresses
    )
  }

  // @Test
  // fun transform() {
  //   val lightningAddresses = manyMoneyAddresses.transform { moneyAddress ->
  //     if (moneyAddress.isLightningAddress()) moneyAddress.asLightningAddress()
  //     else null
  //   }
  //   val lightningAddresses2 = manyMoneyAddresses.transform { moneyAddress ->
  //     moneyAddress.asLightningAddressOrNull()
  //   }
  //   val lightningAddresses3 = manyMoneyAddresses.asLightningAddresses()
  //
  //   assertEquals(
  //     listOf(
  //       LightningAddress(lightningAddress1, lightningAddress1.pss),
  //       LightningAddress(lightningAddress2, lightningAddress2.pss),
  //     ),
  //     lightningAddresses
  //   )
  // }

  companion object {
    val btcOnChainMoneyAddress1 = makeMoneyAddress("btc", "addr")
    val btcOnChainMoneyAddress2 = makeMoneyAddress("btc", "addr")
    val btcLightningMoneyAddress1 = makeMoneyAddress("btc", "lnaddr")
    val btcLightningMoneyAddress2 = makeMoneyAddress("btc", "lnaddr")
    val usdcEthMoneyAddress1 = makeMoneyAddress("usdc", "eth")
    val usdcEthMoneyAddress2 = makeMoneyAddress("usdc", "eth")
    val kesMomoMoneyAddress1 = makeMoneyAddress("kes", "momo", "mpesa:11111")
    val kesMomoMoneyAddress2 = makeMoneyAddress("kes", "momo", "mpesa:22222")
    val zarMomoMoneyAddress1 = makeMoneyAddress("zar", "momo", "mpesa:33333")
    val zarMomoMoneyAddress2 = makeMoneyAddress("zar", "momo", "mpesa:44444")
    val zzzUnrecognizedMoneyAddress1 = makeMoneyAddress("zzz", "zzz", "zzz1")
    val zzzUnrecognizedMoneyAddress2 = makeMoneyAddress("zzz", "zzz", "zzz2")
    val zzzUnrecognizedMomoMoneyAddress1 = makeMoneyAddress("zzz", "momo", "zzz:11111")

    val manyMoneyAddresses = listOf(
      btcOnChainMoneyAddress1,
      btcOnChainMoneyAddress2,
      btcLightningMoneyAddress1,
      btcLightningMoneyAddress2,
      usdcEthMoneyAddress1,
      usdcEthMoneyAddress2,
      kesMomoMoneyAddress1,
      kesMomoMoneyAddress2,
      zarMomoMoneyAddress1,
      zarMomoMoneyAddress2,
      zzzUnrecognizedMoneyAddress1,
      zzzUnrecognizedMoneyAddress2
    )

    private fun makeMoneyAddress(currency: String, protocol: String, pss: String? = null) =
      DapUrn(currency, protocol, pss ?: "fake-$currency-$protocol-address")
        .toMoneyAddress(UUID.randomUUID().toString())
  }
}
