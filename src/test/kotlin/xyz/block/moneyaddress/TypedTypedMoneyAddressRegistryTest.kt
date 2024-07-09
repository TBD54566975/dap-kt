package xyz.block.moneyaddress

import xyz.block.moneyaddress.Currency.Companion.asCurrency
import xyz.block.moneyaddress.MoneyAddressExamples.Companion.btcLightningAddressMoneyAddress1
import xyz.block.moneyaddress.MoneyAddressExamples.Companion.btcLightningAddressMoneyAddress2
import xyz.block.moneyaddress.MoneyAddressExamples.Companion.btcLightningOfferMoneyAddress1
import xyz.block.moneyaddress.MoneyAddressExamples.Companion.btcLightningOfferMoneyAddress2
import xyz.block.moneyaddress.MoneyAddressExamples.Companion.btcOnChainMoneyAddress1
import xyz.block.moneyaddress.MoneyAddressExamples.Companion.btcOnChainMoneyAddress2
import xyz.block.moneyaddress.MoneyAddressExamples.Companion.kesMomoMoneyAddress1
import xyz.block.moneyaddress.MoneyAddressExamples.Companion.kesMomoMoneyAddress2
import xyz.block.moneyaddress.MoneyAddressExamples.Companion.manyMoneyAddresses
import xyz.block.moneyaddress.MoneyAddressExamples.Companion.zzzUnrecognizedMoneyAddress1
import xyz.block.moneyaddress.TypedMoneyAddressRegistry.Companion.defaultTypedMoneyAddressRegistry
import xyz.block.moneyaddress.TypedMoneyAddressRegistry.Companion.toTypedMoneyAddress
import xyz.block.moneyaddress.Protocol.Companion.asProtocol
import xyz.block.moneyaddress.typed.BtcLightningAddress
import xyz.block.moneyaddress.typed.BtcLightningOffer
import xyz.block.moneyaddress.typed.BtcOnChainAddress
import xyz.block.moneyaddress.typed.MPESA
import xyz.block.moneyaddress.typed.MobileMoneyAddress
import xyz.block.moneyaddress.typed.MomoAddress
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TypedTypedMoneyAddressRegistryTest {

  @Test
  fun toTypedAddress() {
    val btcLightningAddress = btcLightningAddressMoneyAddress1.toTypedMoneyAddress()
    assertIs<BtcLightningAddress>(btcLightningAddress)
    assertEquals(
      BtcLightningAddress(btcLightningAddressMoneyAddress1.pss, btcLightningAddressMoneyAddress1.id),
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
      btcLightningAddressMoneyAddress1.toTypedMoneyAddress(defaultTypedMoneyAddressRegistry)
    )
    assertIs<MobileMoneyAddress>(
      kesMomoMoneyAddress1.toTypedMoneyAddress(defaultTypedMoneyAddressRegistry)
    )
    assertIs<UnrecognizedMoneyAddress>(
      zzzUnrecognizedMoneyAddress1.toTypedMoneyAddress(defaultTypedMoneyAddressRegistry)
    )
  }

  @Test
  fun toTypedAddressWithCustomRegistry() {
    val typedMoneyAddressRegistry = TypedMoneyAddressRegistry()
    assertIs<UnrecognizedMoneyAddress>(btcOnChainMoneyAddress1.toTypedMoneyAddress(typedMoneyAddressRegistry))

    BtcOnChainAddress.register(typedMoneyAddressRegistry)
    assertIs<BtcOnChainAddress>(btcOnChainMoneyAddress1.toTypedMoneyAddress(typedMoneyAddressRegistry))

    typedMoneyAddressRegistry.clear()
    assertIs<UnrecognizedMoneyAddress>(btcOnChainMoneyAddress1.toTypedMoneyAddress(typedMoneyAddressRegistry))
  }

  @Test
  fun toTypedAddressesFilteredByCurrency() {
    val bitcoinAddresses =
      manyMoneyAddresses.map { it.toTypedMoneyAddress() }.filter { it.currency == BTC }
    assertEquals(
      listOf(
        BtcLightningAddress(btcLightningAddressMoneyAddress1.pss, btcLightningAddressMoneyAddress1.id),
        BtcLightningAddress(btcLightningAddressMoneyAddress2.pss, btcLightningAddressMoneyAddress2.id),
        BtcLightningOffer(btcLightningOfferMoneyAddress1.pss, btcLightningOfferMoneyAddress1.id),
        BtcLightningOffer(btcLightningOfferMoneyAddress2.pss, btcLightningOfferMoneyAddress2.id),
        BtcOnChainAddress(btcOnChainMoneyAddress1.pss, btcOnChainMoneyAddress1.id),
        BtcOnChainAddress(btcOnChainMoneyAddress2.pss, btcOnChainMoneyAddress2.id),
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
        BtcLightningAddress(btcLightningAddressMoneyAddress1.pss, btcLightningAddressMoneyAddress1.id),
        BtcLightningAddress(btcLightningAddressMoneyAddress2.pss, btcLightningAddressMoneyAddress2.id),
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
