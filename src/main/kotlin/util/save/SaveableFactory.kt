package util.save

import java.io.IOException

interface SaveableFactory<T : Saveable> {
    @Throws(IOException::class)
    fun load(input: SaveInput): T

    @Throws(IOException::class)
    fun save(output: SaveOutput, saveable: T)
}
