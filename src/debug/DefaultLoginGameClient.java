package debug;

import debug.decorator.DebugClientConnectionManager;
import gameplay.ClientWorld;
import network.ClientConnectionListener;
import network.ClientConnectionManager;
import network.Network.Command;
import ui.ClientUserInterface;
import util.Size;

public class DefaultLoginGameClient implements ClientConnectionListener {
    private final ClientConnectionManager client;
    private final ClientUserInterface userInterface;
    private final String username, password;

    private ClientWorld world = null;

    public DefaultLoginGameClient(String username, String password) {
        this.client = new DebugClientConnectionManager();
        this.username = username;
        this.password = password;
        this.userInterface = new ClientUserInterface(client, new Size(800, 600));
    }

    public void start() {
        client.openSocket(this);
        client.connectToHost(InetAddressHelper.getLocalHost());
    }

    @Override
    public void connected() {
        client.loginToHost(username, password);
    }

    @Override
    public void connectError() {
        userInterface.showInfo("Couldn't connect");
    }

    @Override
    public void disconnected() {
        userInterface.showInfo("Disconnected.");
    }

    @Override
    public void messageRegistered(Command.Registered command) {
        userInterface.showInfo(String.format(
                "Created account \"%s\" successfully. You can now log in.", command.username));
    }

    @Override
    public void messageLoggedIn(Command.LoggedIn command) {
        System.out.println(String.format("Logged in as \"%s\" (#%d)", command.username, command.userId));
    }

    @Override
    public void messageLoginRejected(Command.LoginRejected command) {
        userInterface.showInfo(command.reason);
        System.out.println("Login rejected");
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
    }

    @Override
    public void messageLobbyWelcome(Command.LobbyWelcome command) {
        client.joinTeam(1);
        client.setReady(true);
    }

    @Override
    public void messageChatMessage(Command.ChatMessage command) {
        userInterface.incomingChatMessage(command.senderId, command.text);
    }

    @Override
    public void messageUpdateSharedState(Command.UpdateSharedState command) {
        if (world == null) return;
        world.updateCharacter(command);
    }

    @Override
    public void messageUserLeft(Command.UserLeft command) {
        if (world == null) return;
        world.removeCharacterById(command.userId);
        System.out.println(String.format("player #%d left the game", command.userId));
    }
}
