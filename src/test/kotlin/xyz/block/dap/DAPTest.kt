package xyz.block.dap

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class DAPTest {

  @Test
  fun testDapToString() {
    val dap = DAP("handle", "domain.com")
    assertEquals("@handle/domain.com'", dap.toString())
  }

  @Test
  fun testParseDap() {
    val dap = DAP.parse("@handle/domain.com")
    assertEquals("handle", dap.handle)
    assertEquals("domain.com", dap.domain)
  }

  @Test
  fun testParseInvalidDaps() {
    val invalidDaps = listOf(
      "",
      "a",
      "@handle",
      "@handle/",
      "@handle@/domain.com",
      "@handle//domain.com",
      "@handle/@domain.com",
      "@handle/domain.com@",
      "@handle/domain.com/",
    )
    for (dap in invalidDaps) {
      val exception = assertThrows(DAPParserException::class.java) {
        DAP.parse(dap)
      }
      assertEquals("Invalid DAP: $dap", exception.message)
      assertEquals("DAPParserException(invalidValue=$dap)", exception.toString())
    }
  }
}
