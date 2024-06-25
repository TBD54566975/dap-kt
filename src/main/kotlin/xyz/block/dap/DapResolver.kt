package xyz.block.dap

import xyz.block.moneyaddress.MoneyAddress

/**
 * Resolves a Decentralized Agnostic Paytag (DAP)
 * See the [DAP spec](https://github.com/TBD54566975/dap#resolution) for the resolution process.
 *
 * This wires together the RegistryResolver, RegistryDidResolver, and MoneyAddressResolver
 * The
 */
class DapResolver(
  private val registryDidResolver: RegistryDidResolver = defaultRegistryDidResolver(),
) {
  private val registryResolver: RegistryResolver = RegistryResolver()
  private val moneyAddressResolver: MoneyAddressResolver = MoneyAddressResolver()

  /**
   * Resolves the money addresses for a DAP
   * This wires together the RegistryResolver, RegistryDidResolver, and MoneyAddressResolver
   * This does NOT verify the proof of the DID
   *
   * @param dap the DAP to resolve
   * @return the list of money addresses for the DAP
   */
  fun resolveMoneyAddresses(dap: Dap): List<MoneyAddress> {
    val registryUrl = registryResolver.resolveRegistryUrl(dap)
    // TODO - should we verify the proof here?
    val did = registryDidResolver.getUnprovenDid(registryUrl, dap)
    val moneyAddresses = moneyAddressResolver.resolveMoneyAddresses(did)
    return moneyAddresses
  }
}
