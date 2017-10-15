package debug;

import debug.decorator.DebugClientConnectionManager;
import gameplay.ClientWorld;
import network.ClientConnectionListener;
import network.ClientConnectionManager;
import network.Network.Command;
import ui.ClientUserInterface;
import util.Size;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DebugGameClient implements ClientConnectionListener {
    private final ClientUserInterface userInterface;
    private final ClientConnectionManager client = new DebugClientConnectionManager();
    private ClientWorld world;

    public DebugGameClient() {
        client.openSocket(this);

        userInterface = new ClientUserInterface(client, new Size(880, 750));
        client.connectToHost(getLocalHost());
        client.loginToHost("Test", "test");
        client.joinTeam(1);
        client.setReady(true);
    }

    private InetAddress getLocalHost() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void connected() {
        // client.updateReturnTripTime();   // ping
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
        client.disconnect();
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
