package network;

import network.Network.Command;

public interface ClientLobbyListener {
    void messageLobbyTeamChanged(Command.LobbyTeamChanged command);
}
