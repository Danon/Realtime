package gameplay;

import network.KeysState;

final public class CharacterSharedState {
    private int characterId;

    public int hp;
    double x, y, velocityX, velocityY;
    WalkDirection walkDirection = WalkDirection.Right;

    public KeysState keysState = new KeysState();
    boolean leftClick, rightClick;

    public CharacterSharedState() {
    }

    CharacterSharedState(int characterId) {
        this.characterId = characterId;
    }

    private CharacterSharedState(CharacterSharedState base) {
        setValues(base);
    }

    void setValues(CharacterSharedState base) {
        this.characterId = base.characterId;
        this.x = base.x;
        this.y = base.y;
        this.hp = base.hp;
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

    int getCharacterId() {
        return this.characterId;
    }
}
