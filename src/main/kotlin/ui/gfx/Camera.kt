package ui.gfx

import gameplay.Point
import gameplay.geometry.Rectangle

class Camera(private var point: Point) {

    constructor() : this(Point())
    constructor(x: Int, y: Int) : this(Point(x.toDouble(), y.toDouble()))

    fun cap(borders: Rectangle, windowWidth: Int, windowHeight: Int) {
        point = Point(
                point.capX(-Renderer.CAMERA_SIDE_MARGIN.toDouble(), borders.width - windowWidth + Renderer.CAMERA_SIDE_MARGIN),
                point.capY(-Renderer.CAMERA_BOTTOM_MARGIN.toDouble(), borders.height - windowHeight + Renderer.CAMERA_TOP_MARGIN)
        )
    }

    val x: Int
        get() = point.x.toInt()

    val y: Int
        get() = point.y.toInt()
}
