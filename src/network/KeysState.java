package network;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class KeysState {
    public boolean Up, Down, Left, Right;

    public void set(KeysState k) {
        Up = k.Up;
        Down = k.Down;
        Left = k.Left;
        Right = k.Right;
    }

    public KeysState copy() {
        KeysState keysState = new KeysState();
        keysState.set(this);
        return keysState;
    }
}
