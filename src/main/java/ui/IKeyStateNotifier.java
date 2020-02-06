package ui;

import network.KeysState;

public interface IKeyStateNotifier extends IChatOperator {
    void sendCurrentMove(KeysState keysState, boolean left, boolean right);
}
