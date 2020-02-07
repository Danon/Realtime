package gameplay.geometry

import gameplay.Point
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PointTest {
    @Test
    fun `should cap X`() {
        assertEquals(0.0, Point(-0.5, -1.0).capX(0.0, 2.0), 0.001)
        assertEquals(1.0, Point(1.0, -1.0).capX(0.0, 2.0), 0.001)
        assertEquals(2.0, Point(2.5, -1.0).capX(0.0, 2.0), 0.001)
    }

    @Test
    fun `should cap Y`() {
        assertEquals(0.0, Point(-1.0, -0.5).capY(0.0, 2.0), 0.001)
        assertEquals(1.0, Point(-1.0, 1.0).capY(0.0, 2.0), 0.001)
        assertEquals(2.0, Point(-1.0, 2.5).capY(0.0, 2.0), 0.001)
    }

    @Test
    fun `should substitute point`() {
        // given
        val a = Point(1.0, 2.0)

        // when
        val new = a.sub(30.0, 50.0)

        // then
        assertEquals(-29.0, new.x, 0.001)
        assertEquals(-48.0, new.y, 0.001)
    }

    @Test
    fun `should add point (Point)`() {
        // when
        val new = Point(1.0, 2.0).add(Point(30.0, 50.0))

        // then
        assertEquals(31.0, new.x, 0.001)
        assertEquals(52.0, new.y, 0.001)
    }

    @Test
    fun `should add point (x,y)`() {
        // when
        val new = Point(1.0, 2.0).add(30.0, 50.0)

        // then
        assertEquals(31.0, new.x, 0.001)
        assertEquals(52.0, new.y, 0.001)
    }

    @Test
    fun `should add point Y`() {
        // when
        val new = Point(1.0, 2.0).addY(50.0)

        // then
        assertEquals(1.0, new.x, 0.001)
        assertEquals(52.0, new.y, 0.001)
    }
}
