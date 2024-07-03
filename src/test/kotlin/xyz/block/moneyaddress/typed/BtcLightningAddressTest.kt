package xyz.block.moneyaddress.typed

import xyz.block.moneyaddress.MoneyAddressExamples.Companion.btcLightningMoneyAddress1
import xyz.block.moneyaddress.typed.BtcLightningAddress.Companion.toBtcLightningAddress
import kotlin.test.Test
import kotlin.test.assertEquals

class BtcLightningAddressTest {
  @Test
  fun toBtcLightningAddress() {
    assertEquals(
      BtcLightningAddress(btcLightningMoneyAddress1.pss, btcLightningMoneyAddress1.id),
      btcLightningMoneyAddress1.toBtcLightningAddress()
    )
  }
  @Test
  fun fromUntypedMoneyAddress() {
    assertEquals(
      BtcLightningAddress(btcLightningMoneyAddress1.pss, btcLightningMoneyAddress1.id),
      BtcLightningAddress.from(btcLightningMoneyAddress1)
    )
  }
}
