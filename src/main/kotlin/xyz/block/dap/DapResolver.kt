package xyz.block.dap

import xyz.block.maddr.MoneyAddress

// This implements the DAP resolution process
// See the [DAP spec](https://github.com/TBD54566975/dap#resolution)
class DapResolver(
  private val registryResolver: RegistryResolver = RegistryResolver(),
  private val registryDidResolver: RegistryDidResolver = RegistryDidResolver {},
  private val moneyAddressResolver: MoneyAddressResolver = MoneyAddressResolver()
) {
  fun resolveMoneyAddresses(dap: Dap): List<MoneyAddress> {
    val registryUrl = registryResolver.resolveRegistryUrl(dap)
    val did = registryDidResolver.getDid(registryUrl, dap)
    val moneyAddresses = moneyAddressResolver.resolveMoneyAddresses(did)
    return moneyAddresses
  }
}
