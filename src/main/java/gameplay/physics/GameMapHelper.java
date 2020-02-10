package gameplay.physics;

import gameplay.scene.Floor;
import gameplay.scene.GameMap;
import lombok.RequiredArgsConstructor;
import util.Validate;

@RequiredArgsConstructor
public class GameMapHelper {
    private final GameMap map;
    private final gameplay.Character character;

    enum FloorIs {Below, Above}

    enum From {Left, Right}

    double closestDistance(FloorIs floorIs) {
        double minDistance = map.getHeight();  // largest possible value

        for (Floor floor : map.getFloors()) {
            if ((floor.getLeft() <= character.getRightSideX()) && (character.getLeftSideX() <= floor.getRight())) {
                double off = (floorIs == FloorIs.Below)
                        ? character.getY() - floor.getTop()
                        : floor.getBottom() - character.getHeadY();

                if (off == 0.0) {
                    return 0;
                }
                if (off > 0.0) {
                    if (off < minDistance) {
                        minDistance = off;
                    }
                }
            }
        }
        return minDistance;
    }

    double closestHorizontalObstacle(From from) {

        double minDistance = map.getWidth();  // largest possible value

        for (Floor floor : map.getFloors()) {
            if (Validate.inside(floor.getBottom(), character.getFeetY() - Floor.HEIGHT, character.getHeadY())) {
                double dist = (from == From.Right)
                        ? floor.getLeft() - character.getRightSideX()
                        : character.getLeftSideX() - floor.getRight();

                if (dist == 0) {
                    return 0;
                }
                if (dist > 0) { // only change distance if Floor is in the right direction (negative values are opposite direction)
                    minDistance = Math.min(minDistance, dist);
                }
            }
        }
        return minDistance;
    }
}
