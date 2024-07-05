package xyz.block.moneyaddress

/**
 * Represents the protocol of a MoneyAddress.
 * These protocols are often, but not always, specific to a currency.
 *
 * @property scheme the string representation of the protocol as used in the DID Documents.
 *
 * Protocols mentioned in the DAP specification have concrete implementations.
 * This allows type-safe matching of MoneyAddress objects without matching on strings.
 * See [TypedMoneyAddress] and [Filter.kt]
 *
 * Protocols not included in the DAP specification are represented by [UNRECOGNIZED_PROTOCOL].
 */
open class Protocol(open val scheme: String) {
  override fun toString(): String = scheme

  companion object {
    fun String.asProtocol(): Protocol =
      when (this) {
        ETHEREUM.scheme -> ETHEREUM
        LIGHTNING_ADDRESS.scheme -> LIGHTNING_ADDRESS
        LIGHTNING_OFFER.scheme -> LIGHTNING_OFFER
        MOBILE_MONEY.scheme -> MOBILE_MONEY
        ONCHAIN_ADDRESS.scheme -> ONCHAIN_ADDRESS
        SILENT_PAYMENT_ADDRESS.scheme -> SILENT_PAYMENT_ADDRESS
        STELLAR_XLM.scheme -> STELLAR_XLM
        else -> UNRECOGNIZED_PROTOCOL(this)
      }
  }
}

data object ETHEREUM : Protocol("eth")
data object LIGHTNING_ADDRESS : Protocol("lnaddr")
data object LIGHTNING_OFFER : Protocol("lno")
data object MOBILE_MONEY : Protocol("momo")
data object ONCHAIN_ADDRESS : Protocol("addr")
data object SILENT_PAYMENT_ADDRESS : Protocol("spaddr")
data object STELLAR_XLM : Protocol("xlm")

data class UNRECOGNIZED_PROTOCOL(override val scheme: String) : Protocol(scheme)
