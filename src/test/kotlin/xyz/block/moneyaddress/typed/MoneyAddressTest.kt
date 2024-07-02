package xyz.block.moneyaddress.typed

import xyz.block.moneyaddress.toMoneyAddress
import xyz.block.moneyaddress.typed.MoneyAddressRegistry.toTypedMoneyAddress
import xyz.block.moneyaddress.typed.x.BtcLightningAddress
import xyz.block.moneyaddress.typed.x.BtcOnChainAddress
import xyz.block.moneyaddress.typed.x.MPESA
import xyz.block.moneyaddress.typed.x.MobileMoneyAddress
import xyz.block.moneyaddress.typed.x.MomoAddress
import xyz.block.moneyaddress.urn.DapUrn
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class MoneyAddressTest {

  @Test
  fun toTypedAddress() {
    val btcLightningAddress: BtcLightningAddress =
      btcLightningMoneyAddress1.toTypedMoneyAddress() as BtcLightningAddress
    assertEquals(
      BtcLightningAddress(btcLightningMoneyAddress1.pss, btcLightningMoneyAddress1.id),
      btcLightningAddress
    )

    val kesMomoAddress = kesMomoMoneyAddress1.toTypedMoneyAddress() as MobileMoneyAddress
    assertEquals(
      MobileMoneyAddress(KES, MomoAddress(MPESA, "11111"), kesMomoMoneyAddress1.id),
      kesMomoAddress
    )
  }

  @Test
  fun toTypedAddressesFilteredByCurrency() {
    val bitcoinAddresses =
      manyMoneyAddresses.map { it.toTypedMoneyAddress() }.filter { it.currency == BTC }
    assertEquals(
      listOf(
        BtcOnChainAddress(btcOnChainMoneyAddress1.pss, btcOnChainMoneyAddress1.id),
        BtcOnChainAddress(btcOnChainMoneyAddress2.pss, btcOnChainMoneyAddress2.id),
        BtcLightningAddress(btcLightningMoneyAddress1.pss, btcLightningMoneyAddress1.id),
        BtcLightningAddress(btcLightningMoneyAddress2.pss, btcLightningMoneyAddress2.id),
      ),
      bitcoinAddresses
    )
    val kesAddresses =
      manyMoneyAddresses.map { it.toTypedMoneyAddress() }.filter { it.currency == KES }
    assertEquals(
      listOf(
        MobileMoneyAddress(KES, MomoAddress.parse(kesMomoMoneyAddress1.pss), kesMomoMoneyAddress1.id),
        MobileMoneyAddress(KES, MomoAddress.parse(kesMomoMoneyAddress2.pss), kesMomoMoneyAddress2.id),
      ),
      kesAddresses
    )
  }

  @Test
  fun toTypedAddressWithFilteringByIsInstance() {
    assertEquals(
      listOf(
        BtcLightningAddress(btcLightningMoneyAddress1.pss, btcLightningMoneyAddress1.id),
        BtcLightningAddress(btcLightningMoneyAddress2.pss, btcLightningMoneyAddress2.id),
      ),
      manyMoneyAddresses.map { it.toTypedMoneyAddress() }.filterIsInstance<BtcLightningAddress>()
    )
  }

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
