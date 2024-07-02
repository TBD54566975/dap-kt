package xyz.block.moneyaddress.currency.usdc

import xyz.block.moneyaddress.MoneyAddress
import xyz.block.moneyaddress.currency.USDC
import xyz.block.moneyaddress.protocol.ETH

data class EthAddress(
  val address: String,
  val moneyAddress: MoneyAddress,
) {
  override fun toString(): String =
    "EthAddress(usdc, eth, $address, ${moneyAddress.id})"
}

fun MoneyAddress.isEthAddress(): Boolean =
  this.currency == USDC.scheme && this.protocol == ETH.scheme

fun MoneyAddress.asEthAddressOrNull(): EthAddress? =
  if (this.isEthAddress()) this.asEthAddressOrThrow() else null

fun List<MoneyAddress>.findEthAddresses(): List<EthAddress> =
  mapNotNull { it.asEthAddressOrNull() }

fun MoneyAddress.asEthAddressOrThrow(): EthAddress {
  if (isEthAddress()) return EthAddress(this.pss, this)
  else throw NotAnEthAddressException(this)
}

fun List<MoneyAddress>.asEthAddressesOrThrow(): List<EthAddress> =
  this.map { it.asEthAddressOrThrow() }

class NotAnEthAddressException(moneyAddress: MoneyAddress) :
  Throwable("Not a usdc eth address [$moneyAddress]")
