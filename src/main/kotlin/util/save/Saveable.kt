package util.save

import java.io.IOException

interface Saveable {
    @Throws(IOException::class)
    fun restoreState(input: SaveInput)

    @Throws(IOException::class)
    fun storeState(output: SaveOutput)
}
