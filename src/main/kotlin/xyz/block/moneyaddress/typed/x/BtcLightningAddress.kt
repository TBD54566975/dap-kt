package xyz.block.moneyaddress.typed.x

import xyz.block.moneyaddress.typed.BTC
import xyz.block.moneyaddress.typed.LIGHTNING_ADDRESS
import xyz.block.moneyaddress.typed.MoneyAddressRegistry
import xyz.block.moneyaddress.typed.MoneyAddress
import xyz.block.moneyaddress.typed.matches
import xyz.block.moneyaddress.MoneyAddress as UntypedMoneyAddress

data class BtcLightningAddress(
  override val address: String,
  override val id: String,
) : MoneyAddress<String>(address, BTC, LIGHTNING_ADDRESS, id) {
  init {
    MoneyAddressRegistry.register(BTC, LIGHTNING_ADDRESS) { address -> from(address) }
  }

  companion object {
    fun from(untypedMoneyAddress: UntypedMoneyAddress): BtcLightningAddress {
      if (!untypedMoneyAddress.matches(BTC, LIGHTNING_ADDRESS)) {
        throw NotABtcLightningAddressException(untypedMoneyAddress)
      }
      return BtcLightningAddress(untypedMoneyAddress.pss, untypedMoneyAddress.id)
    }

    fun UntypedMoneyAddress.toBtcLightningAddress(): BtcLightningAddress = from(this)
  }
}

class NotABtcLightningAddressException(moneyAddress: UntypedMoneyAddress) :
  Throwable("Not a bitcoin lightning address [$moneyAddress]")
