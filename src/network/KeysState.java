package network;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof KeysState)) return false;
        KeysState k = (KeysState) o;
        return (k.Up == Up && k.Down == Down && k.Left == Left && k.Right == Right);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (this.Up ? 1 : 0);
        hash = 83 * hash + (this.Down ? 1 : 0);
        hash = 83 * hash + (this.Left ? 1 : 0);
        hash = 83 * hash + (this.Right ? 1 : 0);
        return hash;
    }
}
