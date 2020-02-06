package gameplay.geometry

import gameplay.Point

interface Shape {
    fun contains(point: Point): Boolean
}
