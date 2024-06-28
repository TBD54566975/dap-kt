package xyz.block.moneyaddress.currency.bitcoin

import org.junit.jupiter.api.assertThrows
import xyz.block.moneyaddress.filter.FilterTest.Companion.btcLightningMoneyAddress1
import xyz.block.moneyaddress.filter.FilterTest.Companion.manyMoneyAddresses
import xyz.block.moneyaddress.filter.FilterTest.Companion.btcOnChainMoneyAddress1
import xyz.block.moneyaddress.filter.FilterTest.Companion.btcOnChainMoneyAddress2
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class OnChainAddressTest {
  @Test
  fun isOnChainAddress() {
    assertTrue(btcOnChainMoneyAddress1.isOnChainAddress())
    assertFalse(btcLightningMoneyAddress1.isOnChainAddress())
  }

  @Test
  fun asOnChainAddress() {
    assertEquals(
      OnChainAddress(btcOnChainMoneyAddress1.pss, btcOnChainMoneyAddress1),
      btcOnChainMoneyAddress1.asOnChainAddress()
    )
    assertThrows<NotAnOnChainAddressException> {
      btcLightningMoneyAddress1.asOnChainAddress()
    }
  }


  @Test
  fun asOnChainAddressOrNull() {
    assertEquals(
      OnChainAddress(btcOnChainMoneyAddress1.pss, btcOnChainMoneyAddress1),
      btcOnChainMoneyAddress1.asOnChainAddressOrNull()
    )
    assertNull(
      btcLightningMoneyAddress1.asOnChainAddressOrNull()
    )
  }


  @Test
  fun asOnChainAddresses() {
    val onChainAddresses = listOf(btcOnChainMoneyAddress1, btcOnChainMoneyAddress2)
    val transformed = onChainAddresses.asOnChainAddressesOrThrow()
    assertEquals(
      listOf(
        OnChainAddress(btcOnChainMoneyAddress1.pss, btcOnChainMoneyAddress1),
        OnChainAddress(btcOnChainMoneyAddress2.pss, btcOnChainMoneyAddress2),
      ),
      transformed
    )
  }

  @Test
  fun findOnChainAddresses() {
    val transformed = manyMoneyAddresses.findOnChainAddresses()
    assertEquals(
      listOf(
        OnChainAddress(btcOnChainMoneyAddress1.pss, btcOnChainMoneyAddress1),
        OnChainAddress(btcOnChainMoneyAddress2.pss, btcOnChainMoneyAddress2)
      ),
      transformed
    )
  }
}
