import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DapTest {

    @Test
    fun testHehe() {
        val dap = Dap()
        assertTrue(dap.hehe(), "The hehe method should return true")
    }
}
