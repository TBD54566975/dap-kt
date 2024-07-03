package xyz.block.moneyaddress.typed

import xyz.block.moneyaddress.BTC
import xyz.block.moneyaddress.TypedMoneyAddress
import xyz.block.moneyaddress.MoneyAddressRegistry
import xyz.block.moneyaddress.ONCHAIN_ADDRESS
import xyz.block.moneyaddress.matches
import xyz.block.moneyaddress.MoneyAddress as UntypedMoneyAddress

/**
 * A typed representation of a Bitcoin on-chain address.
 * The `address` is the string representation of the on-chain address. This is not validated.
 * The `currency` will always be [BTC].
 * The `protocol` will always be [ONCHAIN_ADDRESS].
 */
data class BtcOnChainAddress(
  override val address: String,
  override val id: String,
) : TypedMoneyAddress<String>(address, BTC, ONCHAIN_ADDRESS, id) {
  companion object {
    fun register(moneyAddressRegistry: MoneyAddressRegistry) {
      moneyAddressRegistry.register(BTC, ONCHAIN_ADDRESS) { address -> from(address) }
    }

    /**
     * Converts an [UntypedMoneyAddress] to a [BtcOnChainAddress].
     *
     * @throws NotABtcOnChainAddressException if the currency is not [BTC] or the protocol is not [ONCHAIN_ADDRESS].
     */
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
