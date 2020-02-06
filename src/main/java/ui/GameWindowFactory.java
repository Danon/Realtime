package ui;

import gameplay.ClientWorld;
import lombok.RequiredArgsConstructor;
import ui.gfx.Renderer;
import util.Size;

import static ui.GameWindow.VIEW_HEIGHT;
import static ui.GameWindow.VIEW_WIDTH;

@RequiredArgsConstructor
class GameWindowFactory {
    private final IKeyStateNotifier keyStateNotifier;
    private final Size windowSize;

    GameWindow showGameWindow(ClientWorld world, Chat chat) {
        Renderer renderer = new Renderer(new Size(VIEW_WIDTH, VIEW_HEIGHT));
        renderer.attachWorld(world);
        renderer.attachChat(chat);
        GameWindow gameWindow = new GameWindow(keyStateNotifier, windowSize, world, chat, renderer);
        gameWindow.showGameWindow();
        return gameWindow;
    }
}
