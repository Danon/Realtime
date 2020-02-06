package gameplay;

public class CharacterCommonState {
    public final static int WIDTH = 23;
    public final static int HEIGHT = 35;

    public boolean walking = false;
    public boolean climbingLadder = false;
    public boolean onLadderTop = false;
    public boolean disableLadderTopCollision = false;
    public boolean onGround = false;

    public int timeInAir = 0;
    public int jumpFrame = -1;
    public int basicFrame = -1;
    public int shootFrame = -1;
    public int runFrame = -1;
    public int climbFrame = -1;

    public LadderCollide collideLadder = LadderCollide.None;

    public CharacterCommonState copy() {
        CharacterCommonState deepCopy = new CharacterCommonState();
        deepCopy.collideLadder = this.collideLadder;
        deepCopy.walking = this.walking;
        deepCopy.climbingLadder = this.climbingLadder;
        deepCopy.onLadderTop = this.onLadderTop;
        deepCopy.disableLadderTopCollision = this.disableLadderTopCollision;
        deepCopy.onGround = this.onGround;
        deepCopy.timeInAir = this.timeInAir;
        deepCopy.jumpFrame = this.jumpFrame;
        deepCopy.basicFrame = this.basicFrame;
        deepCopy.shootFrame = this.shootFrame;
        deepCopy.runFrame = this.runFrame;
        deepCopy.climbFrame = this.climbFrame;
        return deepCopy;
    }
}
