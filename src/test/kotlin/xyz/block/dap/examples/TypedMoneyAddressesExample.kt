package xyz.block.dap.examples

import xyz.block.dap.Dap
import xyz.block.dap.DapResolver
import xyz.block.moneyaddress.BTC
import xyz.block.moneyaddress.LIGHTNING_ADDRESS
import xyz.block.moneyaddress.ONCHAIN_ADDRESS
import xyz.block.moneyaddress.TypedMoneyAddressRegistry.Companion.toTypedMoneyAddress
import xyz.block.moneyaddress.typed.BtcLightningAddress
import kotlin.test.assertEquals

fun main(args: Array<String>) {
  val dapString = if (args.isNotEmpty()) { args[0] } else { "@example/didpay.me" }
  try {
    val dap = Dap.parse(dapString)
    val moneyAddresses = DapResolver().resolveMoneyAddresses(dap)

    println("Resolved money addresses for $dap: $moneyAddresses")

    val typedMoneyAddresses = moneyAddresses.map { it.toTypedMoneyAddress() }
    println("Resolved typed money addresses for $dap: $typedMoneyAddresses")

    // filter by Currency and Protocol
    val btcOnChainAddresses = moneyAddresses
      .map { it.toTypedMoneyAddress() }
      .filter { it.currency == BTC }
      .filter { it.protocol == ONCHAIN_ADDRESS }
    if (btcOnChainAddresses.isNotEmpty()) {
      println("  found ${btcOnChainAddresses.count()} BTC on-chain addresses")
      btcOnChainAddresses.forEach {
        println("    $it")
        assertEquals(BTC, it.currency)
        assertEquals(ONCHAIN_ADDRESS, it.protocol)
      }
    }

    // filter by specific address type which encapsulates currency/protocol
    val btcLightningAddresses = moneyAddresses
      .map { it.toTypedMoneyAddress() }
      .filterIsInstance<BtcLightningAddress>()
    if (btcLightningAddresses.isNotEmpty()) {
      println("  found ${btcLightningAddresses.count()} BTC lightning addresses")
      btcLightningAddresses.forEach {
        println("    $it")
        assertEquals(BTC, it.currency)
        assertEquals(LIGHTNING_ADDRESS, it.protocol)
      }
    }
  } catch (t: Throwable) {
    println("Failed to resolve money addresses for $dapString: $t")
  }
}
