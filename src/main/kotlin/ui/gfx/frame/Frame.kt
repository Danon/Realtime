package ui.gfx.frame

class Frame(val name: String, val x: Int, val y: Int, val width: Int, val height: Int, val offsetX: Int, val offsetY: Int) {

    constructor(name: String, x: Int, y: Int, width: Int, height: Int) :
            this(name, x, y, width, height, 0, 0)

    constructor(name: String, x: Int, y: Int, width: Int, height: Int, offsetX: Int) :
            this(name, x, y, width, height, offsetX, 0)
}
