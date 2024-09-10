package xyz.block.dap.examples

import xyz.block.dap.Dap
import xyz.block.dap.DapResolver
import kotlin.test.assertEquals

fun main(args: Array<String>) {
  val dapString = if (args.isNotEmpty()) { args[0] } else { "@example/didpay.me" }
  try {
    val dap = Dap.parse(dapString)
    val moneyAddresses = DapResolver().resolveMoneyAddresses(dap)
    println("Resolved money addresses for $dap: $moneyAddresses")

    // filter by Currency and Protocol
    val btcOnChainAddresses = moneyAddresses
      .filter { it.currency == "btc" }
      .filter { it.protocol == "addr" }
    if (btcOnChainAddresses.isNotEmpty()) {
      println("  found ${btcOnChainAddresses.count()} BTC on-chain addresses")
      btcOnChainAddresses.forEach {
        println("    $it")
        assertEquals("btc", it.currency)
        assertEquals("addr", it.protocol)
      }
    }
  } catch (t: Throwable) {
    println("Failed to resolve money addresses for $dapString: $t")
  }
}
