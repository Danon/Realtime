package util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import util.Bytes.toString

internal class BytesTest {
    @Test
    fun `should add angles`() {
        // given
        val bytes = byteArrayOf(123, 14, 123, 32)

        // when
        val string = toString(bytes)

        // then
        assertEquals("7b0e7b20", string)
    }
}
