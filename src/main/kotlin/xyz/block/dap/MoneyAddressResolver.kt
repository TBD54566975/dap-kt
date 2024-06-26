package xyz.block.dap

import web5.sdk.dids.DidResolvers
import web5.sdk.dids.didcore.Did
import web5.sdk.dids.didcore.DidDocument
import xyz.block.moneyaddress.MoneyAddress
import xyz.block.moneyaddress.toMoneyAddresses

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
      throw MoneyAddressResolutionException("DID resolution failed [did=$did][error=${e.message}]", e)
    }
    return didResolutionResult.didDocument
      ?: throw MoneyAddressResolutionException("No DID document found [did=$did]")
  }

  private fun parseDidDocumentForMoneyAddresses(didDocument: DidDocument): List<MoneyAddress> {
    val moneyAddressServices =
      didDocument.service?.filter { it.type == MoneyAddress.KIND } ?: emptyList()
    return moneyAddressServices.fold(emptyList()) { acc, service ->
      acc + service.toMoneyAddresses()
    }
  }
}

class MoneyAddressResolutionException : Throwable {
  constructor(message: String, cause: Throwable?) : super(message, cause)
  constructor(message: String) : super(message)
}
