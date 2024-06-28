package xyz.block.moneyaddress.currency.usdc

import xyz.block.moneyaddress.MoneyAddress
import xyz.block.moneyaddress.currency.Currency
import xyz.block.moneyaddress.protocol.Protocol

data class EthAddress(
  val address: String,
  val moneyAddress: MoneyAddress,
) {
  override fun toString(): String =
    "EthAddress(usdc, eth, $address, ${moneyAddress.id})"
}

fun MoneyAddress.isEthAddress(): Boolean =
  this.currency == Currency.USDC.scheme && this.protocol == Protocol.ETH.scheme

fun MoneyAddress.asEthAddress(): EthAddress {
  if (isEthAddress()) return EthAddress(this.pss, this)
  else throw NotAnEthAddressException(this)
}

fun List<MoneyAddress>.asEthAddresses(): List<EthAddress> =
  this.map { it.asEthAddress() }

fun MoneyAddress.asEthAddressOrNull(): EthAddress? =
  if (this.isEthAddress()) this.asEthAddress() else null

fun List<MoneyAddress>.findEthAddresses(): List<EthAddress> =
  mapNotNull { it.asEthAddressOrNull() }

class NotAnEthAddressException(moneyAddress: MoneyAddress) :
  Throwable("Not a usdc eth address [$moneyAddress]")
