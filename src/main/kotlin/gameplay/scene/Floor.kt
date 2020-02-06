package gameplay.scene

import java.awt.geom.Rectangle2D

class Floor(val left: Int, val top: Int, val tiles: Int) {
    val right: Int
        get() = left + tiles * 32

    val width: Int
        get() = tiles * 32

    val bottom: Int
        get() = top - HEIGHT

    fun asShape(): Rectangle2D {
        return Rectangle2D.Float(left.toFloat(), top.toFloat(), width.toFloat(), HEIGHT.toFloat())
    }

    companion object {
        const val HEIGHT = 32
    }
}
