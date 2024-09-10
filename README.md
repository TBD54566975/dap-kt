# Kotlin SDK for DAPs

This repository contains the Kotlin SDK for [DAPs (Decentralized Agnostic Paytags)](https://github.com/TBD54566975/dap).
This is built on the [Kotlin SDK for Web5](https://github.com/TBD54566975/web5-kt),
and provides data classes to represent `DAP`s and `MoneyAddress`es, as well as an implementaion of the "DAP resolution" process via a `DapResolver` to resolve the money addresses for a given DAP.

## Table of Contents

- [Installation](#installation)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Building the Project](#building-the-project)
- [License](#license)

## Installation

To use this SDK in your Kotlin project, add the following to your `build.gradle.kts` file:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.dap:dap:0.5.2")
}
```

## Examples

### Simple Example

Below is the simplest example of how to use the SDK to resolve the money addresses for a DAP.

Note the `DapResolver` is a convenience wrapper around the pluggable parts of the DAP resolution process,
namely the `RegistryDidResolver`, `RegistryResolver` and `MoneyAddressResolver`.
Those classes can be used independently if the `DapResolver` does not provide what you need (for example to bypass DNS over HTTP).

See [ResolveDapMoneyAddressesExample.kt](src/test/kotlin/xyz/block/dap/examples/ResolveDapMoneyAddressesExample.kt) for this example.


```kotlin
import xyz.block.dap.Dap
  import xyz.block.dap.DapResolver

fun main(args: Array<String>) {
  val dapString = if (args.isNotEmpty()) { args[0] } else { "@example/didpay.me" }
  try {
    val dap = Dap.parse(dapString)
    val moneyAddresses = DapResolver().resolveMoneyAddresses(dap)
    println("Resolved money addresses for $dap: $moneyAddresses")
  } catch (t: Throwable) {
    println("Failed to resolve money addresses for $dapString: $t")
  }
}
```

### Filtering Money Addresses

The `currency` and `protocol` properties of the `MoneyAddress` class can be used to filter the resolved money addresses.

See [ResolveDapMoneyAddressesWithFilteringExample.kt](src/test/kotlin/xyz/block/dap/examples/ResolveDapMoneyAddressesWithFilteringExample.kt) for this example.

```kotlin
import xyz.block.dap.Dap
import xyz.block.dap.DapResolver
import kotlin.test.assertEquals

fun main(args: Array<String>) {
  val dapString = if (args.isNotEmpty()) { args[0] } else { "@example/didpay.me" }
  try {
    val dap = Dap.parse(dapString)
    val moneyAddresses = DapResolver().resolveMoneyAddresses(dap)
    println("Resolved money addresses for $dap: $moneyAddresses")

    // filter by Currency and Protocol
    val btcOnChainAddresses = moneyAddresses
      .filter { it.currency == "btc" }
      .filter { it.protocol == "addr" }
    if (btcOnChainAddresses.isNotEmpty()) {
      println("  found ${btcOnChainAddresses.count()} BTC on-chain addresses")
      btcOnChainAddresses.forEach {
        println("    $it")
        assertEquals("btc", it.currency)
        assertEquals("addr", it.protocol)
      }
    }
  } catch (t: Throwable) {
    println("Failed to resolve money addresses for $dapString: $t")
  }
}
```

### Strongly typed Money Addresses

You can optionally use the strongly-typed `TypedMoneyAddress` classes, which provide data classes for the known currencies and protocols documented in the DAP specification, as well as data classes for the relevant combinations (such as `BtcOnChainAddress`, `BtcLightningAddress` and `BtcLightningOffer`).
This can help to avoid errors by mis-typing the currency and protocol schemes, and make using the currency or protocol-specific properties easier (for example `MobileMoneyAddress`).

See [TypedMoneyAddressesExample.kt](src/test/kotlin/xyz/block/dap/examples/TypedMoneyAddressesExample.kt) for this example.

```kotlin
import xyz.block.dap.Dap
import xyz.block.dap.DapResolver
import xyz.block.moneyaddress.BTC
import xyz.block.moneyaddress.LIGHTNING_ADDRESS
import xyz.block.moneyaddress.ONCHAIN_ADDRESS
import xyz.block.moneyaddress.TypedMoneyAddressRegistry.Companion.toTypedMoneyAddress
import xyz.block.moneyaddress.typed.BtcLightningAddress
import kotlin.test.assertEquals

fun main(args: Array<String>) {
  val dapString = if (args.isNotEmpty()) { args[0] } else { "@example/didpay.me" }
  try {
    val dap = Dap.parse(dapString)
    val moneyAddresses = DapResolver().resolveMoneyAddresses(dap)

    println("Resolved money addresses for $dap: $moneyAddresses")

    val typedMoneyAddresses = moneyAddresses.map { it.toTypedMoneyAddress() }
    println("Resolved typed money addresses for $dap: $typedMoneyAddresses")

    // filter by Currency and Protocol
    val btcOnChainAddresses = moneyAddresses
      .map { it.toTypedMoneyAddress() }
      .filter { it.currency == BTC }
      .filter { it.protocol == ONCHAIN_ADDRESS }
    if (btcOnChainAddresses.isNotEmpty()) {
      println("  found ${btcOnChainAddresses.count()} BTC on-chain addresses")
      btcOnChainAddresses.forEach {
        println("    $it")
        assertEquals(BTC, it.currency)
        assertEquals(ONCHAIN_ADDRESS, it.protocol)
      }
    }

    // filter by specific address type which encapsulates currency/protocol
    val btcLightningAddresses = moneyAddresses
      .map { it.toTypedMoneyAddress() }
      .filterIsInstance<BtcLightningAddress>()
    if (btcLightningAddresses.isNotEmpty()) {
      println("  found ${btcLightningAddresses.count()} BTC lightning addresses")
      btcLightningAddresses.forEach {
        println("    $it")
        assertEquals(BTC, it.currency)
        assertEquals(LIGHTNING_ADDRESS, it.protocol)
      }
    }
  } catch (t: Throwable) {
    println("Failed to resolve money addresses for $dapString: $t")
  }
}
```

## Building the Project

### Using Just
If you have [Just](https://github.com/casey/just) installed, you can use the Justfile included in the repository:

```sh
just test
```

### Using Maven

```sh
mvn clean install
```

## License

This project is licensed under the terms of the [LICENSE](LICENSE) file.
