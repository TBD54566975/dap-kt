package xyz.block.moneyaddress.typed

import io.github.oshai.kotlinlogging.KotlinLogging
import xyz.block.moneyaddress.typed.Currency.Companion.asCurrency
import xyz.block.moneyaddress.typed.Protocol.Companion.asProtocol
import xyz.block.moneyaddress.MoneyAddress as UntypedMoneyAddress

open class MoneyAddress<T>(
  open val address: T,
  open val currency: Currency,
  open val protocol: Protocol,
  open val id: String,
) {
  override fun toString(): String =
    "MoneyAddress(${currency.scheme}, ${protocol.scheme}, $address, $id)"
}

class UnrecognizedMoneyAddress(pss: String, currency: Currency, protocol: Protocol, id: String) :
  MoneyAddress<String>(pss, currency, protocol, id) {

  constructor(pss: String, currency: String, protocol: String, id: String) :
    this(pss, currency.asCurrency(), protocol.asProtocol(), id)
}

typealias MoneyAddressFactory<T> = (UntypedMoneyAddress) -> MoneyAddress<T>

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

  @Suppress("IMPLICIT_NOTHING_TYPE_ARGUMENT_AGAINST_NOT_NOTHING_EXPECTED_TYPE")
  fun UntypedMoneyAddress.toTypedMoneyAddress(): MoneyAddress<*> =
    cpMappings[Pair(currency.asCurrency(), protocol.asProtocol())]?.let { f -> return f(this) }
      ?: pMappings[protocol.asProtocol()]?.let { f -> return f(this) }
      ?: UnrecognizedMoneyAddress(this.pss, this.currency, this.protocol, this.id)

  private val logger = KotlinLogging.logger {}
}
