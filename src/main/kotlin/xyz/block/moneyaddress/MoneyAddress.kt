package xyz.block.moneyaddress

import web5.sdk.dids.didcore.Service
import xyz.block.moneyaddress.urn.DapUrn

data class MoneyAddress(
  val id: String,
  val urn: DapUrn,
  val currency: String,
  val protocol: String,
  val pss: String
) {

  override fun toString(): String =
    "MoneyAddress($currency, $protocol, $pss, $id)"

  companion object {
    const val KIND: String = "MoneyAddress"
  }
}

fun Service.toMoneyAddresses(): List<MoneyAddress> {
  if (type != MoneyAddress.KIND) {
    throw InvalidMoneyAddressException
  }

  return serviceEndpoint.map { endpoint ->
    DapUrn.parse(endpoint).toMoneyAddress(id)
  }
}

fun DapUrn.toMoneyAddress(id: String): MoneyAddress =
  MoneyAddress(
    id = id,
    urn = this,
    currency = currency,
    protocol = protocol,
    pss = pss
  )

object InvalidMoneyAddressException : Throwable("Invalid MoneyAddress")
