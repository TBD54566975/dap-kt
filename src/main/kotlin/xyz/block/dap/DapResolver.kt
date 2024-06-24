package xyz.block.dap

import xyz.block.maddr.MoneyAddress

// This implements the DAP resolution process
// See the [DAP spec](https://github.com/TBD54566975/dap#resolution)
class DapResolver(
  val registryResolver: RegistryResolver,
  val registryDidResolver: RegistryDidResolver,
  val moneyAddressResolver: MoneyAddressResolver
) {
  fun resolveMoneyAddresses(dap: Dap): List<MoneyAddress> {
    val registryUrl = registryResolver.resolveRegistryUrl(dap)
    val did = registryDidResolver.getDid(registryUrl, dap)
    return moneyAddressResolver.resolveMoneyAddresses(did)
  }
}
