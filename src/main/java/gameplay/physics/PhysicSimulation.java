package gameplay.physics;

import gameplay.Character;
import gameplay.CharacterCommonState;
import gameplay.LadderCollide;
import gameplay.WalkDirection;
import gameplay.scene.GameMap;
import gameplay.scene.Ladder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ui.gfx.frame.FrameAnimation;
import util.Validate;

import static gameplay.physics.GameMapHelper.FloorIs.Above;
import static gameplay.physics.GameMapHelper.FloorIs.Below;
import static gameplay.physics.GameMapHelper.From.Left;
import static gameplay.physics.GameMapHelper.From.Right;

@RequiredArgsConstructor
public class PhysicSimulation {
    private final static double JUMP_VELOCITY = 4; //2.55;
    private final static double RUN_SPEED = 1.1;     // + (110px) per second
    private final static double CLIMB_SPEED = 0.8;     // + (60px) per second
    private final static double FALL_ACCELERATION = 0.07;    // + (7px/s) per second

    @Getter
    private final GameMap map;

    protected double getMapWidth() {
        return map.getWidth();
    }

    private Ladder detectLadder(Character character) {
        for (Ladder ladder : map.getLadders()) {
            if (Validate.between(character.getX(), ladder.getLeft(), ladder.getRight()))
                if (Validate.between(character.getY(), ladder.getBottom(), ladder.getPeek())) {
                    return ladder;
                }
        }
        return null;
    }

    private void capCharacterPosition(gameplay.Character character) {
        character.shared.x = Math.min(Math.max(CharacterCommonState.WIDTH / 2.0, character.shared.x), map.getWidth() - CharacterCommonState.WIDTH / 2.0);
        character.shared.y = Math.min(Math.max(0, character.shared.y), map.getHeight() - character.getHeight());
    }

    private void setLadderFor(gameplay.Character character, Ladder ladder) {
        if (ladder == null) {
            character.common.collideLadder = LadderCollide.None;
            character.common.climbingLadder = false;
            character.common.climbFrame = -1;
            return;
        }
        if (ladder.getBottom() == character.shared.y) {
            character.common.collideLadder = LadderCollide.Bottom;
            character.common.climbingLadder = false;
            character.common.climbFrame = -1;
        } else if (ladder.getPeek() == character.shared.y) {
            character.common.collideLadder = LadderCollide.Top;
            character.common.climbingLadder = false;
            character.common.climbFrame = -1;
        } else {
            character.common.collideLadder = LadderCollide.Middle;
            if (character.shared.keysState.Left != character.shared.keysState.Right) {
                character.common.collideLadder = LadderCollide.None;
                character.common.climbingLadder = false;
            }
        }
    }

    private void setClimbVariables(Character character, Ladder ladder) {
        if (!character.common.climbingLadder) {
            if (character.common.collideLadder == LadderCollide.Middle) {
                character.common.climbFrame = 0;
                character.common.climbingLadder = true;
                //   character.shared.x = ladder.getCenter();
            }

            // entering the ladder (from platform)
            if ((character.common.collideLadder == LadderCollide.Bottom && character.shared.keysState.Up) ||
                    (character.common.collideLadder == LadderCollide.Top && character.shared.keysState.Down)) {
                character.common.climbingLadder = true;
                character.common.collideLadder = LadderCollide.Middle;
                character.common.climbFrame = -1;
                character.shared.x = ladder.getCenter();
            }
        }
    }

    private void climbingLadderPhysics(gameplay.Character character, Ladder ladder) {
        character.common.onGround = false;
        character.common.jumpFrame = -1;
        character.common.basicFrame = -1;
        character.common.runFrame = -1;
        character.common.shootFrame = -1;

        // ladder
        character.shared.velocityY = 0;
        if (character.common.collideLadder == LadderCollide.Middle) {
            if (character.shared.keysState.Up != character.shared.keysState.Down) // is moving (one of the keysState is pressed)
            {
                if (character.shared.keysState.Up) {
                    double toPeek = ladder.getPeek() - character.shared.y;  // distance to the ladder peek
                    if (toPeek >= CLIMB_SPEED) {
                        character.shared.y += CLIMB_SPEED;
                    } else {
                        character.shared.y = ladder.getPeek();
                        character.common.collideLadder = LadderCollide.Top;
                        character.common.climbingLadder = false;
                        character.common.climbFrame = -1;
                    }
                }
                if (character.shared.keysState.Down) {
                    double toBottom = character.shared.y - ladder.getBottom();  // distance to the ladder bottom
                    if (toBottom >= CLIMB_SPEED) {
                        character.shared.y -= CLIMB_SPEED;
                    } else {
                        character.shared.y = ladder.getBottom();
                        character.common.collideLadder = LadderCollide.Bottom;
                        character.common.climbingLadder = false;
                        character.common.climbFrame = -1;
                    }
                }
                character.common.climbFrame++;
            }
            if (character.shared.keysState.Left != character.shared.keysState.Right) {
                character.common.climbFrame = -1;
                character.common.climbingLadder = false;
                if (character.shared.keysState.Up) {
                    character.shared.velocityY = JUMP_VELOCITY;
                    character.common.onGround = true;
                }
            }
        }
    }

