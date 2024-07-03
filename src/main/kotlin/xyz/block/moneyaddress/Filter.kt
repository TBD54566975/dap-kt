package xyz.block.moneyaddress

/**
 * Utility extension functions to be used for filtering [MoneyAddress] or [TypedMoneyAddress] objects.
 */

/**
 * Returns true if the [MoneyAddress] has the given currency.
 */
fun MoneyAddress.hasCurrency(currency: Currency): Boolean =
  hasCurrency(currency.scheme)

/**
 * Returns true if the [MoneyAddress] has the given currency.
 */
fun MoneyAddress.hasCurrency(currency: String): Boolean =
  this.currency == currency

/**
 * Returns true if the [MoneyAddress] has the given protocol.
 */
fun MoneyAddress.hasProtocol(protocol: Protocol): Boolean =
  hasProtocol(protocol.scheme)

/**
 * Returns true if the [MoneyAddress] has the given protocol.
 */
fun MoneyAddress.hasProtocol(protocol: String): Boolean =
  this.protocol == protocol

/**
 * Returns true if the [MoneyAddress] has the given currency and protocol.
 */
fun MoneyAddress.matches(currency: Currency, protocol: Protocol): Boolean =
  hasProtocol(protocol) && hasCurrency(currency)

/**
 * Returns true if the [MoneyAddress] has the given currency and protocol.
 */
fun MoneyAddress.matches(currency: String, protocol: String): Boolean =
    hasCurrency(currency) && hasProtocol(protocol)
