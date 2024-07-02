package xyz.block.moneyaddress.typed

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
