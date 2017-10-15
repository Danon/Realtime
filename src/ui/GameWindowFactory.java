package ui;

import gameplay.ClientWorld;
import util.Size;

class GameWindowFactory {
    private final IKeyStateNotifier keyStateNotifier;
    private final Size windowSize;
    private ClientWorld world;
    private Chat chat;

    GameWindowFactory(IKeyStateNotifier keyStateNotifier, Size windowSize) {
        this.keyStateNotifier = keyStateNotifier;
        this.windowSize = windowSize;
    }

    GameWindowFactory use(ClientWorld world) {
        this.world = world;
        return this;
    }

    GameWindowFactory use(Chat chat) {
        this.chat = chat;
        return this;
    }

    GameWindow showGameWindow() {
        GameWindow gameWindow = new GameWindow(keyStateNotifier, windowSize, world, chat);
        gameWindow.showGameWindow();
        return gameWindow;
    }
}
