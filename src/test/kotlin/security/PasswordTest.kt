package security

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PasswordTest {
    @Test
    fun shouldCompare() {
        // then
        assertTrue(Password("foo").compare(Password("foo")))
    }

    @Test
    fun shouldNotCompare() {
        // then
        assertFalse(Password("doo").compare(Password("foo")))
    }

    @Test
    fun shouldTestToString() {
        // given
        val password = Password("doo")

        // when
        val string = password.toString()

        // then
        assertEquals("6400cec37dcc239d0bf982fd6c72fb03c8a6b78f", string)
    }
}
