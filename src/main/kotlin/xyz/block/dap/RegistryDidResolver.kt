package xyz.block.dap

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.jackson.jackson
import kotlinx.coroutines.runBlocking
import okhttp3.Cache
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import web5.sdk.common.Json
import web5.sdk.dids.didcore.Did
import java.io.File
import java.net.InetAddress
import java.net.URL

// This uses a modified version of the scheme used in web5-kt to provide customization
// of the configuration (see `DidWeb`).
// The difference is that we provide two overloaded functions for constructing the resolver,
// which look like two overloaded constructors to the caller.
// This is instead of a single function and the use of the `Default` companion object.
// We also don't need to expose both a `RegistryDidResolver` and `RegistryDidResolverApi`.
// This is probably still too complicated.

/**
 * A RegistryDidResolver with the default configuration.
 */
fun RegistryDidResolver(): RegistryDidResolver = RegistryDidResolver.default

/**
 * Constructs a RegistryDidResolver with the block configuration applied.
 */
fun RegistryDidResolver(
  blockConfiguration: RegistryDidResolverConfiguration.() -> Unit
): RegistryDidResolver {
  val config = RegistryDidResolverConfiguration().apply(blockConfiguration)
  return RegistryDidResolverImpl(config)
}

// This allows the RegistryDidResolver to be sealed
private class RegistryDidResolverImpl(
  configuration: RegistryDidResolverConfiguration
) : RegistryDidResolver(configuration)

/**
 * Given the URL for the DAP registry and a DAP, resolves the DID using the registry.
 */
sealed class RegistryDidResolver(
  configuration: RegistryDidResolverConfiguration
) {
  /**
   * Resolves the DAP using the registry, to retrieve the DID.
   * Does NOT verify Any proof in the response from the registry.
   */
  fun getUnprovenDid(dapRegistryUrl: URL, dap: Dap): Did =
    getDidWithProof(dapRegistryUrl, dap).did

  /*
  // TODO - implement proof verification of the DID returned by the registry
  // TODO - What should this do if there is no proof?
  fun getProvableDid(dapRegistryUrl: URL, dap: Dap): Did {
    val didWithProof = getDidWithProof(dapRegistryUrl, dap)
    // TODO - verify the proof using web5-kt
    return didWithProof.did
  }
  */

  internal fun getDidWithProof(dapRegistryUrl: URL, dap: Dap): DidWithProof {
    val fullUrl = URL("$dapRegistryUrl/daps/${dap.handle}")

    val resp: HttpResponse = try {
      runBlocking {
        client.get(fullUrl) {
          contentType(ContentType.Application.Json)
        }
      }
    } catch (e: Throwable) {
      throw RegistryDidResolutionException("Error fetching DAP from registry [dap=$dap][url=$fullUrl][error=${e.message}]", e)
    }

    val body = runBlocking { resp.bodyAsText() }
    if (!resp.status.isSuccess()) {
      throw RegistryDidResolutionException("Failed to read from DAP registry [dap=$dap][url=$fullUrl][status=${resp.status}]")
    }
    val resolutionResponse = try {
      mapper.readValue(body, DapRegistryResolutionResponse::class.java)
    } catch (e: Throwable) {
      throw RegistryDidResolutionException("Failed to parse DAP registry response [dap=$dap][url=$fullUrl][error=${e.message}]", e)
    }
    if (resolutionResponse.did == null) {
      throw RegistryDidResolutionException("DAP registry did not return a DID [dap=$dap][url=$fullUrl]")
    }

    val did = try {
      Did.parse(resolutionResponse.did)
    } catch (e: Throwable) {
      throw RegistryDidResolutionException("Failed to parse DID from DAP registry response [dap=$dap][url=$fullUrl", e)
    }
    logger.info { "resolved DID [dap=$dap][url=$fullUrl][did=$did]" }
    return DidWithProof(did, resolutionResponse.proof)
  }

  companion object {
    /**
     * A singleton RegistryDidResolver with the default configuration
     */
    internal val default: RegistryDidResolver by lazy {
      RegistryDidResolverImpl(RegistryDidResolverConfiguration())
    }
  }

  private val engine: HttpClientEngine = configuration.engine ?: OkHttp.create {
    val appCache = Cache(File("cacheDir", "okhttpcache"), 10 * 1024 * 1024)
    val bootstrapClient = OkHttpClient.Builder().cache(appCache).build()

    val dns = DnsOverHttps.Builder()
      .client(bootstrapClient)
      .url("https://dns.quad9.net/dns-query".toHttpUrl())
      .bootstrapDnsHosts(
        InetAddress.getByName("9.9.9.9"),
        InetAddress.getByName("149.112.112.112")
      )
      .build()

    val client = bootstrapClient.newBuilder().dns(dns).build()
    preconfigured = client
  }

  private val client = HttpClient(engine) {
    install(ContentNegotiation) {
      jackson { mapper }
    }
  }

  private val mapper = Json.jsonMapper

  private val logger = KotlinLogging.logger {}
}

internal data class DidWithProof(
  val did: Did,
  val proof: Proof?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Proof(
  val id: String,
  val handle: String,
  val did: String,
  val domain: String,
  val signature: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class DapRegistryResolutionResponse(
  val did: String?,
  val proof: Proof?
)

/**
 * Configuration options for the [RegistryDidResolver].
 *
 * - [engine] is used to override the ktor HTTP engine.
 * The default HTTP engine uses [OkHttp] with [DnsOverHttps] and a 10MB cache.
 */
class RegistryDidResolverConfiguration internal constructor(
  var engine: HttpClientEngine? = null
)

class RegistryDidResolutionException : Throwable {
  constructor(message: String, cause: Throwable?) : super(message, cause)
  constructor(message: String) : super(message)
}
