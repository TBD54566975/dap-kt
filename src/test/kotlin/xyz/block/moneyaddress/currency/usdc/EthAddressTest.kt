package xyz.block.moneyaddress.currency.usdc

import org.junit.jupiter.api.assertThrows
import xyz.block.moneyaddress.filter.FilterTest.Companion.btcOnChainMoneyAddress1
import xyz.block.moneyaddress.filter.FilterTest.Companion.manyMoneyAddresses
import xyz.block.moneyaddress.filter.FilterTest.Companion.usdcEthMoneyAddress1
import xyz.block.moneyaddress.filter.FilterTest.Companion.usdcEthMoneyAddress2
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EthAddressTest {
  @Test
  fun isEthAddress() {
    assertTrue(usdcEthMoneyAddress1.isEthAddress())
    assertFalse(btcOnChainMoneyAddress1.isEthAddress())
  }

  @Test
  fun asEthAddress() {
    assertEquals(
      EthAddress(usdcEthMoneyAddress1.pss, usdcEthMoneyAddress1),
      usdcEthMoneyAddress1.asEthAddress()
    )
    assertThrows<NotAnEthAddressException> {
      btcOnChainMoneyAddress1.asEthAddress()
    }
  }

  @Test
  fun asEthAddressOrNull() {
    assertEquals(
      EthAddress(usdcEthMoneyAddress1.pss, usdcEthMoneyAddress1),
      usdcEthMoneyAddress1.asEthAddressOrNull()
    )
    assertNull(
      btcOnChainMoneyAddress1.asEthAddressOrNull()
    )
  }

  @Test
  fun asEhtAddresses() {
    val ethAddresses = listOf(usdcEthMoneyAddress1, usdcEthMoneyAddress2)
    val transformed = ethAddresses.asEthAddresses()
    assertEquals(
      listOf(
        EthAddress(usdcEthMoneyAddress1.pss, usdcEthMoneyAddress1),
        EthAddress(usdcEthMoneyAddress2.pss, usdcEthMoneyAddress2)
      ),
      transformed
    )
    assertThrows<NotAnEthAddressException> {
      manyMoneyAddresses.asEthAddresses()
    }
  }

  @Test
  fun findEhtAddresses() {
    val transformed = manyMoneyAddresses.findEthAddresses()
    assertEquals(
      listOf(
        EthAddress(usdcEthMoneyAddress1.pss, usdcEthMoneyAddress1),
        EthAddress(usdcEthMoneyAddress2.pss, usdcEthMoneyAddress2)
      ),
      transformed
    )
  }
}
