package gameplay.scene

import gameplay.GameMapSaveableFactory
import util.save.Saveable

class GameMap(val name: String, val width: Int, val height: Int, val floors: List<Floor>, val ladders: List<Ladder>) : Saveable {
    companion object Factory : GameMapSaveableFactory()
}
