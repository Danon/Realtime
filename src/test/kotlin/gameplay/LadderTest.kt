package gameplay

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class LadderTest {
    @Test
    fun shouldGetCenter() {
        // given
        val ladder = Ladder(120, 60, 3)

        // then
        assertEquals(132, ladder.center)
    }

    @Test
    fun shouldGetRight() {
        // given
        val ladder = Ladder(120, 60, 3)

        // then
        assertEquals(144, ladder.right)
    }

    @Test
    fun shouldGetHeight() {
        // given
        val ladder = Ladder(120, 60, 3)

        // then
        assertEquals(96, ladder.height)
    }

    @Test
    fun shouldGetPeek() {
        // given
        val ladder = Ladder(120, 60, 3)

        // then
        assertEquals(156, ladder.peek)
    }
}
