package gameplay

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class LadderTest {
    @Test
    fun `should get center`() {
        // given
        val ladder = Ladder(120, 60, 3)

        // then
        assertEquals(132, ladder.center)
    }

    @Test
    fun `should get right`() {
        // given
        val ladder = Ladder(120, 60, 3)

        // then
        assertEquals(144, ladder.right)
    }

    @Test
    fun `should get height`() {
        // given
        val ladder = Ladder(120, 60, 3)

        // then
        assertEquals(96, ladder.height)
    }

    @Test
    fun `should get peek`() {
        // given
        val ladder = Ladder(120, 60, 3)

        // then
        assertEquals(156, ladder.peek)
    }
}
