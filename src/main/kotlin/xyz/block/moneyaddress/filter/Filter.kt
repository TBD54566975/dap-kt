package xyz.block.moneyaddress.filter

import xyz.block.moneyaddress.MoneyAddress
import xyz.block.moneyaddress.currency.Currency
import xyz.block.moneyaddress.protocol.Protocol

fun MoneyAddress.hasCurrency(currency: String): Boolean =
  this.currency == currency

fun MoneyAddress.hasCurrency(currency: Currency): Boolean =
  this.currency == currency.scheme

fun MoneyAddress.hasProtocol(protocol: String): Boolean =
  this.protocol == protocol

fun MoneyAddress.hasProtocol(protocol: Protocol): Boolean =
   this.protocol == protocol.scheme
