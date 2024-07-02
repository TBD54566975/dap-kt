package xyz.block.moneyaddress.typed

import xyz.block.moneyaddress.MoneyAddress

fun MoneyAddress.hasCurrency(currency: Currency): Boolean =
  hasCurrency(currency.scheme)

fun MoneyAddress.hasCurrency(currency: String): Boolean =
  this.currency == currency

fun MoneyAddress.hasProtocol(protocol: Protocol): Boolean =
  hasProtocol(protocol.scheme)

fun MoneyAddress.hasProtocol(protocol: String): Boolean =
  this.protocol == protocol

fun MoneyAddress.matches(currency: Currency, protocol: Protocol): Boolean =
  hasProtocol(protocol) && hasCurrency(currency)

fun MoneyAddress.matches(currency: String, protocol: String): Boolean =
    hasCurrency(currency) && hasProtocol(protocol)