    private void running(Character character) {
        if (character.common.onGround) {
            character.common.jumpFrame = -1;
            if (character.common.walking && !attacking(character)) {
                character.common.runFrame++;
                character.common.runFrame %= 10 * FrameAnimation.Speed.Run;
            } else {
                character.common.runFrame = -1;
            }
        } else {
            character.common.runFrame = -1;
        }
    }

    protected void perform(Character character) {
        this.capCharacterPosition(character);

        Ladder ladder = this.detectLadder(character);  // detecting the ladder

        this.setLadderFor(character, ladder);
        this.setClimbVariables(character, ladder);

        if (character.common.climbingLadder) {
            // TODO: climbingLadder shouldn't but can be true, even if ladder is null
            this.climbingLadderPhysics(character, ladder);
        } else {
            this.freeRunPhysics(character);
        }
    }

    private void freeRunPhysics(Character character) {
        if (character.common.walking = (character.shared.keysState.Left != character.shared.keysState.Right)) {
            character.shared.walkDirection = (character.shared.keysState.Right) ? WalkDirection.Right : WalkDirection.Left;
        }

        if (this.allowedWalkMovement(character)) {
            if (character.shared.keysState.Left) {
                character.shared.x -= Math.min(RUN_SPEED, new GameMapHelper(map, character).closestHorizontalObstacle(Left));
            }
            if (character.shared.keysState.Right) {
                character.shared.x += Math.min(RUN_SPEED, new GameMapHelper(map, character).closestHorizontalObstacle(Right));
            }
        } else {
            character.common.walking = false;
        }

        if (character.isGoingUpByVelocity()) {
            if (character.common.jumpFrame < 5 * FrameAnimation.Speed.MidAir)
                character.common.jumpFrame++;

            double characterOffsetToCeiling = new GameMapHelper(map, character).closestDistance(Above);
            if (characterOffsetToCeiling > character.shared.velocityY) {
                character.shared.y += character.shared.velocityY;
            } else {
                assert characterOffsetToCeiling > 0.0;
                character.shared.y += characterOffsetToCeiling;
                character.shared.velocityY = 0.0;
            }

            character.common.onGround = false;
        } else {
            double characterOffsetToGround = new GameMapHelper(map, character).closestDistance(Below);
            if (characterOffsetToGround > -character.shared.velocityY) {
                character.shared.y += character.shared.velocityY;
                character.common.onGround = false;
            } else if (characterOffsetToGround > 0.0) {
                character.shared.y -= characterOffsetToGround;
                characterOffsetToGround = 0.0;
            }

            if (characterOffsetToGround == 0.0) {
                character.common.onGround = true;
            }
            character.common.jumpFrame = 5 * FrameAnimation.Speed.MidAir;
        }

        character.shared.velocityY = (character.common.onGround) ? 0 : character.shared.velocityY - FALL_ACCELERATION;
        character.common.timeInAir = (character.common.onGround) ? 0 : character.common.timeInAir + 1;

        if (character.shared.leftClick) {
            character.common.runFrame = -1;
            character.common.basicFrame++;
            character.common.basicFrame %= 9 * FrameAnimation.Speed.Basic;
        } else {
            character.common.basicFrame = -1;
        }

        if (character.shared.rightClick) {
            character.common.shootFrame++;
            if (character.common.shootFrame == 9 * FrameAnimation.Speed.Shooting) {  // loop animation without first
                character.common.shootFrame = 2 * FrameAnimation.Speed.Shooting;
            }
        } else {
            character.common.shootFrame = -1;
        }

        this.running(character);

        if (character.shared.keysState.Up && allowedJump(character)) {
            character.shared.velocityY = JUMP_VELOCITY;
            character.common.onGround = false;
        }
    }

    private boolean attacking(gameplay.Character character) {
        return character.common.basicFrame > -1 || character.common.shootFrame > -1;
    }

    private boolean allowedJump(gameplay.Character character) {
        return character.common.onGround;
        // return (character.common.onGround || character.common.timeInAir < OFF_FLOOR_JUMP_MARG);
    }

    private boolean allowedWalkMovement(gameplay.Character character) {
        return !character.common.onGround || !attacking(character);
    }
}
