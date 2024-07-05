package xyz.block.moneyaddress.typed

import xyz.block.moneyaddress.MoneyAddressExamples.Companion.btcLightningAddressMoneyAddress1
import xyz.block.moneyaddress.typed.BtcLightningAddress.Companion.toBtcLightningAddress
import kotlin.test.Test
import kotlin.test.assertEquals

class BtcLightningAddressTest {
  @Test
  fun toBtcLightningAddress() {
    assertEquals(
      BtcLightningAddress(btcLightningAddressMoneyAddress1.pss, btcLightningAddressMoneyAddress1.id),
      btcLightningAddressMoneyAddress1.toBtcLightningAddress()
    )
  }
  @Test
  fun fromUntypedMoneyAddress() {
    assertEquals(
      BtcLightningAddress(btcLightningAddressMoneyAddress1.pss, btcLightningAddressMoneyAddress1.id),
      BtcLightningAddress.from(btcLightningAddressMoneyAddress1)
    )
  }
}
