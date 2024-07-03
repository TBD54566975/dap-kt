package xyz.block.moneyaddress.urn

import java.util.regex.Pattern

/**
 * A URN as used in the Decentralized Agnostic Paytag (DAP).
 *
 * A standard URN has the format `urn:<nid>:<nss>`.
 * A DAP URN has the format `urn:<currency>:<protocol>:<pss>`.
 *
 * That is
 * - the `nid` is the `currency`
 * - the `nss` is split into the `protocol` and `pss` (protocol specific string).
 */
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

    /** parse a string into a DapUrn
     *
     * @throws InvalidUrnException if the URN is not a valid URN.
     * @throws InvalidDapUrnException if the URN is not a valid DAP URN.
     */
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
