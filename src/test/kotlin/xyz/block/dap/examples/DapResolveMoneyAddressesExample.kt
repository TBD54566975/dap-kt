package xyz.block.dap.examples

import xyz.block.dap.Dap
import xyz.block.dap.DapResolver

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
      DapResolver().resolveMoneyAddresses(Dap.parse(dap))
    } catch (_: Throwable) { }
  }
}
