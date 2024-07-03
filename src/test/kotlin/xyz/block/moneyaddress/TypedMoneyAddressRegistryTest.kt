package xyz.block.moneyaddress

import xyz.block.moneyaddress.Currency.Companion.asCurrency
import xyz.block.moneyaddress.MoneyAddressExamples.Companion.btcLightningMoneyAddress1
import xyz.block.moneyaddress.MoneyAddressExamples.Companion.btcLightningMoneyAddress2
import xyz.block.moneyaddress.MoneyAddressExamples.Companion.btcOnChainMoneyAddress1
import xyz.block.moneyaddress.MoneyAddressExamples.Companion.btcOnChainMoneyAddress2
import xyz.block.moneyaddress.MoneyAddressExamples.Companion.kesMomoMoneyAddress1
import xyz.block.moneyaddress.MoneyAddressExamples.Companion.kesMomoMoneyAddress2
import xyz.block.moneyaddress.MoneyAddressExamples.Companion.manyMoneyAddresses
import xyz.block.moneyaddress.MoneyAddressExamples.Companion.zzzUnrecognizedMoneyAddress1
import xyz.block.moneyaddress.MoneyAddressRegistry.Companion.defaultMoneyAddressRegistry
import xyz.block.moneyaddress.MoneyAddressRegistry.Companion.toTypedMoneyAddress
import xyz.block.moneyaddress.Protocol.Companion.asProtocol
import xyz.block.moneyaddress.typed.BtcLightningAddress
import xyz.block.moneyaddress.typed.BtcOnChainAddress
import xyz.block.moneyaddress.typed.MPESA
import xyz.block.moneyaddress.typed.MobileMoneyAddress
import xyz.block.moneyaddress.typed.MomoAddress
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TypedMoneyAddressRegistryTest {

  @Test
  fun toTypedAddress() {
    val btcLightningAddress = btcLightningMoneyAddress1.toTypedMoneyAddress()
    assertIs<BtcLightningAddress>(btcLightningAddress)
    assertEquals(
      BtcLightningAddress(btcLightningMoneyAddress1.pss, btcLightningMoneyAddress1.id),
      btcLightningAddress
    )

    val kesMomoAddress = kesMomoMoneyAddress1.toTypedMoneyAddress()
    assertIs<MobileMoneyAddress>(kesMomoAddress)
    assertEquals(
      MobileMoneyAddress(KES, MomoAddress(MPESA, "11111"), kesMomoMoneyAddress1.id),
      kesMomoAddress
    )
  }

  @Test
  fun toTypedAddressWithDefaultRegistry() {
    assertIs<BtcLightningAddress>(
      btcLightningMoneyAddress1.toTypedMoneyAddress(defaultMoneyAddressRegistry)
    )
    assertIs<MobileMoneyAddress>(
      kesMomoMoneyAddress1.toTypedMoneyAddress(defaultMoneyAddressRegistry)
    )
    assertIs<UnrecognizedMoneyAddress>(
      zzzUnrecognizedMoneyAddress1.toTypedMoneyAddress(defaultMoneyAddressRegistry)
    )
  }

  @Test
  fun toTypedAddressWithCustomRegistry() {
    val moneyAddressRegistry = MoneyAddressRegistry()
    assertIs<UnrecognizedMoneyAddress>(btcOnChainMoneyAddress1.toTypedMoneyAddress(moneyAddressRegistry))

    BtcOnChainAddress.register(moneyAddressRegistry)
    assertIs<BtcOnChainAddress>(btcOnChainMoneyAddress1.toTypedMoneyAddress(moneyAddressRegistry))

    moneyAddressRegistry.clear()
    assertIs<UnrecognizedMoneyAddress>(btcOnChainMoneyAddress1.toTypedMoneyAddress(moneyAddressRegistry))
  }

  @Test
  fun toTypedAddressesFilteredByCurrency() {
    val bitcoinAddresses =
      manyMoneyAddresses.map { it.toTypedMoneyAddress() }.filter { it.currency == BTC }
    assertEquals(
      listOf(
        BtcOnChainAddress(btcOnChainMoneyAddress1.pss, btcOnChainMoneyAddress1.id),
        BtcOnChainAddress(btcOnChainMoneyAddress2.pss, btcOnChainMoneyAddress2.id),
        BtcLightningAddress(btcLightningMoneyAddress1.pss, btcLightningMoneyAddress1.id),
        BtcLightningAddress(btcLightningMoneyAddress2.pss, btcLightningMoneyAddress2.id),
      ),
      bitcoinAddresses
    )
    val kesAddresses =
      manyMoneyAddresses.map { it.toTypedMoneyAddress() }.filter { it.currency == KES }
    assertEquals(
      listOf(
        MobileMoneyAddress(KES, MomoAddress(MPESA, "11111"), kesMomoMoneyAddress1.id),
        MobileMoneyAddress(
          KES,
          MomoAddress.parse(kesMomoMoneyAddress2.pss),
          kesMomoMoneyAddress2.id
        ),
      ),
      kesAddresses
    )
  }

  @Test
  fun toTypedAddressWithFilteringByIsInstance() {
    assertEquals(
      listOf(
        BtcLightningAddress(btcLightningMoneyAddress1.pss, btcLightningMoneyAddress1.id),
        BtcLightningAddress(btcLightningMoneyAddress2.pss, btcLightningMoneyAddress2.id),
      ),
      manyMoneyAddresses.map { it.toTypedMoneyAddress() }.filterIsInstance<BtcLightningAddress>()
    )
  }

  @Test
  fun toTypedAddressReturnsUnrecognizedMoneyAddressForUnknownCurrencyProtocol() {
    val unrecognizedMoneyAddress = zzzUnrecognizedMoneyAddress1.toTypedMoneyAddress()
    assertIs<UnrecognizedMoneyAddress>(unrecognizedMoneyAddress)
    assertEquals(
      UnrecognizedMoneyAddress(
        zzzUnrecognizedMoneyAddress1.pss,
        zzzUnrecognizedMoneyAddress1.currency.asCurrency(),
        zzzUnrecognizedMoneyAddress1.protocol.asProtocol(),
        zzzUnrecognizedMoneyAddress1.id
      ),
      unrecognizedMoneyAddress
    )
  }

  @Test
  fun toTypedAddressReturnsUnrecognizedMoneyAddress() {
    val unrecognizedMoneyAddress = zzzUnrecognizedMoneyAddress1.toTypedMoneyAddress()
    assertIs<UnrecognizedMoneyAddress>(unrecognizedMoneyAddress)
    assertEquals(
      UnrecognizedMoneyAddress(
        zzzUnrecognizedMoneyAddress1.pss,
        zzzUnrecognizedMoneyAddress1.currency.asCurrency(),
        zzzUnrecognizedMoneyAddress1.protocol.asProtocol(),
        zzzUnrecognizedMoneyAddress1.id
      ),
      unrecognizedMoneyAddress
    )
  }
}
