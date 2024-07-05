package xyz.block.moneyaddress

import xyz.block.moneyaddress.urn.DapUrn
import java.util.UUID

class MoneyAddressExamples {

  companion object {
    val btcOnChainMoneyAddress1 = makeMoneyAddress("btc", "addr")
    val btcOnChainMoneyAddress2 = makeMoneyAddress("btc", "addr")
    val btcLightningMoneyAddress1 = makeMoneyAddress("btc", "lnaddr")
    val btcLightningMoneyAddress2 = makeMoneyAddress("btc", "lnaddr")
    val usdcEthMoneyAddress1 = makeMoneyAddress("usdc", "eth")
    val usdcEthMoneyAddress2 = makeMoneyAddress("usdc", "eth")
    val kesMomoMoneyAddress1 = makeMoneyAddress("kes", "momo", "mpesa:11111")
    val kesMomoMoneyAddress2 = makeMoneyAddress("kes", "momo", "mpesa:22222")
    val zarMomoMoneyAddress1 = makeMoneyAddress("zar", "momo", "mpesa:33333")
    val zarMomoMoneyAddress2 = makeMoneyAddress("zar", "momo", "mpesa:44444")
    val zzzUnrecognizedMoneyAddress1 = makeMoneyAddress("zzz", "zzz", "zzz1")
    val zzzUnrecognizedMoneyAddress2 = makeMoneyAddress("zzz", "zzz", "zzz2")

    val manyMoneyAddresses = listOf(
      btcOnChainMoneyAddress1,
      btcOnChainMoneyAddress2,
      btcLightningMoneyAddress1,
      btcLightningMoneyAddress2,
      usdcEthMoneyAddress1,
      usdcEthMoneyAddress2,
      kesMomoMoneyAddress1,
      kesMomoMoneyAddress2,
      zarMomoMoneyAddress1,
      zarMomoMoneyAddress2,
      zzzUnrecognizedMoneyAddress1,
      zzzUnrecognizedMoneyAddress2
    )

    fun makeMoneyAddress(currency: String, protocol: String, pss: String? = null) =
      DapUrn(currency, protocol, pss ?: "fake-$currency-$protocol-address")
        .toMoneyAddress(UUID.randomUUID().toString())
  }
}
