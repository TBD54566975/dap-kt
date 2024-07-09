package xyz.block.moneyaddress

import xyz.block.moneyaddress.urn.DapUrn
import java.util.UUID

class MoneyAddressExamples {

  companion object {
    val btcLightningAddressMoneyAddress1 = makeMoneyAddress("btc", "lnaddr")
    val btcLightningAddressMoneyAddress2 = makeMoneyAddress("btc", "lnaddr")
    val btcLightningOfferMoneyAddress1 = makeMoneyAddress("btc", "lno")
    val btcLightningOfferMoneyAddress2 = makeMoneyAddress("btc", "lno")
    val btcOnChainMoneyAddress1 = makeMoneyAddress("btc", "addr")
    val btcOnChainMoneyAddress2 = makeMoneyAddress("btc", "addr")
    val kesMomoMoneyAddress1 = makeMoneyAddress("kes", "momo", "mpesa:11111")
    val kesMomoMoneyAddress2 = makeMoneyAddress("kes", "momo", "mpesa:22222")
    val usdcEthMoneyAddress1 = makeMoneyAddress("usdc", "eth")
    val usdcEthMoneyAddress2 = makeMoneyAddress("usdc", "eth")
    val zarMomoMoneyAddress1 = makeMoneyAddress("zar", "momo", "mpesa:33333")
    val zarMomoMoneyAddress2 = makeMoneyAddress("zar", "momo", "mpesa:44444")
    val zzzUnrecognizedMoneyAddress1 = makeMoneyAddress("zzz", "zzz", "zzz1")
    val zzzUnrecognizedMoneyAddress2 = makeMoneyAddress("zzz", "zzz", "zzz2")

    val manyMoneyAddresses = listOf(
      btcLightningAddressMoneyAddress1,
      btcLightningAddressMoneyAddress2,
      btcLightningOfferMoneyAddress1,
      btcLightningOfferMoneyAddress2,
      btcOnChainMoneyAddress1,
      btcOnChainMoneyAddress2,
      kesMomoMoneyAddress1,
      kesMomoMoneyAddress2,
      usdcEthMoneyAddress1,
      usdcEthMoneyAddress2,
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
