package gameplay.physics

import gameplay.Character
import gameplay.physics.GameMapHelper.FloorIs.Above
import gameplay.physics.GameMapHelper.FloorIs.Below
import gameplay.physics.GameMapHelper.From.Left
import gameplay.physics.GameMapHelper.From.Right
import gameplay.scene.Floor
import gameplay.scene.GameMap
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Floor tile is 32x32, so for floor with top-left corner 30,30, its bottom-right corner is -2,-2
 * Character is 35 high, so for character position 0,0 its head is at 35,0
 * Character is 22 wide, so for character position 0,0, its left and right boundaries are at -11 and 11,
 */
internal class GameMapHelperTest {
    @Test
    fun `should return maximum distance`() {
        // given
        val helper = helper(emptyList())

        // when
        assertEquals(123.0, helper.closestHorizontalObstacle(Left))
        assertEquals(456.0, helper.closestDistance(Above))
    }

    @Test
    fun `should find vertical above`() {
        // given
        val height = 35 + 32

        // when
        assertEquals(3.0, helperWithTops(tops = listOf(height + 5, height + 3, height - 1)).closestDistance(Above))
    }

    @Test
    fun `should find vertical below`() {
        // when
        assertEquals(3.0, helperWithTops(tops = listOf(-5, -3, 1)).closestDistance(Below))
    }

    @Test
    fun `should find vertical left`() {
        // given
        val width = -32 - 11

        // when
        assertEquals(3.0, helperWithLefts(lefts = listOf(width - 5, width - 3, width + 1)).closestHorizontalObstacle(Left))
    }

    @Test
    fun `should find vertical right`() {
        // given
        val width = 11

        // when
        assertEquals(3.0, helperWithLefts(lefts = listOf(width + 5, width + 3, width - 1)).closestHorizontalObstacle(Right))
    }

    @Test
    fun `should not find vertical in opposite direction (above)`() {
        // when
        assertEquals(456.0, helperWithTops(tops = listOf(-10, -5)).closestDistance(Above)) // optional not present
    }

    @Test
    fun `should not find vertical in opposite direction (below)`() {
        // when
        assertEquals(456.0, helperWithTops(tops = listOf(40, 70)).closestDistance(Below)) // optional not present
    }

    @Test
    fun `should not find vertical in opposite direction (left)`() {
        // given
        val width = 11

        // when
        assertEquals(123.0, helperWithLefts(lefts = listOf(width + 1)).closestHorizontalObstacle(Left)) // optional not present
    }

    @Test
    fun `should not find vertical in opposite direction (right)`() {
        // given
        val width = 11

        // when
        assertEquals(123.0, helperWithLefts(lefts = listOf(-width - 1)).closestHorizontalObstacle(Right)) // optional not present
    }

    private fun helperWithTops(tops: List<Int>): GameMapHelper {
        return helper(tops.map { Floor(0, it, 1) })
    }

    private fun helperWithLefts(lefts: List<Int>): GameMapHelper {
        return helper(lefts.map { Floor(it, 16, 1) })
    }

    private fun helper(floors: List<Floor>): GameMapHelper {
        return GameMapHelper(
                GameMap("", 123, 456, floors, emptyList()),
                Character(0))
    }
}
