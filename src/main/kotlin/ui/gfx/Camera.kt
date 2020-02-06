package ui.gfx

import gameplay.Point
import gameplay.geometry.Rectangle
import ui.gfx.Renderer.*

class Camera(private var point: Point) {

    constructor() : this(Point())
    constructor(x: Int, y: Int) : this(Point(x.toDouble(), y.toDouble()))

    fun cap(borders: Rectangle, windowWidth: Int, windowHeight: Int) {
        point = Point(
                point.capX(-CAMERA_SIDE_MARGIN.toDouble(), borders.width - windowWidth + CAMERA_SIDE_MARGIN),
                point.capY(-CAMERA_BOTTOM_MARGIN.toDouble(), borders.height - windowHeight + CAMERA_TOP_MARGIN)
        )
    }

    val x: Int
        get() = point.x.toInt()

    val y: Int
        get() = point.y.toInt()
}
