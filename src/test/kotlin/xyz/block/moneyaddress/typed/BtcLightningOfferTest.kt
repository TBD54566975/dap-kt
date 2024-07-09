package xyz.block.moneyaddress.typed

import xyz.block.moneyaddress.MoneyAddressExamples.Companion.btcLightningOfferMoneyAddress1
import xyz.block.moneyaddress.typed.BtcLightningOffer.Companion.toBtcLightningOffer
import kotlin.test.Test
import kotlin.test.assertEquals

class BtcLightningOfferTest {
  @Test
  fun toBtcLightningOffer() {
    assertEquals(
      BtcLightningOffer(btcLightningOfferMoneyAddress1.pss, btcLightningOfferMoneyAddress1.id),
      btcLightningOfferMoneyAddress1.toBtcLightningOffer()
    )
  }
  @Test
  fun fromUntypedMoneyOffer() {
    assertEquals(
      BtcLightningOffer(btcLightningOfferMoneyAddress1.pss, btcLightningOfferMoneyAddress1.id),
      BtcLightningOffer.from(btcLightningOfferMoneyAddress1)
    )
  }
}
