package xyz.block.dap

import web5.sdk.dids.DidResolvers
import web5.sdk.dids.didcore.Did
import web5.sdk.dids.didcore.DidDocument
import xyz.block.maddr.MoneyAddress
import xyz.block.maddr.toMoneyAddresses

// This implements part of the DAP resolution process.
// See [Resolver] for the full resolution process.
// See the [DAP spec](https://github.com/TBD54566975/dap#resolution)
class MoneyAddressResolver {

  fun resolveMoneyAddresses(did: Did): List<MoneyAddress> {
    val didDocument = resolveDidDocument(did)
    val moneyAddresses = parseDidDocumentForMoneyAddresses(didDocument)
    return moneyAddresses
  }

  private fun resolveDidDocument(did: Did): DidDocument {
    val didResolutionResult = try {
      DidResolvers.resolve(did.toString())
    } catch (e: Throwable) {
      throw MoneyAddressResolutionException("DID resolution failed", e)
    }
    didResolutionResult.didDocument?.let { return it }
    throw MoneyAddressResolutionException("DID resolution failed with no document found")
  }

  private fun parseDidDocumentForMoneyAddresses(didDocument: DidDocument): List<MoneyAddress> {
    return didDocument.service?.find { it.type == MoneyAddress.KIND }?.toMoneyAddresses()
      ?: emptyList()
  }
}

class MoneyAddressResolutionException : Throwable {
  constructor(message: String, cause: Throwable?) : super(message, cause)
  constructor(message: String) : super(message)
}
