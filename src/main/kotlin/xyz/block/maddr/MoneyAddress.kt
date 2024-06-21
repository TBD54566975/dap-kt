package xyz.block.maddr

import xyz.block.maddr.MoneyAddress.Companion.KIND
import xyz.block.maddr.urn.URN

// TODO(aparker) - placeholder, this should come from web5-kt
data class DidService(val id: String, val type: String, val serviceEndpoints: List<String>)

data class MoneyAddress(
  val id: String,
  val urn: URN,
  val currency: String,
  val css: String
) {
  companion object {
    const val KIND: String = "maddr"
  }
}

fun DidService.toMoneyAddresses(): List<MoneyAddress> {
  if (type != KIND) {
    throw InvalidMoneyAddressException("invalid service type: $type")
  }

  return serviceEndpoints.map { endpoint ->
    val urn = URN.parse(endpoint)
    MoneyAddress(
      id = id,
      urn = urn,
      currency = urn.nid,
      css = urn.nss
    )
  }
}

data class InvalidMoneyAddressException(val invalidValue: String) :
  Throwable("Invalid MoneyAddress: $invalidValue")
