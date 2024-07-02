package xyz.block.moneyaddress.currency.bitcoin

import xyz.block.moneyaddress.MoneyAddress
import xyz.block.moneyaddress.currency.BTC
import xyz.block.moneyaddress.protocol.ADDR

data class OnChainAddress(
  val address: String,
  val moneyAddress: MoneyAddress,
) {
  override fun toString(): String =
    "OnChainAddress(btc, addr, $address, ${moneyAddress.id})"
}

fun MoneyAddress.isOnChainAddress(): Boolean =
  this.currency == BTC.scheme && this.protocol == ADDR.scheme

fun MoneyAddress.asOnChainAddress(): OnChainAddress? =
  if (this.isOnChainAddress()) this.asOnChainAddressOrThrow() else null

fun List<MoneyAddress>.findOnChainAddresses(): List<OnChainAddress> =
  mapNotNull { it.asOnChainAddress() }

fun MoneyAddress.asOnChainAddressOrThrow(): OnChainAddress {
  if (isOnChainAddress()) return OnChainAddress(this.pss, this)
  else throw NotAnOnChainAddressException(this)
}

fun List<MoneyAddress>.asOnChainAddressesOrThrow(): List<OnChainAddress> =
  this.map { it.asOnChainAddressOrThrow() }

class NotAnOnChainAddressException(moneyAddress: MoneyAddress) :
  Throwable("Not a bitcoin on-chain address [$moneyAddress]")
