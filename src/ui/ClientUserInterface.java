package ui;


import gameplay.Character;
import gameplay.ClientWorld;
import ui.window.LobbyForm;
import ui.window.ProvideHostForm;
import util.Size;
import util.save.SaveManager;

public class ClientUserInterface extends CustomUserInterface {
    public final ClientWorld world;
    private final GameWindow gameWindow;

    private ProvideHostForm hostProvideForm;
    private LobbyForm lobbyForm;
    private Chat chat;

    private int ping;

    public ClientUserInterface(IKeyStateNotifier keyStateNotifier, Size windowSize) {
        gameWindow = new GameWindow(keyStateNotifier, windowSize);
        world = new ClientWorld(gameWindow.getRenderObserver(), gameWindow);
        world.setMap(SaveManager.Map.load("Standard"));
        chat = new Chat();
        gameWindow.attachWorld(this.world);
        gameWindow.attachChat(this.chat);
    }

    public void startGame(Character[] characters) {
        for (Character character : characters) {
            world.addCharacter(character);
        }
        gameWindow.showGameWindow();
        world.startLoop();
    }

    public IHostObserver createHostObserver(IHostOperator operator) {
        hostProvideForm = new ProvideHostForm(operator);
        return hostProvideForm;
    }

    public ILobbyObserver createLobbyObserver(ILobbyOperator operator) {
        lobbyForm = new LobbyForm(operator);
        return lobbyForm;
    }

    public void open() {
        hostProvideForm.setVisible(true);
    }

    public void setMainPlayerLoggedIn(int characterId) {
        this.world.setMainPlayerId(characterId);
    }

    public void openLobby() {
        lobbyForm.setVisible(true);
    }

    public void incomingChatMessage(int senderId, String text) {
        chat.addMessage(senderId + ": " + text.trim());
        chat.setLast5ForEasyRetrieval();
    }

    public void pingTime(int ms) {
        ping = ms;
    }
}
