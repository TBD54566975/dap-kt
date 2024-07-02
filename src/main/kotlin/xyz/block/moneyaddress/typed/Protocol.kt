package xyz.block.moneyaddress.typed

open class Protocol(open val scheme: String) {
  override fun toString(): String = scheme

  companion object {
    fun String.asProtocol(): Protocol =
      when (this) {
        ONCHAIN_ADDRESS.scheme -> ONCHAIN_ADDRESS
        ETHEREUM.scheme -> ETHEREUM
        LIGHTNING_ADDRESS.scheme -> LIGHTNING_ADDRESS
        MOBILE_MONEY.scheme -> MOBILE_MONEY
        SILENT_PAYMENT_ADDRESS.scheme -> SILENT_PAYMENT_ADDRESS
        STELLAR.scheme -> STELLAR
        else -> UNRECOGNIZED_PROTOCOL(this)
      }
  }
}

data object ONCHAIN_ADDRESS : Protocol("addr")
data object ETHEREUM : Protocol("eth")
data object LIGHTNING_ADDRESS : Protocol("lnaddr")
data object MOBILE_MONEY : Protocol("momo")
data object SILENT_PAYMENT_ADDRESS : Protocol("spaddr")
data object STELLAR : Protocol("xlm")
data class UNRECOGNIZED_PROTOCOL(override val scheme: String) : Protocol(scheme)
