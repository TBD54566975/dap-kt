package xyz.block.dap

import web5.sdk.dids.DidResolutionResult
import web5.sdk.dids.DidResolvers
import web5.sdk.dids.didcore.Service
import java.net.URL

// This implements part of the DAP resolution process.
// See [Resolver] for the full resolution process.
// See the [DAP spec](https://github.com/TBD54566975/dap#resolution)
class RegistryResolver {

  fun resolveRegistryUrl(dap: Dap): URL {
    val did = dap.toWebDid()
    val didResolutionResult = resolveDid(did)
    val dapRegistryService = findDapRegistryService(didResolutionResult)
    val dapRegistryUrl = findDapRegistryUrl(dapRegistryService)
    try {
      return URL(dapRegistryUrl)
    } catch (e: Throwable) {
      throw RegistryResolutionException("Invalid DAP registry url", e)
    }
  }

  private fun resolveDid(did: String): DidResolutionResult {
    try {
      return DidResolvers.resolve(did)
    } catch (e: Throwable) {
      throw RegistryResolutionException("DID resolution failed", e)
    }
  }

  private fun findDapRegistryService(didResolutionResult: DidResolutionResult): Service =
    didResolutionResult.didDocument?.service?.find { it.type == Dap.SERVICE_TYPE }
      ?: throw RegistryResolutionException("DAP registry service not found")

  private fun findDapRegistryUrl(dapRegistryService: Service): String {
    if (dapRegistryService.serviceEndpoint.size != 1) {
      throw RegistryResolutionException("DAP registry has no service endpoints")
    }
    // TODO - feedback to web5-kt, should this be a list or a single string?
    return dapRegistryService.serviceEndpoint[0]
  }

  companion object {
    fun Dap.toWebDid(): String {
      return "did:web:${domain}"
    }
  }
}

class RegistryResolutionException : Throwable {
  constructor(message: String, cause: Throwable?) : super(message, cause)
  constructor(message: String) : super(message)
}
