package xyz.block.maddr.urn

import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class UrnTest {

  @Test
  fun testUrnParsing() {
    val urn = Urn.parse("urn:nid:nss")
    assertEquals("urn:nid:nss", urn.toString())
    assertEquals("nid", urn.nid)
    assertEquals("nss", urn.nss)
  }

  @Test
  fun testUrnParsingWithNssParts() {
    val urn = Urn.parse("urn:nid:nss1:nss2:nss3")
    assertEquals("urn:nid:nss1:nss2:nss3", urn.toString())
    assertEquals("nid", urn.nid)
    assertEquals("nss1:nss2:nss3", urn.nss)
  }

  @Test
  fun testParseInvalidUrn() {
    val invalidUrns = listOf(
      "",
      "invalid:nid:nss",
      "invalid::nss",
      "invalid:nid:",
      "invalid:nid",
      "invalid:",
      "invalid::",
    )
    for (urn in invalidUrns) {
      val exception = assertThrows<InvalidUrnException> {
        Urn.parse(urn)
      }
      assertEquals("Invalid URN", exception.message)
    }
  }
}