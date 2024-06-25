package xyz.block.moneyaddress

import org.junit.jupiter.api.assertThrows
import xyz.block.moneyaddress.urn.InvalidUrnException
import kotlin.test.Test
import kotlin.test.assertEquals
import web5.sdk.dids.didcore.Service

class MoneyAddressTest {

  @Test
  fun testDidToMoneyAddress() {
    val did = Service("didpay", "MoneyAddress", listOf("urn:currency:protocol:pss"))

    val moneyAddresses = did.toMoneyAddresses()
    assertEquals(1, moneyAddresses.size)
    val moneyAddress = moneyAddresses[0]
    assertEquals("didpay", moneyAddress.id)
    assertEquals("urn:currency:protocol:pss", moneyAddress.urn.toString())
    assertEquals("currency", moneyAddress.currency)
    assertEquals("protocol", moneyAddress.protocol)
    assertEquals("pss", moneyAddress.pss)
  }

  @Test
  fun testDidToMultipleMoneyAddresses() {
    val did = Service(
      "didpay",
      "MoneyAddress",
      listOf(
        "urn:btc:addr:fakeAddress",
        "urn:btc:lnurl:http://fakeLnurl",
        "urn:btc:spaddr:fakeSPAddress",
      )
    )

    val moneyAddresses = did.toMoneyAddresses()
    assertEquals(3, moneyAddresses.size)

    moneyAddresses[0].let { moneyAddress ->
      assertEquals("didpay", moneyAddress.id)
      assertEquals("urn:btc:addr:fakeAddress", moneyAddress.urn.toString())
      assertEquals("btc", moneyAddress.currency)
      assertEquals("addr", moneyAddress.protocol)
      assertEquals("fakeAddress", moneyAddress.pss)
    }

    moneyAddresses[1].let { moneyAddress ->
      assertEquals("didpay", moneyAddress.id)
      assertEquals("urn:btc:lnurl:http://fakeLnurl", moneyAddress.urn.toString())
      assertEquals("btc", moneyAddress.currency)
      assertEquals("lnurl", moneyAddress.protocol)
      assertEquals("http://fakeLnurl", moneyAddress.pss)
    }

    moneyAddresses[2].let { moneyAddress ->
      assertEquals("didpay", moneyAddress.id)
      assertEquals("urn:btc:spaddr:fakeSPAddress", moneyAddress.urn.toString())
      assertEquals("btc", moneyAddress.currency)
      assertEquals("spaddr", moneyAddress.protocol)
      assertEquals("fakeSPAddress", moneyAddress.pss)
    }

  }

  @Test
  fun testDidToMoneyAddressInvalidServiceType() {
    assertThrows<InvalidMoneyAddressException> {
      val did = Service("didpay", "not-a-MoneyAddress", listOf("urn:nid:protocol:pss"))
      did.toMoneyAddresses()
    }
  }

  @Test
  fun testDidToMoneyAddressInvalidUrn() {
    assertThrows<InvalidUrnException> {
      val did = Service("didpay", "MoneyAddress", listOf("not-a-urn"))
      did.toMoneyAddresses()
    }
  }
}
