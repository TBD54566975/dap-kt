package xyz.block.dap

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DAPTest {

  @Test
  fun testDapStringRepresentation() {
    val dap = DAP("handle", "domain.com")
    assertEquals("@handle/domain.com'", dap.toString())
  }
}
