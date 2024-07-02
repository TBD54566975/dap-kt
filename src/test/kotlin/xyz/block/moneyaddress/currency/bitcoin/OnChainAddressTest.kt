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
      btcOnChainMoneyAddress1.asOnChainAddressOrThrow()
    )
    assertThrows<NotAnOnChainAddressException> {
      btcLightningMoneyAddress1.asOnChainAddressOrThrow()
    }
  }

  @Test
  fun asOnChainAddressOrNull() {
    assertEquals(
      OnChainAddress(btcOnChainMoneyAddress1.pss, btcOnChainMoneyAddress1),
      btcOnChainMoneyAddress1.asOnChainAddress()
    )
    assertNull(
      btcLightningMoneyAddress1.asOnChainAddress()
    )
  }

  @Test
  fun asOnChainAddresses() {
    val onChainAddresses =
      listOf(btcOnChainMoneyAddress1, btcOnChainMoneyAddress2).asOnChainAddressesOrThrow()
    assertEquals(
      listOf(
        OnChainAddress(btcOnChainMoneyAddress1.pss, btcOnChainMoneyAddress1),
        OnChainAddress(btcOnChainMoneyAddress2.pss, btcOnChainMoneyAddress2),
      ),
      onChainAddresses
    )
  }

  @Test
  fun findOnChainAddresses() {
    val onChainAddresses = manyMoneyAddresses.findOnChainAddresses()
    assertEquals(
      listOf(
        OnChainAddress(btcOnChainMoneyAddress1.pss, btcOnChainMoneyAddress1),
        OnChainAddress(btcOnChainMoneyAddress2.pss, btcOnChainMoneyAddress2)
      ),
      onChainAddresses
    )
  }
}
