package xyz.block.dap.examples

import xyz.block.dap.Dap
import xyz.block.dap.DapResolver

fun main(args: Array<String>) {
  val dapString = if (args.isNotEmpty()) { args[0] } else { "@example/didpay.me" }
  try {
    val dap = Dap.parse(dapString)
    val moneyAddresses = DapResolver().resolveMoneyAddresses(dap)
    println("Resolved money addresses for $dap: $moneyAddresses")
  } catch (t: Throwable) {
    println("Failed to resolve money addresses for $dapString: $t")
  }
}
