package xyz.block.moneyaddress.urn

import java.util.regex.Pattern

/**
 * A Uniform Resource Name as defined by the IETF in [RFC-8141](https://datatracker.ietf.org/doc/html/rfc8141).
 *
 * A URN has the format `urn:<nid>:<nss>`.
 * - nid is a "Namespace Identifier"
 * - nss is a "Namespace Specific String"
 */
data class Urn(
  val nid: String,
  val nss: String
) {
  private val urn: String = "$PREFIX$nid:$nss"

  override fun toString(): String {
    return urn
  }

  companion object {
    const val PREFIX = "urn:"
    private const val SEPARATOR = ":"

    private val URN_PATTERN = Pattern.compile("""^${PREFIX}([^$SEPARATOR]+)$SEPARATOR(.+)$""")

    /**
     * Parse a string into a URN.
     *
     * @throws InvalidUrnException if the URN is not a valid URN.
     */
    fun parse(urn: String): Urn {
      val matcher = URN_PATTERN.matcher(urn)
      matcher.find()
      if (!matcher.matches()) {
        throw InvalidUrnException
      }
      val nid = matcher.group(1)
      val nss = matcher.group(2)

      return Urn(nid, nss)
    }
  }
}

object InvalidUrnException : Throwable("Invalid URN")
