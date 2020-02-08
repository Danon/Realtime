package util.save

import java.io.IOException

interface Savable {
    @Throws(IOException::class)
    fun restoreState(input: SaveInput)

    @Throws(IOException::class)
    fun storeState(output: SaveOutput)

    @Throws(IOException::class)
    fun factory(input: SaveInput): SaveableFactory
}
