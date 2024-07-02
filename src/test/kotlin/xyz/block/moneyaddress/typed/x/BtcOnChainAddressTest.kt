package xyz.block.moneyaddress.typed.x

import xyz.block.moneyaddress.typed.MoneyAddressTest.Companion.btcOnChainMoneyAddress1
import xyz.block.moneyaddress.typed.x.BtcOnChainAddress.Companion.toBtcOnChainAddress
import kotlin.test.Test
import kotlin.test.assertEquals

class BtcOnChainAddressTest {
  @Test
  fun toBtcOnChainAddress() {
    assertEquals(
      BtcOnChainAddress(btcOnChainMoneyAddress1.pss, btcOnChainMoneyAddress1.id),
      btcOnChainMoneyAddress1.toBtcOnChainAddress()
    )
  }

  @Test
  fun fromUntypedMoneyAddress() {
    assertEquals(
      BtcOnChainAddress(btcOnChainMoneyAddress1.pss, btcOnChainMoneyAddress1.id),
      BtcOnChainAddress.from(btcOnChainMoneyAddress1)
    )
  }
}
