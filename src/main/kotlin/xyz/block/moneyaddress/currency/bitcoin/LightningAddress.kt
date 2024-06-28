package xyz.block.moneyaddress.currency.bitcoin

import xyz.block.moneyaddress.MoneyAddress
import xyz.block.moneyaddress.currency.Currency
import xyz.block.moneyaddress.protocol.Protocol

data class LightningAddress(
  val address: String,
  val moneyAddress: MoneyAddress,
) {
  override fun toString(): String =
    "LightningAddress(btc, addr, $address, ${moneyAddress.id})"
}

fun MoneyAddress.isLightningAddress(): Boolean =
  this.currency == Currency.BTC.scheme && this.protocol == Protocol.LNADDR.scheme

fun MoneyAddress.asLightningAddress(): LightningAddress {
  if (isLightningAddress()) return LightningAddress(this.pss, this)
  else throw NotALightningAddressException(this)
}

fun List<MoneyAddress>.asLightningAddresses(): List<LightningAddress> =
  this.map { it.asLightningAddress() }

fun MoneyAddress.asLightningAddressOrNull(): LightningAddress? =
  if (this.isLightningAddress()) this.asLightningAddress() else null

fun List<MoneyAddress>.findLightningAddresses(): List<LightningAddress> =
  mapNotNull { it.asLightningAddressOrNull() }

class NotALightningAddressException(moneyAddress: MoneyAddress) :
  Throwable("Not a bitcoin lightning address [$moneyAddress]")
