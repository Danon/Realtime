package util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SizeTest {
    @Test
    fun test() {
        assertEquals(2, Size(2, 4).width)
        assertEquals(4, Size(2, 4).height)
    }
}
