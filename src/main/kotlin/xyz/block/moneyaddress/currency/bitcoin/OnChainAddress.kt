package xyz.block.moneyaddress.currency.bitcoin

import xyz.block.moneyaddress.MoneyAddress
import xyz.block.moneyaddress.currency.Currency
import xyz.block.moneyaddress.protocol.Protocol

data class OnChainAddress(
  val address: String,
  val moneyAddress: MoneyAddress,
) {
  override fun toString(): String =
    "OnChainAddress(btc, addr, $address, ${moneyAddress.id})"
}

fun MoneyAddress.isOnChainAddress(): Boolean =
  this.currency == Currency.BTC.scheme && this.protocol == Protocol.ADDR.scheme

fun MoneyAddress.asOnChainAddress(): OnChainAddress {
  if (isOnChainAddress()) return OnChainAddress(this.pss, this)
  else throw NotAnOnChainAddressException(this)
}

fun MoneyAddress.asOnChainAddressOrNull(): OnChainAddress? =
  if ( this.isOnChainAddress() ) this.asOnChainAddress() else null

fun List<MoneyAddress>.asOnChainAddressesOrThrow(): List<OnChainAddress> =
  this.map { it.asOnChainAddress() }

fun List<MoneyAddress>.findOnChainAddresses(): List<OnChainAddress> =
  mapNotNull { it.asOnChainAddressOrNull() }

class NotAnOnChainAddressException(moneyAddress: MoneyAddress) :
  Throwable("Not a bitcoin on-chain address [$moneyAddress]")
