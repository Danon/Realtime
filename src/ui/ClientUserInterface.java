package ui;


import gameplay.Character;
import gameplay.ClientWorld;
import ui.window.LobbyForm;
import ui.window.ProvideHostForm;
import util.Size;
import util.save.SaveManager;

public class ClientUserInterface extends CustomUserInterface {
    // logic
    public final ClientWorld world;
    private final GameWindow gameWindow;

    private ProvideHostForm hostProvideForm;
    private LobbyForm lobbyForm;

    public ClientUserInterface(IKeyStateNotifier keyStateNotifier, Size windowSize) {
        gameWindow = new GameWindow(keyStateNotifier, windowSize);
        world = new ClientWorld(gameWindow.getRenderObserver(), gameWindow);
        world.setMap(SaveManager.Map.load("Standard"));
        gameWindow.attachWorld(this.world);
    }

    public void startGame(Character[] characters) {
        for (Character character : characters) {
            world.addCharacter(character);
        }
        gameWindow.showGameWindow();
        world.startLoop();
    }

    public IWorldUpdateObserver getWorldUpdateObserver() {
        return this.gameWindow;
    }

    public IHostObserver createHostObserver(IHostOperator operator) {
        hostProvideForm = new ProvideHostForm(operator);
        return hostProvideForm;
    }

    public ILobbyObserver createLobbyObserver(ILobbyOperator operator) {
        lobbyForm = new LobbyForm(operator);
        return lobbyForm;
    }

    /**
     * Opens an interface for a user to probide a hostname.
     */
    public void open() {
        hostProvideForm.setVisible(true);
    }

    /**
     * Sets a main player, which will be indicated during game.
     *
     * @param characterId Character to be marked as main.
     */
    public void setMainPlayerLoggedIn(int characterId) {
        this.world.setMainPlayerId(characterId);
    }

    /**
     * Opens lobby, where player can chose teams.
     */
    public void openLobby() {
        lobbyForm.setVisible(true);
    }

    /**
     * Invoked by higher class, to put a chat mesage.
     *
     * @param senderId id of a character that send the message
     * @param text     text of the message
     */
    public void incomingChatMessage(int senderId, String text) {
        Chat.addMessage(text);
        Chat.setLast5ForEasyRetrieval();
    }

    /**
     * Sets the displayable ping
     */
    int ping;

    public void pingTime(int ms) {
        ping = ms;
    }

}
