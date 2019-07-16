package app;

import debug.decorator.DebugClientConnectionManager;
import gameplay.ClientWorld;
import network.ClientConnectionListener;
import network.ClientConnectionManager;
import network.Network.Command;
import ui.ClientUserInterface;
import ui.window.LobbyForm;
import ui.window.ProvideHostForm;
import ui.window.ServerLoginForm;
import util.LookAndFeel;
import util.Size;

public class GameClient implements ClientConnectionListener {
    private final ClientUserInterface userInterface;
    private final ClientConnectionManager client = new DebugClientConnectionManager();

    private LobbyForm lobby;
    private ProvideHostForm hostProvideForm;
    private ServerLoginForm serverLoginForm;
    private ClientWorld world;

    public static void main(String[] args) {
        LookAndFeel.setLookAndFeel();
        new GameClient().start();
    }

    GameClient() {
        userInterface = new ClientUserInterface(client, new Size(880, 750));
    }

    public void start() {
        client.openSocket(this);

        hostProvideForm = new ProvideHostForm(client);
        lobby = new LobbyForm(client);

        client.addHostObserver(hostProvideForm);
        client.addChatListener(lobby);
        serverLoginForm = new ServerLoginForm(client);

        hostProvideForm.setVisible(true);
    }

    @Override
    public void connected() {
        // client.updateReturnTripTime();   // ping
        serverLoginForm.setVisible(true);
    }

    @Override
    public void connectError() {
        userInterface.showInfo("Couldn't connect");
    }

    @Override
    public void disconnected() {
        userInterface.showInfo("Disconnected.");
        hostProvideForm.setVisible(true);
    }

    @Override
    public void messageRegistered(Command.Registered command) {
        userInterface.showInfo(String.format(
                "Created account \"%s\" successfully. You can now log in.", command.username));
    }

    @Override
    public void messageLoggedIn(Command.LoggedIn command) {
        lobby.setVisible(true);
        System.out.println(String.format("Logged in as \"%s\" (#%d)", command.username, command.userId));
    }

    @Override
    public void messageLoginRejected(Command.LoginRejected command) {
        userInterface.showInfo(command.reason);
        serverLoginForm.setVisible(true);
    }

    @Override
    public void messageMatchStarted(Command.MatchStarted command) {
        System.out.println("Match started.");
        world = userInterface.startGame(command.clientsCharacterId, command.characters);
    }

    @Override
    public void messageMatchAlreadyStarted(Command.MatchAlreadyStarted command) {
        userInterface.showInfo("Match has already started");
    }

    @Override
    public void messageLobbyTeamChanged(Command.LobbyTeamChanged command) {
        lobby.teamChanged(command.userId, command.previousTeamId, command.currentTeamId, command.readyForGame);
    }

    @Override
    public void messageLobbyWelcome(Command.LobbyWelcome command) {
        lobby.teamSet(command.teams);
    }

    @Override
    public void messageChatMessage(Command.ChatMessage command) {
        userInterface.incomingChatMessage(command.senderId, command.text);
    }

    @Override
    public void messageUpdateSharedState(Command.UpdateSharedState command) {
        world.updateCharacter(command);
    }

    @Override
    public void messageUserLeft(Command.UserLeft command) {
        world.removeCharacterById(command.userId);
        System.out.println(String.format("player #%d left the game", command.userId));
    }
}
