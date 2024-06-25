package xyz.block.moneyaddress

import web5.sdk.dids.didcore.Service
import xyz.block.moneyaddress.MoneyAddress.Companion.KIND
import xyz.block.moneyaddress.urn.DapUrn

data class MoneyAddress(
  val id: String,
  val urn: DapUrn,
  val currency: String,
  val protocol: String,
  val pss: String
) {
  companion object {
    const val KIND: String = "MoneyAddress"
  }
}

fun Service.toMoneyAddresses(): List<MoneyAddress> {
  if (type != KIND) {
    throw InvalidMoneyAddressException
  }

  return serviceEndpoint.map { endpoint ->
    val urn = DapUrn.parse(endpoint)
    MoneyAddress(
      id = id,
      urn = urn,
      currency = urn.currency,
      protocol = urn.protocol,
      pss = urn.pss
    )
  }
}

object InvalidMoneyAddressException : Throwable("Invalid MoneyAddress")
