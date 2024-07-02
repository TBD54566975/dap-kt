package xyz.block.moneyaddress.typed.x

import xyz.block.moneyaddress.typed.MoneyAddressTest.Companion.btcLightningMoneyAddress1
import xyz.block.moneyaddress.typed.x.BtcLightningAddress.Companion.toBtcLightningAddress
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
