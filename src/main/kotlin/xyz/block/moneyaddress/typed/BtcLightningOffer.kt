package xyz.block.moneyaddress.typed

import xyz.block.moneyaddress.BTC
import xyz.block.moneyaddress.LIGHTNING_OFFER
import xyz.block.moneyaddress.TypedMoneyAddressRegistry
import xyz.block.moneyaddress.TypedMoneyAddress
import xyz.block.moneyaddress.matches
import xyz.block.moneyaddress.MoneyAddress as UntypedMoneyAddress

/**
 * A typed representation of a Bitcoin Lightning Offer
 * i.e. a re-usable payment code from [BOLT 12](https://github.com/rustyrussell/lightning-rfc/blob/guilt/offers/12-offer-encoding.md)
 *
 * @property address - the string representation of the lightning offer. Not validated.
 * @property currency - always [BTC].
 * @property protocol - always [LIGHTNING_OFFER].
 */
data class BtcLightningOffer(
  override val address: String,
  override val id: String,
) : TypedMoneyAddress<String>(address, BTC, LIGHTNING_OFFER, id) {
  companion object {
    fun register(typedMoneyAddressRegistry: TypedMoneyAddressRegistry) {
      typedMoneyAddressRegistry.register(BTC, LIGHTNING_OFFER) { address -> from(address) }
    }

    /**
     * Converts an [UntypedMoneyAddress] to a [BtcLightningAddress].
     *
     * @throws NotABtcLightningAddressException if the currency is not [BTC] or the protocol is not [LIGHTNING_OFFER].
     */
    fun from(untypedMoneyAddress: UntypedMoneyAddress): BtcLightningOffer {
      if (!untypedMoneyAddress.matches(BTC, LIGHTNING_OFFER)) {
        throw NotABtcLightningOfferException(untypedMoneyAddress)
      }
      return BtcLightningOffer(untypedMoneyAddress.pss, untypedMoneyAddress.id)
    }

    fun UntypedMoneyAddress.toBtcLightningOffer(): BtcLightningOffer = from(this)
  }
}

class NotABtcLightningOfferException(moneyAddress: UntypedMoneyAddress) :
  Throwable("Not a bitcoin lightning offer [$moneyAddress]")
