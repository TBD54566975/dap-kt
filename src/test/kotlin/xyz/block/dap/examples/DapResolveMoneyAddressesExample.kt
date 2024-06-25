package xyz.block.dap.examples

import xyz.block.dap.Dap
import xyz.block.dap.DapResolver
import xyz.block.moneyaddress.MoneyAddress

fun main(args: Array<String>) {
  if (args.isEmpty()) {
    printDapResolvedMoneyAddresses("@moegrammer/didpay.me")
  } else {
    args.forEach { dap ->
      printDapResolvedMoneyAddresses(dap)
    }
  }
}

private fun printDapResolvedMoneyAddresses(value: String) {
  println("Resolving DAP: $value")
  val dap = Dap.parse(value)
  val moneyAddresses: List<MoneyAddress> = DapResolver().resolveMoneyAddresses(dap)
  println("Found ${moneyAddresses.size} money addresses")
  moneyAddresses.forEach {
    println("  currency=${it.currency} css=${it.css} id=${it.id} urn=${it.urn}")
  }
}