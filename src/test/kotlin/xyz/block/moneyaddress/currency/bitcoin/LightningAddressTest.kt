package xyz.block.moneyaddress.currency.bitcoin

import org.junit.jupiter.api.assertThrows
import xyz.block.moneyaddress.filter.FilterTest.Companion.btcLightningMoneyAddress1
import xyz.block.moneyaddress.filter.FilterTest.Companion.btcLightningMoneyAddress2
import xyz.block.moneyaddress.filter.FilterTest.Companion.manyMoneyAddresses
import xyz.block.moneyaddress.filter.FilterTest.Companion.btcOnChainMoneyAddress1
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LightningAddressTest {
  @Test
  fun isLightningAddress() {
    assertTrue(btcLightningMoneyAddress1.isLightningAddress())
    assertFalse(btcOnChainMoneyAddress1.isLightningAddress())
  }

  @Test
  fun asLightningAddress() {
    assertEquals(
      LightningAddress(btcLightningMoneyAddress1.pss, btcLightningMoneyAddress1),
      btcLightningMoneyAddress1.asLightningAddressOrThrow()
    )
    assertThrows<NotALightningAddressException> {
      btcOnChainMoneyAddress1.asLightningAddressOrThrow()
    }
  }

  @Test
  fun asLightningAddressOrNull() {
    assertEquals(
      LightningAddress(btcLightningMoneyAddress1.pss, btcLightningMoneyAddress1),
      btcLightningMoneyAddress1.asLightningAddress()
    )
    assertNull(
      btcOnChainMoneyAddress1.asLightningAddress()
    )
  }

  @Test
  fun asLightningAddresses() {
    val lightningAddresses =
      listOf(btcLightningMoneyAddress1, btcLightningMoneyAddress2).asLightningAddressesOrThrow()
    assertEquals(
      listOf(
        LightningAddress(btcLightningMoneyAddress1.pss, btcLightningMoneyAddress1),
        LightningAddress(btcLightningMoneyAddress2.pss, btcLightningMoneyAddress2)
      ),
      lightningAddresses
    )
    assertThrows<NotALightningAddressException> {
      manyMoneyAddresses.asLightningAddressesOrThrow()
    }
  }

  @Test
  fun findLightningAddresses() {
    val lightningAddresses = manyMoneyAddresses.findLightningAddresses()
    assertEquals(
      listOf(
        LightningAddress(btcLightningMoneyAddress1.pss, btcLightningMoneyAddress1),
        LightningAddress(btcLightningMoneyAddress2.pss, btcLightningMoneyAddress2)
      ),
      lightningAddresses
    )
  }
}
