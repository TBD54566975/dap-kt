package xyz.block.moneyaddress.filter

import xyz.block.moneyaddress.currency.Currency.BTC
import xyz.block.moneyaddress.currency.Currency.USDC
import xyz.block.moneyaddress.protocol.Protocol.ADDR
import xyz.block.moneyaddress.protocol.Protocol.LNADDR
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
    val bitcoinAddresses = manyMoneyAddresses.filter { it.hasCurrency(USDC.scheme) }
    assertEquals(
      listOf(
        usdcEthMoneyAddress1,
        usdcEthMoneyAddress2
      ),
      bitcoinAddresses
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
    val bitcoinAddresses = manyMoneyAddresses.filter { it.hasProtocol(LNADDR.scheme) }
    assertEquals(
      listOf(
        btcLightningMoneyAddress1,
        btcLightningMoneyAddress2
      ),
      bitcoinAddresses
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
      zarMomoMoneyAddress2
    )

    private fun makeMoneyAddress(currency: String, protocol: String, pss: String? = null) =
      DapUrn(currency, protocol, pss ?: "fake-$currency-$protocol-address")
        .toMoneyAddress(UUID.randomUUID().toString())
  }
}
