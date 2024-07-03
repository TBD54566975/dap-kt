package xyz.block.moneyaddress

/**
 * Represents the currency of a MoneyAddress.
 * The `scheme` is the string representation of the currency as used in the DID Documents.
 *
 * Currencies mentioned in the DAP specification have concrete implementations.
 * This allows type-safe matching of MoneyAddress objects without matching on strings.
 * See [TypedMoneyAddress] and [Filter.kt]
 *
 * Currencies not included in the DAP specification are represented by [UNRECOGNIZED_CURRENCY].
 */
open class Currency(open val scheme: String) {
  override fun toString(): String = scheme

  companion object {
    fun String.asCurrency(): Currency =
      when (this) {
        BTC.scheme -> BTC
        KES.scheme -> KES
        USDC.scheme -> USDC
        ZAR.scheme -> ZAR
        else -> UNRECOGNIZED_CURRENCY(this)
      }
  }
}

data object BTC : Currency("btc")
data object KES : Currency("kes")
data object USDC : Currency("usdc")
data object ZAR : Currency("zar")

data class UNRECOGNIZED_CURRENCY(override val scheme: String) : Currency(scheme)
