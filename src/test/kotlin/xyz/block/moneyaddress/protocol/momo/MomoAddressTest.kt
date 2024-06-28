package xyz.block.moneyaddress.protocol.momo

import org.junit.jupiter.api.assertThrows
import xyz.block.moneyaddress.currency.Currency
import xyz.block.moneyaddress.filter.FilterTest.Companion.btcOnChainMoneyAddress1
import xyz.block.moneyaddress.filter.FilterTest.Companion.kesMomoMoneyAddress1
import xyz.block.moneyaddress.filter.FilterTest.Companion.kesMomoMoneyAddress2
import xyz.block.moneyaddress.filter.FilterTest.Companion.manyMoneyAddresses
import xyz.block.moneyaddress.filter.FilterTest.Companion.zarMomoMoneyAddress1
import xyz.block.moneyaddress.filter.FilterTest.Companion.zarMomoMoneyAddress2
import xyz.block.moneyaddress.filter.hasCurrency
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MomoAddressTest {

  private val kesMomoAddress1 = MomoAddress("kes", "mpesa", "11111", kesMomoMoneyAddress1)
  private val kesMomoAddress2 = MomoAddress("kes", "mpesa", "22222", kesMomoMoneyAddress2)
  private val zarMomoAddress1 = MomoAddress("zar", "mpesa", "33333", zarMomoMoneyAddress1)
  private val zarMomoAddress2 = MomoAddress("zar", "mpesa", "44444", zarMomoMoneyAddress2)

  @Test
  fun isMomoAddress() {
    assertTrue(kesMomoMoneyAddress1.isMomoAddress())
    assertFalse(btcOnChainMoneyAddress1.isMomoAddress())
  }

  @Test
  fun asMomoAddress() {
    assertEquals(
      kesMomoAddress1,
      kesMomoMoneyAddress1.asMomoAddress()
    )
    assertThrows<NotAMomoAddressException> {
      btcOnChainMoneyAddress1.asMomoAddress()
    }
  }

  @Test
  fun asMomoAddressOrNull() {
    assertEquals(
      kesMomoAddress1,
      kesMomoMoneyAddress1.asMomoAddressOrNull()
    )
    assertNull(
      btcOnChainMoneyAddress1.asMomoAddressOrNull()
    )
  }

  @Test
  fun asMomoAddresses() {
    val momoAddresses = listOf(kesMomoMoneyAddress1, zarMomoMoneyAddress1)
    val transformed = momoAddresses.asMomoAddresses()
    assertEquals(
      listOf(
        kesMomoAddress1,
        zarMomoAddress1,
      ),
      transformed
    )
    assertThrows<NotAMomoAddressException> {
      manyMoneyAddresses.asMomoAddresses()
    }
  }

  @Test
  fun findMomoAddress() {
    val transformed = manyMoneyAddresses.findMomoAddresses()
    assertEquals(
      listOf(
        kesMomoAddress1,
        kesMomoAddress2,
        zarMomoAddress1,
        zarMomoAddress2,
      ),
      transformed
    )
  }

  @Test
  fun filterByCurrencyAndType() {
    val kesMomoAddresses = manyMoneyAddresses
      .filter { it.hasCurrency(Currency.KES) }
      .filter { it.isMomoAddress() }
    assertEquals(
      listOf(
        kesMomoMoneyAddress1,
        kesMomoMoneyAddress2
      ),
      kesMomoAddresses
    )
  }

  @Test
  fun filterByCurrencyAndTypeAndTransform() {
    val zarMomoAddresses = manyMoneyAddresses
      .filter { it.hasCurrency(Currency.ZAR) }
      .findMomoAddresses()
    assertEquals(
      listOf(
        zarMomoAddress1,
        zarMomoAddress2
      ),
      zarMomoAddresses
    )
  }
}
