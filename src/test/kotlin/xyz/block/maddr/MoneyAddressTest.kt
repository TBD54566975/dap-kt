package xyz.block.maddr

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import xyz.block.maddr.urn.InvalidUrnException

class MoneyAddressTest {
  @Test
  fun testDidToMoneyAddress() {
    val did = DidService("didpay", "maddr", listOf("urn:nid:nss"))

    val moneyAddresses = did.toMoneyAddresses()
    assertEquals(1, moneyAddresses.size)
    val moneyAddress = moneyAddresses[0]
    assertEquals("didpay", moneyAddress.id)
    assertEquals("urn:nid:nss", moneyAddress.urn.toString())
    assertEquals("nid", moneyAddress.currency)
    assertEquals("nss", moneyAddress.css)
  }

  @Test
  fun testDidToMultipleMoneyAddresses() {
    val did = DidService(
      "cashapp",
      "maddr",
      listOf(
        "urn:btc:addr:fakeAddress",
        "urn:btc:lnurl:http://fakeLnurl",
        "urn:btc:spaddr:fakeSPAddress",
      )
    )

    val moneyAddresses = did.toMoneyAddresses()
    assertEquals(3, moneyAddresses.size)

    moneyAddresses[0].let { moneyAddress ->
      assertEquals("cashapp", moneyAddress.id)
      assertEquals("urn:btc:addr:fakeAddress", moneyAddress.urn.toString())
      assertEquals("btc", moneyAddress.currency)
      assertEquals("addr:fakeAddress", moneyAddress.css)
    }

    moneyAddresses[1].let { moneyAddress ->
      assertEquals("cashapp", moneyAddress.id)
      assertEquals("urn:btc:lnurl:http://fakeLnurl", moneyAddress.urn.toString())
      assertEquals("btc", moneyAddress.currency)
      assertEquals("lnurl:http://fakeLnurl", moneyAddress.css)
    }

    moneyAddresses[2].let { moneyAddress ->
      assertEquals("cashapp", moneyAddress.id)
      assertEquals("urn:btc:spaddr:fakeSPAddress", moneyAddress.urn.toString())
      assertEquals("btc", moneyAddress.currency)
      assertEquals("spaddr:fakeSPAddress", moneyAddress.css)
    }

  }

  @Test
  fun testDidToMoneyAddressInvalidServiceType() {
    assertThrows(InvalidMoneyAddressException::class.java) {
      val did = DidService("didpay", "not-maddr", listOf("urn:nid:nss"))
      did.toMoneyAddresses()
    }
  }

  @Test
  fun testDidToMoneyAddressInvalidUrn() {
    assertThrows(InvalidUrnException::class.java) {
      val did = DidService("didpay", "maddr", listOf("not-a-urn"))
      did.toMoneyAddresses()
    }
  }
}
