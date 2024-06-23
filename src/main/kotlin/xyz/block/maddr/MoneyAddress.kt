package xyz.block.maddr

import xyz.block.maddr.MoneyAddress.Companion.KIND
import xyz.block.maddr.urn.Urn
import web5.sdk.dids.didcore.Service

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

fun Service.toMoneyAddresses(): List<MoneyAddress> {
  if (type != KIND) {
    throw InvalidMoneyAddressException
  }

  return serviceEndpoint.map { endpoint ->
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
