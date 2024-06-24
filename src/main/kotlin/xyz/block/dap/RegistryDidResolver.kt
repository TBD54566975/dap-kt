package xyz.block.dap

import web5.sdk.dids.didcore.Did
import java.net.URL

// This implements part of the DAP resolution process.
// See [Resolver] for the full resolution process.
// See the [DAP spec](https://github.com/TBD54566975/dap#resolution)
class RegistryDidResolver {

  fun getDid(dapResistryUrl: URL, dap: Dap): Did {
    // TODO - implement this
    // val fullUrl = URL("$dapResistryUrl/daps/${dap.handle}")
    // call GET on the dapRegistryUrl to get the did for the document with the money addresses
    // TODO - should we validate the proof here?
    // parse the result document to get the did
    return Did.parse("did:web:didpay.me:moegrammer")
  }
}

class RegistryDidResolutionException : Throwable {
  constructor(message: String, cause: Throwable?) : super(message, cause)
  constructor(message: String) : super(message)
}
