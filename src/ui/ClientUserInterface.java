package ui;


import gameplay.Character;
import gameplay.ClientWorld;
import ui.window.LobbyForm;
import ui.window.ProvideHostForm;
import util.Size;
import util.save.SaveManager;

/**
 * ClientUserInterface.java
 * <p>
 * Window displayed to user, than shows gameplay, and gets user input in the form
 * of keysState pushed in game or MessageBoxes and InputBoxes.
 *
 * @author Danio
 * @version 1.0 03/04/2015
 */
public class ClientUserInterface extends CustomUserInterface {
    // logic
    public final ClientWorld world;

    private final GameWindow gameWindow;

    ProvideHostForm hostProvideForm;
    LobbyForm lobbyForm;


    /**
     * Constructor
     *
     * @param ksnot      object that has a function which will be invoked after
     *                   keysState state has changed.
     * @param windowSize size of the window
     */
    public ClientUserInterface(IKeyStateNotifier ksnot, Size windowSize) {
        gameWindow = new GameWindow(ksnot, windowSize);
        this.world = new ClientWorld(gameWindow.getRenderObserver(), gameWindow);
        this.world.setMap(
                SaveManager.Map.load("Standard")
        );
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
        this.gameWindow.setMainPlayer(characterId);
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
