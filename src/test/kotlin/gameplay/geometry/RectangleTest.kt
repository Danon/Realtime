package gameplay.geometry

import gameplay.Point
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class RectangleTest {
    @Test
    fun testCast() {
        // given
        val rectangle = Rectangle(1.25, 2.25, 4.25, 5.25)

        // then
        assertEquals(1.25, rectangle.x, 0.00001)
        assertEquals(2.25, rectangle.y, 0.00001)
        assertEquals(4.25, rectangle.width, 0.00001)
        assertEquals(5.25, rectangle.height, 0.00001)
    }

    @Test
    fun `should contain point`() {
        // given
        val rectangle = Rectangle(-1.0, -2.0, 3.0, 4.0)
        val point = Point(1.0, 1.0)

        // when
        val contains = rectangle.contains(point)

        // then
        assertTrue(contains)
    }

    @Test
    fun `should not contain point`() {
        // given
        val rectangle = Rectangle(-1.0, -2.0, 3.0, 4.0)
        val point = Point(1.0, 5.0)

        // when
        val contains = rectangle.contains(point)

        // then
        assertFalse(contains)
    }
}
