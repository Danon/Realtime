package gameplay.geometry

import gameplay.Point

class Rectangle(val x: Double,
                val y: Double,
                var width: Double,
                val height: Double) : Shape {

    constructor(p: Point, width: Double, height: Double) : this(p.x, p.y, width, height)
    constructor(r: Rectangle) : this(r.x, r.y, r.width, r.height)

    val intWidth: Int
        get() = width.toInt()

    val intHeight: Int
        get() = height.toInt()

    val intX: Int
        get() = x.toInt()

    val intY: Int
        get() = y.toInt()

    override fun contains(point: Point): Boolean {
        return x <= point.x && y <= point.y && x + width >= point.x && y + height >= point.y
    }
}
