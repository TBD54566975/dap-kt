package xyz.block.moneyaddress.typed

import xyz.block.moneyaddress.KES
import xyz.block.moneyaddress.MoneyAddressExamples.Companion.makeMoneyAddress
import xyz.block.moneyaddress.UNRECOGNIZED_CURRENCY
import xyz.block.moneyaddress.typed.MobileMoneyAddress.Companion.toMobileMoneyAddress
import kotlin.test.Test
import kotlin.test.assertEquals

class MobileTypedMoneyAddressTest {
  @Test
  fun toMobileMoneyAddress() {
    assertEquals(
      MobileMoneyAddress(KES, MomoAddress(MPESA, "11111"), kesMomoMpesaMoneyAddress.id),
      kesMomoMpesaMoneyAddress.toMobileMoneyAddress()
    )
  }

  @Test fun fromUntypedMoneyAddress() {
    assertEquals(
      MobileMoneyAddress(KES, MomoAddress(MPESA, "11111"), kesMomoMpesaMoneyAddress.id),
      MobileMoneyAddress.from(kesMomoMpesaMoneyAddress)
    )
    val zzzMomoCheeseMoneyAddress =
      makeMoneyAddress("zzz", "momo", "cheese:11111")
    assertEquals(
      MobileMoneyAddress(
        UNRECOGNIZED_CURRENCY("zzz"),
        MomoAddress(UNRECOGNIZED_CARRIER("cheese"), "11111"),
        zzzMomoCheeseMoneyAddress.id
      ),
      MobileMoneyAddress.from(zzzMomoCheeseMoneyAddress)
    )
  }

  private val kesMomoMpesaMoneyAddress =
    makeMoneyAddress("kes", "momo", "mpesa:11111")
}
