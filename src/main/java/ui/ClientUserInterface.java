package ui;

import gameplay.Character;
import gameplay.ClientWorld;
import gameplay.GameMap;
import util.Size;
import util.save.SaveManager;

public class ClientUserInterface extends CustomUserInterface {
    private final GameWindowFactory gameWindowFactory;
    private final Chat chat = new Chat();

    public ClientUserInterface(IKeyStateNotifier keyStateNotifier, Size windowSize) {
        gameWindowFactory = new GameWindowFactory(keyStateNotifier, windowSize);
    }

    public ClientWorld startGame(int characterId, Character[] characters) {
        GameMap map = SaveManager.Map.load("Standard");

        ClientWorld world = new ClientWorld(map, characterId, characters);

        GameWindow window = gameWindowFactory.use(world).use(this.chat).showGameWindow();

        world.addRenderObserver(window.getRenderObserver());
        world.addUpdateObserver(window);

        world.startLoop();

        return world;
    }

    public void incomingChatMessage(int senderId, String text) {
        chat.addMessage(senderId + ": " + text.trim());
        chat.setLast5ForEasyRetrieval();
    }
}
