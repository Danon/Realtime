package gameplay.scene

class Ladder(val left: Int, val bottom: Int, val tiles: Int) {
    val center: Int
        get() = left + WIDTH / 2

    val right: Int
        get() = left + WIDTH

    val height: Int
        get() = tiles * 32

    val peek: Int
        get() = bottom + tiles * 32

    companion object {
        private const val WIDTH = 24
    }
}
