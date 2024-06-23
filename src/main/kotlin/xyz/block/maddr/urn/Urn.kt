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
        throw InvalidUrnException
      }
      val urnBody = input.substringAfter(PREFIX)
      if (urnBody.isBlank()) {
        throw InvalidUrnException
      }
      val nid = urnBody.substringBefore(":")
      val nss = urnBody.substringAfter(":")

      if (nid.isBlank()) {
        throw InvalidUrnException
      }
      if (nss.isBlank() || nss == urnBody) {
        throw InvalidUrnException
      }
      return Urn(nid, nss)
    }
  }
}

object InvalidUrnException : Throwable("Invalid URN")
