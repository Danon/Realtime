package ui;

import network.KeysState;

public interface IKeyStateNotifier {
    void sendCurrentMove(KeysState keysState, boolean left, boolean right);

    void sendTextMessage(String text);
}
