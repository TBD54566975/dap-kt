package xyz.block.moneyaddress

import io.github.oshai.kotlinlogging.KotlinLogging
import xyz.block.moneyaddress.Currency.Companion.asCurrency
import xyz.block.moneyaddress.Protocol.Companion.asProtocol
import xyz.block.moneyaddress.typed.BtcLightningAddress
import xyz.block.moneyaddress.typed.BtcLightningOffer
import xyz.block.moneyaddress.typed.BtcOnChainAddress
import xyz.block.moneyaddress.typed.MobileMoneyAddress
import xyz.block.moneyaddress.MoneyAddress as UntypedMoneyAddress

/**
 * A factory function that converts an [UntypedMoneyAddress] to a [TypedMoneyAddress].
 * These are registered with the [TypedMoneyAddressRegistry] in order to allow automatic conversion to the typed classes.
 */
typealias TypedMoneyAddressFactory<T> = (UntypedMoneyAddress) -> TypedMoneyAddress<T>

/**
 * Provides type conversion from [MoneyAddress] to [TypedMoneyAddress].
 * This is based on registered mappings from [Currency] and [Protocol] to [TypedMoneyAddressFactory] functions.
 *
 * There is a `defaultTypedMoneyAddressRegistry` that is pre-populated with the default mappings.
 * New mappings can be added using the `register` method, for either a `Currency` and `Protocol` pair or just a `Protocol`.
 * Mappings for the `Currency` and `Protocol` pair take precendence over mappings for just the `Protocol`.
 */
class TypedMoneyAddressRegistry {
  fun <T> register(
    currency: Currency,
    protocol: Protocol,
    f: TypedMoneyAddressFactory<T>
  ): TypedMoneyAddressRegistry {
    if (cpMappings.put(Pair(currency, protocol), f) != null) {
      logger.warn {
        "Overwriting existing MoneyAddressRegistry entry [currency=$currency][protocol=$protocol]"
      }
    }
    return this
  }

  fun <T> register(
    protocol: Protocol,
    f: TypedMoneyAddressFactory<T>
  ): TypedMoneyAddressRegistry {
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

  private val cpMappings = mutableMapOf<Pair<Currency, Protocol>, TypedMoneyAddressFactory<*>>()
  private val pMappings = mutableMapOf<Protocol, TypedMoneyAddressFactory<*>>()

  fun toTypedMoneyAddress(ma: UntypedMoneyAddress): TypedMoneyAddress<*> =
    cpMappings[Pair(ma.currency.asCurrency(), ma.protocol.asProtocol())]?.invoke(ma)
      ?: pMappings[ma.protocol.asProtocol()]?.invoke(ma)
      ?: UnrecognizedMoneyAddress(ma.pss, ma.currency, ma.protocol, ma.id)

  private val logger = KotlinLogging.logger {}

  companion object {
    val defaultTypedMoneyAddressRegistry: TypedMoneyAddressRegistry = TypedMoneyAddressRegistry()

    init {
      BtcLightningAddress.register(defaultTypedMoneyAddressRegistry)
      BtcLightningOffer.register(defaultTypedMoneyAddressRegistry)
      BtcOnChainAddress.register(defaultTypedMoneyAddressRegistry)
      MobileMoneyAddress.register(defaultTypedMoneyAddressRegistry)
    }

    fun UntypedMoneyAddress.toTypedMoneyAddress(typedMoneyAddressRegistry: TypedMoneyAddressRegistry = defaultTypedMoneyAddressRegistry): TypedMoneyAddress<*> =
      typedMoneyAddressRegistry.toTypedMoneyAddress(this)
  }
}
