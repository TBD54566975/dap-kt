package xyz.block.dap

import io.github.oshai.kotlinlogging.KotlinLogging
import web5.sdk.dids.DidResolutionResult
import web5.sdk.dids.DidResolvers
import web5.sdk.dids.didcore.DidDocument
import java.net.URL

/**
 * This resolves a DAP to the DAP registry URL, using web5-kt.
 * This is part of the DAP resolution process.
 * See the [DAP spec](https://github.com/TBD54566975/dap#resolution)
 *
 * The process is
 * - transform the DAP to a web DID
 * - resolve the web DID
 * - find the (first) DAP registry service endpoint URL in the DID document
 *
 * Any errors in the process will throw a [RegistryResolutionException].
 */
class RegistryResolver {

  fun resolveRegistryUrl(dap: Dap): URL {
    val did = dap.toWebDid()
    val didResolutionResult = resolveDid(did)
    if (didResolutionResult.didResolutionMetadata.error != null) {
      throw RegistryResolutionException("DID resolution failed [did=$did][error=${didResolutionResult.didResolutionMetadata.error}]")
    }
    val didDocument = didResolutionResult.didDocument
      ?: throw RegistryResolutionException("DID resolution returned no DID Document [did=$did]")
    val url = findDapRegistryUrl(didDocument, did)
    try {
      val result = URL(url)
      logger.info { "resolved DAP registry URL [dap=$dap][url=$url]" }
      return result
    } catch (e: Throwable) {
      throw RegistryResolutionException("Invalid DAP registry url [url=$url][error=${e.message}]", e)
    }
  }

  private fun resolveDid(did: String): DidResolutionResult {
    try {
      return DidResolvers.resolve(did)
    } catch (e: Throwable) {
      throw RegistryResolutionException("DID resolution failed [did=$did][error=${e.message}", e)
    }
  }

  private fun findDapRegistryUrl(didDocument: DidDocument, did: String): String {
    val service = didDocument.service?.find { it.type == Dap.SERVICE_TYPE }
      ?: throw RegistryResolutionException("DID document has no DAP registry service [did=$did]")

    if (service.serviceEndpoint.isEmpty()) {
      throw RegistryResolutionException("DAP registry service has no service endpoints [did=$did]")
    }
    // TODO - ask web5-kt - what do we do with multiple endpoints?
    return service.serviceEndpoint[0]
  }

  private val logger = KotlinLogging.logger {}
}

class RegistryResolutionException : Throwable {
  constructor(message: String, cause: Throwable?) : super(message, cause)
  constructor(message: String) : super(message)
}
