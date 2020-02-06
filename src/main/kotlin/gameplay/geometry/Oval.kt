package gameplay.geometry

import gameplay.Point
import kotlin.math.pow

class Oval(private val x: Double,private val y: Double, private val radiusX: Double, private val radiusY: Double) : Shape {

    constructor(o: Oval) : this(o.x, o.y, o.radiusX, o.radiusY)
    constructor(pos: Point, radiusX: Int, radiusY: Int) : this(pos.x, pos.y, radiusX.toDouble(), radiusY.toDouble())

    val center: Point
        get() = Point(x, y)

    fun getX(): Int {
        return x.toInt()
    }

    fun getY(): Int {
        return y.toInt()
    }

    fun getRadiusX(): Int {
        return radiusX.toInt()
    }

    fun getRadiusY(): Int {
        return radiusY.toInt()
    }

    override operator fun contains(point: Point): Boolean {
        return (point.x - x).pow(2.0) / radiusX.pow(2.0) +
                (point.y - y).pow(2.0) / radiusY.pow(2.0) <= 1
    }
}
