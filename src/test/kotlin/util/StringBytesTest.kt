package util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class StringBytesTest {
    @Test
    fun `should render bytes as hex string`() {
        // given
        val bytes = byteArrayOf(123, 14, 123, 32)

        // when
        val string = StringBytes.toString(bytes)

        // then
        assertEquals("7b0e7b20", string)
    }
}
