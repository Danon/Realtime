package network;

import network.Network.Command;

public interface ClientConnectionListener extends ClientLoginListener {
    void connected();

    void connectError();

    void disconnected();

    void messageRegistered(Command.Registered command);

    void messageMatchStarted(Command.MatchStarted command);

    void messageMatchAlreadyStarted(Command.MatchAlreadyStarted command);

    void messageLobbyTeamsChanged(Command.LobbyTeamsChanged command);

    void messageChatMessage(Command.ChatMessage command);

    void messageUpdateSharedState(Command.UpdateSharedState command);

    void messageUserLeft(Command.UserLeft command);
}
