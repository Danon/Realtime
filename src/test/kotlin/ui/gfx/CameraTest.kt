package ui.gfx

import gameplay.Point
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CameraTest {
    @Test
    fun test() {
        // given
        val camera = Camera(14, 15)

        // when + then
        assertEquals(14, camera.x)
        assertEquals(15, camera.y)
    }

    @Test
    fun `test constructor Point`() {
        // given
        val point = Point(14.0, 15.0)
        val camera = Camera(point)

        // when + then
        assertEquals(14, camera.x)
        assertEquals(15, camera.y)
    }

    @Test
    fun `should cap`() {
        // given
        // TODO write test

        // when

        // then
    }
}
