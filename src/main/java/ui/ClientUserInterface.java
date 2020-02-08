package ui;

import gameplay.Character;
import gameplay.ClientWorld;
import gameplay.scene.GameMap;
import lombok.RequiredArgsConstructor;
import ui.gfx.Renderer;
import util.Size;
import util.save.SaveManager;

import static ui.GameWindow.VIEW_HEIGHT;
import static ui.GameWindow.VIEW_WIDTH;

@RequiredArgsConstructor
public class ClientUserInterface extends CustomUserInterface {
    private final IKeyStateNotifier keyStateNotifier;
    private final Size windowSize;
    private final Chat chat = new Chat();

    public ClientWorld startGame(int characterId, Character[] characters) {
        GameMap map = SaveManager.Map.load("Standard");

        ClientWorld world = new ClientWorld(map, characterId, characters);
        Renderer renderer = new Renderer(new Size(VIEW_WIDTH, VIEW_HEIGHT));
        renderer.attachWorld(world);
        renderer.attachChat(chat);
        GameWindow window = new GameWindow(keyStateNotifier, windowSize, world, chat, renderer);
        world.addRenderObserver(renderer);
        world.addUpdateObserver(window);

        window.showGameWindow();
        world.startLoop();

        return world;
    }

    public void incomingChatMessage(int senderId, String text) {
        chat.addMessage(senderId + ": " + text.trim());
        chat.setLast5ForEasyRetrieval();
    }
}
