package xyz.block.moneyaddress

import io.github.oshai.kotlinlogging.KotlinLogging
import xyz.block.moneyaddress.Currency.Companion.asCurrency
import xyz.block.moneyaddress.Protocol.Companion.asProtocol
import xyz.block.moneyaddress.MoneyAddress as UntypedMoneyAddress

/**
 * A type-safe representation of a MoneyAddress provided as a layer on top of [MoneyAddress]
 *
 * The `currency` and `protocol` are typed, allowing filtering and matching without string comparison.
 * See [Filter.kt] for utility functions for this matching.
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
 * Represents a MoneyAddress that is not recognized by the [MoneyAddressRegistry].
 */
class UnrecognizedMoneyAddress(pss: String, currency: Currency, protocol: Protocol, id: String) :
  TypedMoneyAddress<String>(pss, currency, protocol, id) {

  constructor(pss: String, currency: String, protocol: String, id: String) :
    this(pss, currency.asCurrency(), protocol.asProtocol(), id)
}

/**
 * A factory function that converts an [UntypedMoneyAddress] to a [TypedMoneyAddress].
 * These are registered with the [MoneyAddressRegistry] in order to allow automatic conversion to the typed classes.
 */
typealias MoneyAddressFactory<T> = (UntypedMoneyAddress) -> TypedMoneyAddress<T>

object MoneyAddressRegistry {
  fun <T> register(
    currency: Currency,
    protocol: Protocol,
    f: MoneyAddressFactory<T>
  ): MoneyAddressRegistry {
    if (cpMappings.put(Pair(currency, protocol), f) != null) {
      logger.warn {
        "Overwriting existing MoneyAddressRegistry entry [currency=$currency][protocol=$protocol]"
      }
    }
    return this
  }

  fun <T> register(
    protocol: Protocol,
    f: MoneyAddressFactory<T>
  ): MoneyAddressRegistry {
    if (pMappings.put(protocol, f) != null) {
      logger.warn {
        "Overwriting existing MoneyAddressRegistry entry [protocol=$protocol]"
      }
    }
    return this
  }

  fun clear() {
    cpMappings.clear()
    pMappings.clear()
  }

  private val cpMappings = mutableMapOf<Pair<Currency, Protocol>, MoneyAddressFactory<*>>()
  private val pMappings = mutableMapOf<Protocol, MoneyAddressFactory<*>>()

  fun UntypedMoneyAddress.toTypedMoneyAddress(): TypedMoneyAddress<*> =
    cpMappings[Pair(currency.asCurrency(), protocol.asProtocol())]?.invoke(this)
      ?: pMappings[protocol.asProtocol()]?.invoke(this)
      ?: UnrecognizedMoneyAddress(this.pss, this.currency, this.protocol, this.id)

  private val logger = KotlinLogging.logger {}
}
