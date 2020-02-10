package gameplay.physics

import gameplay.Character
import gameplay.scene.Floor
import gameplay.scene.GameMap

class GameMapHelper(val map: GameMap, val character: Character) {
    fun closest(from: From): Double {
        return map.floors
                .stream()
                .filter { from.overlaps(character, it) }
                .mapToDouble { from.distance(character, it) }
                .filter { it >= 0.0 }
                .min()
                .orElseGet { from.max(map).toDouble() }
    }

    enum class From(val overlaps: (Character, Floor) -> Boolean, val max: (GameMap) -> Int, val distance: (Character, Floor) -> Double) {

        Left(Overlaps::horizontally, GameMap::width, { character, floor -> character.leftSideX - floor.right }),
        Right(Overlaps::horizontally, GameMap::width, { character, floor -> -(character.rightSideX - floor.left) }),
        Below(Overlaps::vertically, GameMap::height, { character, floor -> character.y - floor.top }),
        Above(Overlaps::vertically, GameMap::height, { character, floor -> -(character.headY - floor.bottom) });
    }

    companion object Overlaps {
        fun vertically(character: Character, floor: Floor): Boolean {
            return character.rightSideX >= floor.left && floor.right >= character.leftSideX
        }

        fun horizontally(character: Character, floor: Floor): Boolean {
            return character.headY > floor.bottom && floor.top > character.feetY
        }
    }
}
