package gameplay;

import lombok.Getter;
import lombok.NoArgsConstructor;
import network.KeysState;

@NoArgsConstructor
public class CharacterSharedState {
    @Getter
    private int characterId;

    public int hp;
    public double x;
    public double y;
    public double velocityX;
    public double velocityY;
    public WalkDirection walkDirection = WalkDirection.Right;

    public KeysState keysState = new KeysState();
    public boolean leftClick;
    public boolean rightClick;

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
