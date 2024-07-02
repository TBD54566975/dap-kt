package xyz.block.moneyaddress.typed.x

import xyz.block.moneyaddress.typed.BTC
import xyz.block.moneyaddress.typed.MoneyAddress
import xyz.block.moneyaddress.typed.MoneyAddressRegistry
import xyz.block.moneyaddress.typed.ONCHAIN_ADDRESS
import xyz.block.moneyaddress.typed.matches
import xyz.block.moneyaddress.MoneyAddress as UntypedMoneyAddress

data class BtcOnChainAddress(
  override val address: String,
  override val id: String,
) : MoneyAddress<String>(address, BTC, ONCHAIN_ADDRESS, id) {
  init {
    MoneyAddressRegistry.register(BTC, ONCHAIN_ADDRESS) { address -> from(address) }
  }

  companion object {
    fun from(untypedMoneyAddress: UntypedMoneyAddress): BtcOnChainAddress {
      if (!untypedMoneyAddress.matches(BTC, ONCHAIN_ADDRESS)) {
        throw NotABtcOnChainAddressException(untypedMoneyAddress)
      }
      return BtcOnChainAddress(untypedMoneyAddress.pss, untypedMoneyAddress.id)
    }

    fun UntypedMoneyAddress.toBtcOnChainAddress(): BtcOnChainAddress = from(this)
  }
}

class NotABtcOnChainAddressException(moneyAddress: UntypedMoneyAddress) :
  Throwable("Not a bitcoin on-chain address [$moneyAddress]")
