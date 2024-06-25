package xyz.block.moneyaddress.urn

import org.junit.jupiter.api.assertThrows
import xyz.block.dap.InvalidDapException
import kotlin.test.Test
import kotlin.test.assertEquals

class DapUrnTest {

  @Test
  fun testDapUrnParsing() {
    val urn = DapUrn.parse("urn:currency:protocol:pss")
    assertEquals("urn:currency:protocol:pss", urn.toString())
    assertEquals("currency", urn.currency)
    assertEquals("protocol", urn.protocol)
    assertEquals("pss", urn.pss)
  }

  @Test
  fun testUrnParsingWithPssParts() {
    val urn = DapUrn.parse("urn:currency:protocol:pss1:pss2")
    assertEquals("urn:currency:protocol:pss1:pss2", urn.toString())
    assertEquals("currency", urn.currency)
    assertEquals("protocol", urn.protocol)
    assertEquals("pss1:pss2", urn.pss)
  }

  /*
  @Test
  fun testParseInvalidDapUrn() {
    val invalidDapUrns = listOf(
      "urn:currency::pss",
      "urn:currency:protocol:",
      "invalid:currency:protocol:pss",
    )
    for (urn in invalidDapUrns) {
      val exception = assertThrows<InvalidDapUrnException>("expect [$urn] to be invalid") {
        DapUrn.parse(urn)
      }
      assertEquals("Invalid DAP URN", exception.message)
    }
  }

  @Test
  fun testParseInvalidUrn() {
    val invalidUrns = listOf(
      "",
      "urn:",
      "urn::",
      "urn:::",
      "urn::protocol:pss",
      "invalid:currency:protocol:pss",
    )
    for (urn in invalidUrns) {
      val exception = assertThrows<InvalidUrnException>("expect [$urn] to be invalid") {
        DapUrn.parse(urn)
      }
      assertEquals("Invalid URN", exception.message)
    }
  }
   */
}
