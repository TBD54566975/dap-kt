package xyz.block.moneyaddress

import xyz.block.moneyaddress.Currency.Companion.asCurrency
import xyz.block.moneyaddress.Protocol.Companion.asProtocol

/**
 * A type-safe representation of a MoneyAddress provided as a layer on top of [MoneyAddress]
 *
 * The `currency` and `protocol` are typed, allowing filtering and matching without string comparison.
 * See [Filter.kt] for utility functions for this matching.
 *
 * See [TypedMoneyAddressRegistry] for semi-automatic conversion of [MoneyAddress] to [TypedMoneyAddress].
 */
open class TypedMoneyAddress<T>(
  open val address: T,
  open val currency: Currency,
  open val protocol: Protocol,
  open val id: String,
) {
  override fun toString(): String =
    "MoneyAddress(${currency.scheme}, ${protocol.scheme}, $address, $id)"
}

/**
 * Represents a MoneyAddress that is not recognized by the [TypedMoneyAddressRegistry].
 */
data class UnrecognizedMoneyAddress(
  val pss: String,
  override val currency: Currency,
  override val protocol: Protocol,
  override val id: String
) : TypedMoneyAddress<String>(pss, currency, protocol, id) {

  constructor(pss: String, currency: String, protocol: String, id: String) :
    this(pss, currency.asCurrency(), protocol.asProtocol(), id)
}
