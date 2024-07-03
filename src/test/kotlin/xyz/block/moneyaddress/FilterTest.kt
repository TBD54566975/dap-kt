package xyz.block.moneyaddress

import xyz.block.moneyaddress.TypedMoneyAddressTest.Companion.btcLightningMoneyAddress1
import xyz.block.moneyaddress.TypedMoneyAddressTest.Companion.btcLightningMoneyAddress2
import xyz.block.moneyaddress.TypedMoneyAddressTest.Companion.btcOnChainMoneyAddress1
import xyz.block.moneyaddress.TypedMoneyAddressTest.Companion.btcOnChainMoneyAddress2
import xyz.block.moneyaddress.TypedMoneyAddressTest.Companion.manyMoneyAddresses
import xyz.block.moneyaddress.TypedMoneyAddressTest.Companion.zarMomoMoneyAddress1
import xyz.block.moneyaddress.TypedMoneyAddressTest.Companion.zarMomoMoneyAddress2
import xyz.block.moneyaddress.TypedMoneyAddressTest.Companion.zzzUnrecognizedMoneyAddress1
import xyz.block.moneyaddress.TypedMoneyAddressTest.Companion.zzzUnrecognizedMoneyAddress2
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
        btcLightningMoneyAddress2,
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
        zzzUnrecognizedMoneyAddress2,
      ),
      zzzAddresses
    )
  }

  @Test
  fun filterByProtocol() {
    val bitcoinAddresses = manyMoneyAddresses.filter { it.hasProtocol(ONCHAIN_ADDRESS) }
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
        zzzUnrecognizedMoneyAddress2,
      ),
      zzzAddresses
    )
  }

  @Test
  fun filterByCurrencyAndProtocol() {
    val bitcoinAddresses = manyMoneyAddresses.filter { it.matches(BTC, ONCHAIN_ADDRESS) }
    assertEquals(
      listOf(
        btcOnChainMoneyAddress1,
        btcOnChainMoneyAddress2,
      ),
      bitcoinAddresses
    )
  }

  @Test
  fun filterByCurrencyAndProtocolAsStrings() {
    val bitcoinAddresses = manyMoneyAddresses.filter { it.matches("zar", "momo") }
    assertEquals(
      listOf(
        zarMomoMoneyAddress1,
        zarMomoMoneyAddress2,
      ),
      bitcoinAddresses
    )
  }
}
