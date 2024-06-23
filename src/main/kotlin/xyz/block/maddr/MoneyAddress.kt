package xyz.block.maddr

import xyz.block.maddr.MoneyAddress.Companion.KIND
import xyz.block.maddr.urn.Urn

// TODO(aparker) - placeholder, this should come from web5-kt
data class DidService(val id: String, val type: String, val serviceEndpoints: List<String>)

data class MoneyAddress(
  val id: String,
  val urn: Urn,
  val currency: String,
  val css: String
) {
  companion object {
    const val KIND: String = "maddr"
  }
}

fun DidService.toMoneyAddresses(): List<MoneyAddress> {
  if (type != KIND) {
    throw InvalidMoneyAddressException
  }

  return serviceEndpoints.map { endpoint ->
    val urn = Urn.parse(endpoint)
    MoneyAddress(
      id = id,
      urn = urn,
      currency = urn.nid,
      css = urn.nss
    )
  }
}

object InvalidMoneyAddressException : Throwable("Invalid MoneyAddress")
