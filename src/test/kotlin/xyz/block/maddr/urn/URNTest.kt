package xyz.block.maddr.urn

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class URNTest {
  @Test
  fun testUrnParsing() {
    val urn = URN.parse("urn:nid:nss")
    assertEquals("urn:nid:nss", urn.toString())
    assertEquals("nid", urn.nid)
    assertEquals("nss", urn.nss)
  }

  @Test
  fun testUrnParsingWithNssParts() {
    val urn = URN.parse("urn:nid:nss1:nss2:nss3")
    assertEquals("urn:nid:nss1:nss2:nss3", urn.toString())
    assertEquals("nid", urn.nid)
    assertEquals("nss1:nss2:nss3", urn.nss)
  }

  @Test
  fun testUrnParsingInvalidPrefix()  {
    assertThrows(InvalidUrnException::class.java) {
      URN.parse("invalid:nid:nss")
    }
  }

  @Test
  fun testUrnParsingEmptyNss() {
    assertThrows(InvalidUrnException::class.java) {
      URN.parse("urn:nid:")
    }
  }

  @Test
  fun testUrnParsingMissingNss() {
    assertThrows(InvalidUrnException::class.java) {
      URN.parse("urn:nid")
    }
  }

  @Test
  fun testUrnParsingMissingNid() {
    assertThrows(InvalidUrnException::class.java) {
      URN.parse("uri:")
    }
  }
}