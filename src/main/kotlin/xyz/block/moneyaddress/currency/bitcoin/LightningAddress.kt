package xyz.block.moneyaddress.currency.bitcoin

import xyz.block.moneyaddress.MoneyAddress
import xyz.block.moneyaddress.currency.BTC
import xyz.block.moneyaddress.protocol.LNADDR

data class LightningAddress(
  val address: String,
  val moneyAddress: MoneyAddress, // TODO - do we even need this? should this be a MoneyAddress?
) {
  override fun toString(): String =
    "LightningAddress(btc, addr, $address, ${moneyAddress.id})"
}

fun MoneyAddress.isLightningAddress(): Boolean =
  this.currency == BTC.scheme && this.protocol == LNADDR.scheme

fun MoneyAddress.asLightningAddress(): LightningAddress? =
  if ( this.isLightningAddress() ) this.asLightningAddressOrThrow() else null

fun List<MoneyAddress>.findLightningAddresses(): List<LightningAddress> =
  mapNotNull { it.asLightningAddress() }

fun MoneyAddress.asLightningAddressOrThrow(): LightningAddress {
  if (isLightningAddress()) return LightningAddress(this.pss, this)
  else throw NotALightningAddressException(this)
}

fun List<MoneyAddress>.asLightningAddressesOrThrow(): List<LightningAddress> =
  this.map { it.asLightningAddressOrThrow() }

class NotALightningAddressException(moneyAddress: MoneyAddress) :
  Throwable("Not a bitcoin lightning address [$moneyAddress]")
