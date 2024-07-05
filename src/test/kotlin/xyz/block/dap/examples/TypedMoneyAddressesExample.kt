package xyz.block.dap.examples

import xyz.block.dap.Dap
import xyz.block.dap.DapResolver
import xyz.block.moneyaddress.BTC
import xyz.block.moneyaddress.LIGHTNING_ADDRESS
import xyz.block.moneyaddress.ONCHAIN_ADDRESS
import xyz.block.moneyaddress.TypedMoneyAddressRegistry.Companion.toTypedMoneyAddress
import xyz.block.moneyaddress.typed.BtcLightningAddress
import xyz.block.moneyaddress.typed.BtcOnChainAddress
import kotlin.test.assertEquals

fun main(args: Array<String>) {
  val dapsToResolve =
    if (args.isEmpty()) {
      listOf(
        "@moegrammer2/didpay.me",
        "@thejoker/didpay.me",
        "@thejoker-onchain/didpay.me",
        "@thejoker-ln/didpay.me",
        "@thejoker/cashstaging.app",
        // "@thejoker/127.0.0.1%3A8802",
      )
    } else {
      args.toList()
    }

  dapsToResolve.forEach { dap ->
    try {
      println("resolving DAP $dap")
      val moneyAddresses = DapResolver().resolveMoneyAddresses(Dap.parse(dap)).map { it.toTypedMoneyAddress() }
      val btcOnChainAddresses = moneyAddresses.filterIsInstance<BtcOnChainAddress>()
      println("  found ${btcOnChainAddresses.count()} btc on-chain addresses")
      btcOnChainAddresses.forEach {
        println("    $it")
        assertEquals(BTC, it.currency)
        assertEquals(ONCHAIN_ADDRESS, it.protocol)
      }
      val btcLightningAddresses = moneyAddresses.filterIsInstance<BtcLightningAddress>()
      println("  found ${btcLightningAddresses.count()} btc lightning addresses")
      btcLightningAddresses.forEach {
        println("    $it")
        assertEquals(BTC, it.currency)
        assertEquals(LIGHTNING_ADDRESS, it.protocol)
      }
    } catch (_: Throwable) { }
  }
}
