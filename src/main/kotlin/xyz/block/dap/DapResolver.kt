package xyz.block.dap

import xyz.block.moneyaddress.MoneyAddress

/**
 * Resolves a Decentralized Agnostic Paytag (DAP).
 * See the [DAP spec](https://github.com/TBD54566975/dap#resolution) for the resolution process.
 *
 * This wires together the RegistryResolver, RegistryDidResolver, and MoneyAddressResolver.
 *
 * The RegistryDidResolver will use the default configuration unless an instance is provided that
 * is constructed with a block configuration override (e.g. to change the HTTP engine configuration).
 */
class DapResolver(
  private val registryDidResolver: RegistryDidResolver = RegistryDidResolver(),
) {
  private val registryResolver: RegistryResolver = RegistryResolver()
  private val moneyAddressResolver: MoneyAddressResolver = MoneyAddressResolver()

  /**
   * Resolves the money addresses for a DAP.
   * This does NOT verify the proof of the DID returned by the registry.
   *
   * @param dap the DAP to resolve
   * @return the list of money addresses for the DAP
   */
  fun resolveMoneyAddresses(dap: Dap): List<MoneyAddress> {
    val registryUrl = registryResolver.resolveRegistryUrl(dap)
    val did = registryDidResolver.getUnprovenDid(registryUrl, dap)
    val moneyAddresses = moneyAddressResolver.resolveMoneyAddresses(did)
    return moneyAddresses
  }

  // TODO - either use the registryDidResolver.getProvenDid above, or add a method
  // `resolveProvenMoneyAddresses` that verifies the proof of the DID returned by the registry
}
