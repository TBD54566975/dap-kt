package xyz.block.dap

import io.github.oshai.kotlinlogging.KotlinLogging
import xyz.block.moneyaddress.MoneyAddress

/**
 * Resolves a Decentralized Agnostic Paytag (DAP).
 * See the [DAP spec](https://github.com/TBD54566975/dap#resolution) for the resolution process.
 *
 * This wires together the RegistryResolver, RegistryDidResolver, and MoneyAddressResolver.
 * The RegistryDidResolver will use the default configuration unless an instance is provided that
 * is constructed with a block configuration override.
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
    try {
      val registryUrl = registryResolver.resolveRegistryUrl(dap)
      val did = registryDidResolver.getUnprovenDid(registryUrl, dap)
      val moneyAddresses = moneyAddressResolver.resolveMoneyAddresses(did)
      logger.info {
        "resolved money addresses [dap=$dap][moneyAddresses=${moneyAddresses.map { it.urn }}]"
      }
      return moneyAddresses
    } catch (t: Throwable) {
      logger.warn(t) { "error resolving money addresses for DAP [dap=$dap][error=${t.message}]" }
      throw t
    }
  }

  // TODO - either use the registryDidResolver.getProvenDid above, or add a method
  // `resolveProvenMoneyAddresses` that verifies the proof of the DID returned by the registry

  private val logger = KotlinLogging.logger {}
}
