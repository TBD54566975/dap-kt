package xyz.block.dap

import com.fasterxml.jackson.annotation.JsonIgnore
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
import java.net.UnknownHostException

// This implements part of the DAP resolution process.
// See [Resolver] for the full resolution process.
// See the [DAP spec](https://github.com/TBD54566975/dap#resolution)

class RegistryDidResolver(
  configuration: RegistryDidResolverConfiguration
) {
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

  fun getDid(dapRegistryUrl: URL, dap: Dap): Did {
    val fullUrl = URL("$dapRegistryUrl/daps/${dap.handle}")

    val resp: HttpResponse = try {
      runBlocking {
        client.get(fullUrl) {
          contentType(ContentType.Application.Json)
        }
      }
    } catch (e: UnknownHostException) {
      throw RegistryDidResolutionException("Failed to reach DAP Registry", e)
    }

    val body = runBlocking { resp.bodyAsText() }
    if (!resp.status.isSuccess()) {
      throw RegistryDidResolutionException("Failed to read from DAP registry")
    }
    val resolutionResponse = mapper.readValue(body, DapRegistryResolutionResponse::class.java)
    // TODO - should we verify the proof here?
    if (resolutionResponse.did == null) {
      throw RegistryDidResolutionException("DAP registry did not return a DID")
    }

    return Did.parse(resolutionResponse.did)
  }
}

class Proof(
  val id: String,
  val handle: String,
  val did: String,
  val domain: String,
  val signature: String
)

class DapRegistryResolutionResponse(
  val did: String?,
  @JsonIgnore val proof: Proof?
)

class RegistryDidResolverConfiguration internal constructor(
  var engine: HttpClientEngine? = null
)

fun RegistryDidResolver(configuration: RegistryDidResolverConfiguration.() -> Unit): RegistryDidResolver {
  val config = RegistryDidResolverConfiguration().apply(configuration)
  return RegistryDidResolver(config)
}

class RegistryDidResolutionException : Throwable {
  constructor(message: String, cause: Throwable?) : super(message, cause)
  constructor(message: String) : super(message)
}
