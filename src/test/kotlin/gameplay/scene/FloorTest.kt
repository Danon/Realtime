package gameplay.scene

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class FloorTest {
    @Test
    fun shouldGetRight() {
        // given
        val floor = Floor(30, 50, 4)

        // when
        val right = floor.right

        // then
        assertEquals(158, right)
    }

    @Test
    fun shouldGetWidth() {
        // given
        val floor = Floor(30, 50, 4)

        // when
        val right = floor.width

        // then
        assertEquals(128, right)
    }

    @Test
    fun shouldGetBottom() {
        // given
        val floor = Floor(30, 50, 4)

        // when
        val right = floor.bottom

        // then
        assertEquals(18, right)
    }
}
