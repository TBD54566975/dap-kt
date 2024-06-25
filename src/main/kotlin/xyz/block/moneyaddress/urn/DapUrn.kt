package xyz.block.moneyaddress.urn

import java.util.regex.Pattern

data class DapUrn(
  val currency: String,
  val protocol: String,
  val pss: String
) {
  private val urn: String = "${Urn.PREFIX}$currency:$protocol:$pss"

  override fun toString(): String {
    return urn
  }

  companion object {
    private const val SEPARATOR = ":"

    private val DAP_URN_CSS_PATTERN =
      Pattern.compile("""^([^$SEPARATOR]+)$SEPARATOR(.+)$""")

    fun parse(value: String): DapUrn {
      val urn = Urn.parse(value)

      val matcher = DAP_URN_CSS_PATTERN.matcher(urn.nss)
      matcher.find()
      if (!matcher.matches()) {
        throw InvalidDapUrnException
      }
      val protocol = matcher.group(1)
      val pss = matcher.group(2)

      return DapUrn(currency = urn.nid, protocol = protocol, pss = pss)
    }
  }
}

object InvalidDapUrnException : Throwable("Invalid DAP URN")
