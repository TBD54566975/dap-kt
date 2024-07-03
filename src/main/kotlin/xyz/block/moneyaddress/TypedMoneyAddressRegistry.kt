package xyz.block.moneyaddress

import io.github.oshai.kotlinlogging.KotlinLogging
import xyz.block.moneyaddress.Currency.Companion.asCurrency
import xyz.block.moneyaddress.Protocol.Companion.asProtocol
import xyz.block.moneyaddress.typed.BtcLightningAddress
import xyz.block.moneyaddress.typed.BtcOnChainAddress
import xyz.block.moneyaddress.typed.MobileMoneyAddress
import xyz.block.moneyaddress.MoneyAddress as UntypedMoneyAddress

/**
 * A factory function that converts an [UntypedMoneyAddress] to a [TypedMoneyAddress].
 * These are registered with the [MoneyAddressRegistry] in order to allow automatic conversion to the typed classes.
 */
typealias MoneyAddressFactory<T> = (UntypedMoneyAddress) -> TypedMoneyAddress<T>

class MoneyAddressRegistry {
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

  fun toTypedMoneyAddress(ma: UntypedMoneyAddress): TypedMoneyAddress<*> =
    cpMappings[Pair(ma.currency.asCurrency(), ma.protocol.asProtocol())]?.invoke(ma)
      ?: pMappings[ma.protocol.asProtocol()]?.invoke(ma)
      ?: UnrecognizedMoneyAddress(ma.pss, ma.currency, ma.protocol, ma.id)

  private val logger = KotlinLogging.logger {}

  companion object {
    val defaultMoneyAddressRegistry: MoneyAddressRegistry = MoneyAddressRegistry()

    init {
      BtcLightningAddress.register(defaultMoneyAddressRegistry)
      BtcOnChainAddress.register(defaultMoneyAddressRegistry)
      MobileMoneyAddress.register(defaultMoneyAddressRegistry)
    }

    fun UntypedMoneyAddress.toTypedMoneyAddress(moneyAddressRegistry: MoneyAddressRegistry = defaultMoneyAddressRegistry): TypedMoneyAddress<*> =
      moneyAddressRegistry.toTypedMoneyAddress(this)
  }
}
