package gameplay.geometry

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class AngleTest {
    @Test
    fun test() {
        assertEquals(3.14, Angle(3.14).value, 0.001)
    }

    @Test
    fun shouldAdd() {
        // when
        val angle = Angle(3.14).add(Angle(6.28))

        // then
        assertEquals(9.42, angle.value, 0.001)
    }
}
