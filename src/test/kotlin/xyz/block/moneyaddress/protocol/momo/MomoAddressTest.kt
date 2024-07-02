package xyz.block.moneyaddress.protocol.momo

import org.junit.jupiter.api.assertThrows
import xyz.block.moneyaddress.currency.KES
import xyz.block.moneyaddress.currency.ZAR
import xyz.block.moneyaddress.filter.FilterTest.Companion.btcOnChainMoneyAddress1
import xyz.block.moneyaddress.filter.FilterTest.Companion.kesMomoMoneyAddress1
import xyz.block.moneyaddress.filter.FilterTest.Companion.kesMomoMoneyAddress2
import xyz.block.moneyaddress.filter.FilterTest.Companion.manyMoneyAddresses
import xyz.block.moneyaddress.filter.FilterTest.Companion.zarMomoMoneyAddress1
import xyz.block.moneyaddress.filter.FilterTest.Companion.zarMomoMoneyAddress2
import xyz.block.moneyaddress.filter.FilterTest.Companion.zzzUnrecognizedMomoMoneyAddress1
import xyz.block.moneyaddress.filter.hasCurrency
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MomoAddressTest {

  private val kesMpesaAddress1 = MomoAddress(KES, MPESA, "11111", kesMomoMoneyAddress1.id)
  private val kesMpesaAddress2 = MomoAddress(KES, MPESA, "22222", kesMomoMoneyAddress2.id)
  private val zarMpesaAddress1 = MomoAddress(ZAR, MPESA, "33333", zarMomoMoneyAddress1.id)
  private val zarMpesaAddress2 = MomoAddress(ZAR, MPESA, "44444", zarMomoMoneyAddress2.id)
  private val zarZZZAddress1 = MomoAddress(ZAR, UNRECOGNIZED_CARRIER("zzz"), "44444", zzzUnrecognizedMomoMoneyAddress1.id)

  @Test
  fun isMomoAddress() {
    assertTrue(kesMomoMoneyAddress1.isMomoAddress())
    assertFalse(btcOnChainMoneyAddress1.isMomoAddress())
    assertTrue(zzzUnrecognizedMomoMoneyAddress1.isMomoAddress())
  }

  @Test
  fun asMomoAddress() {
    assertEquals(
      kesMpesaAddress1,
      kesMomoMoneyAddress1.asMomoAddressOrThrow()
    )
    assertThrows<NotAMomoAddressException> {
      btcOnChainMoneyAddress1.asMomoAddressOrThrow()
    }
  }

  @Test
  fun asMomoAddressOrNull() {
    assertEquals(
      kesMpesaAddress1,
      kesMomoMoneyAddress1.asMomoAddressOrNull()
    )
    assertNull(
      btcOnChainMoneyAddress1.asMomoAddressOrNull()
    )
  }

  @Test
  fun asMomoAddresses() {
    val momoAddresses = listOf(kesMomoMoneyAddress1, zarMomoMoneyAddress1)
    val transformed = momoAddresses.asMomoAddressesOrThrow()
    assertEquals(
      listOf(
        kesMpesaAddress1,
        zarMpesaAddress1,
      ),
      transformed
    )
    assertThrows<NotAMomoAddressException> {
      manyMoneyAddresses.asMomoAddressesOrThrow()
    }
  }

  @Test
  fun findMomoAddress() {
    val transformed = manyMoneyAddresses.findMomoAddresses()
    assertEquals(
      listOf(
        kesMpesaAddress1,
        kesMpesaAddress2,
        zarMpesaAddress1,
        zarMpesaAddress2,
      ),
      transformed
    )
  }

  @Test
  fun filterByCurrencyAndType() {
    val kesMomoAddresses = manyMoneyAddresses
      .filter { it.hasCurrency(KES) }
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
      .filter { it.hasCurrency(ZAR) }
      .findMomoAddresses()
    assertEquals(
      listOf(
        zarMpesaAddress1,
        zarMpesaAddress2
      ),
      zarMomoAddresses
    )
  }
}
