package app;

import debug.DebugClientConnectionManager;
import gameplay.Character;
import network.ClientConnectionListener;
import network.ClientConnectionManager;
import network.Network.Command;
import ui.ClientUserInterface;
import ui.ILobbyObserver;
import util.Size;

import java.util.HashSet;
import java.util.Set;

public class GameClient implements ClientConnectionListener {
    private ClientUserInterface userInterface;
    private ClientConnectionManager client;
    Set<Character> loggedCharacters = new HashSet<>();

    // Object that is notified when lobby's state changed
    private ILobbyObserver lobby;

    GameClient() {
        client = new DebugClientConnectionManager();
        client.openSocket();
        client.addConnectionListener(this);
        userInterface = new ClientUserInterface(client, new Size(880, 750));
    }

    void openUserInterface() {
        client.addHostObserver(userInterface.createHostObserver(client));
        lobby = userInterface.createLobbyObserver(client);
        client.addChatListener(lobby);
        userInterface.open();
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
        userInterface.openLobby();
        System.out.println(String.format("Logged in as \"%s\" (#%d)", command.username, command.userId));
    }

    @Override
    public void messageLoginRejected(Command.LoginRejected command) {
        userInterface.showInfo(command.reason);
        client.disconnect();
        userInterface.open();
    }

    @Override
    public void messageMatchStarted(Command.MatchStarted command) {
        System.out.println("Match started.");
        userInterface.setMainPlayerLoggedIn(command.clientsCharacterId);
        userInterface.startGame(command.characters);
    }

    @Override
    public void messageMatchAlreadyStarted(Command.MatchAlreadyStarted command) {
    }

    @Override
    public void messageLobbyTeamChanged(Command.LobbyTeamChanged command) {
        lobby.teamChanged(command.userId, command.previousTeamId, command.currentTeamId, command.readyForGame);
    }

    @Override
    public void messageChatMessage(Command.ChatMessage command) {
        userInterface.incomingChatMessage(command.senderId, command.text);
    }

    @Override
    public void messageUpdateSharedState(Command.UpdateSharedState command) {
        userInterface.world.updateCharacter(command);
    }

    @Override
    public void messageUserLeft(Command.UserLeft command) {
        userInterface.world.removeCharacterById(command.userId);
        System.out.println(String.format("player #%d left the game", command.userId));
    }
}
