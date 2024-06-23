package xyz.block.maddr.urn

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

    fun parse(input: String): Urn {
      if (!input.startsWith(PREFIX)) {
        throw InvalidUrnException("incorrect prefix", input)
      }
      val urnBody = input.substringAfter(PREFIX)
      if (urnBody.isBlank()) {
        throw InvalidUrnException("missing body", input)
      }
      val nid = urnBody.substringBefore(":")
      val nss = urnBody.substringAfter(":")

      if (nid.isBlank()) {
        throw InvalidUrnException("missing nid", input)
      }
      if (nss.isBlank() || nss == urnBody) {
        throw InvalidUrnException("missing nss", input)
      }
      return Urn(nid, nss)
    }
  }
}

data class InvalidUrnException(val reason: String, val invalidValue: String) :
  Throwable("Invalid URN: $reason: $invalidValue")
