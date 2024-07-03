package xyz.block.moneyaddress.typed

import xyz.block.moneyaddress.BTC
import xyz.block.moneyaddress.LIGHTNING_ADDRESS
import xyz.block.moneyaddress.MoneyAddressRegistry
import xyz.block.moneyaddress.TypedMoneyAddress
import xyz.block.moneyaddress.matches
import xyz.block.moneyaddress.MoneyAddress as UntypedMoneyAddress

/**
 * A typed representation of a Bitcoin lightning address.
 * The `address` is the string representation of the lightning address. This is not validated.
 * The `currency` will always be [BTC].
 * The `protocol` will always be [LIGHTNING_ADDRESS].
 */
data class BtcLightningAddress(
  override val address: String,
  override val id: String,
) : TypedMoneyAddress<String>(address, BTC, LIGHTNING_ADDRESS, id) {
  companion object {
    fun register() {
      MoneyAddressRegistry.register(BTC, LIGHTNING_ADDRESS) { address -> from(address) }
    }

    /**
     * Converts an [UntypedMoneyAddress] to a [BtcLightningAddress].
     *
     * @throws NotABtcLightningAddressException if the currency is not [BTC] or the protocol is not [LIGHTNING_ADDRESS].
     */
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
