package xyz.block.moneyaddress.protocol

open class Protocol(open val scheme: String) {
  override fun toString(): String = scheme

  companion object {
    fun String.asProtocol(): Protocol =
      when (this) {
        ADDR.scheme -> ADDR
        ETH.scheme -> ETH
        LNADDR.scheme -> LNADDR
        MOMO.scheme -> MOMO
        SPADDR.scheme -> SPADDR
        XLM.scheme -> XLM
        else -> UNRECOGNIZED_PROTOCOL(this)
      }
  }
}

data object ADDR : Protocol("addr")
data object ETH : Protocol("eth")
data object LNADDR : Protocol("lnaddr")
data object MOMO : Protocol("momo")
data object SPADDR : Protocol("spaddr")
data object XLM : Protocol("xlm")
data class UNRECOGNIZED_PROTOCOL(override val scheme: String) : Protocol(scheme)
