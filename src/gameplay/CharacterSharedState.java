package gameplay;

import lombok.Getter;
import network.KeysState;

public class CharacterSharedState {
    @Getter
    private int characterId;

    public int hp;
    double x;
    double y;
    double velocityX;
    double velocityY;
    WalkDirection walkDirection = WalkDirection.Right;

    public KeysState keysState = new KeysState();
    boolean leftClick;
    boolean rightClick;

    CharacterSharedState(int characterId) {
        this.characterId = characterId;
    }

    private CharacterSharedState(CharacterSharedState base) {
        setValues(base);
    }

    void setValues(CharacterSharedState base) {
        this.characterId = base.characterId;
        this.hp = base.hp;
        this.x = base.x;
        this.y = base.y;
        this.walkDirection = base.walkDirection;
        this.leftClick = base.leftClick;
        this.rightClick = base.rightClick;
        this.velocityX = base.velocityX;
        this.velocityY = base.velocityY;
        this.keysState.set(base.keysState);
    }

    public CharacterSharedState copy() {
        return new CharacterSharedState(this);
    }
}
