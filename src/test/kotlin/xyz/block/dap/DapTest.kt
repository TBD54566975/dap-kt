package xyz.block.dap

import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class DapTest {

  @Test
  fun testDapToString() {
    val dap = Dap("handle", "domain.com")
    assertEquals("@handle/domain.com'", dap.toString())
  }

  @Test
  fun testParseDap() {
    val dap = Dap.parse("@handle/domain.com")
    assertEquals("handle", dap.handle)
    assertEquals("domain.com", dap.domain)
  }

  @Test
  fun testParseValidDaps() {
    val validDaps = listOf(
      "@123/domain.com", // min length 3
      "@123456789012345678901234567890/domain.com", // max length 30
      "@a-b/domain.com",
      "@a_b/domain.com",
    )
    validDaps.forEach { dap ->
      Dap.parse(dap)
    }
  }

  @Test
  fun testParseInvalidDaps() {
    val invalidDaps = listOf(
      "",
      "a",
      "@handle",
      "@handle/",
      "@ha/domain.com", // handle too short
      "@1234567890123456789012345678901/domain.com", // handle too long
      "@handle@/domain.com",
      "@handle@handle/domain.com",
      "@handle//domain.com",
      "@handle/handle/domain.com",
      "@handle/@domain.com",
      "@handle/domain.com@",
      "@handle/domain.com/",
      "@handle/domain.com/extra-stuff",
    )
    for (dap in invalidDaps) {
      val exception = assertThrows<InvalidDapException>("expect [$dap] to be invalid") {
        Dap.parse(dap)
      }
      assertEquals("Invalid DAP", exception.message)
    }
  }
}
