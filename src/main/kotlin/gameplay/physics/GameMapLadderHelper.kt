package gameplay.physics

import gameplay.Character
import gameplay.scene.GameMap
import gameplay.scene.Ladder
import util.Validate

class GameMapLadderHelper(private val map: GameMap) {
    fun detectLadder(character: Character): Ladder? {
        for (ladder in map.ladders) {
            if (Validate.between(character.x, ladder.left.toDouble(), ladder.right.toDouble())) {
                if (Validate.between(character.y, ladder.bottom.toDouble(), ladder.peek.toDouble())) {
                    return ladder
                }
            }
        }
        return null
    }
}
