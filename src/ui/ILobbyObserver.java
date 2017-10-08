package ui;

import network.LobbyEntry;

public interface ILobbyObserver extends ClientChatListener {
    void teamChanged(int userId, int previousTeamId, int currentTeamId, boolean readyForGame);

    void teamSet(LobbyEntry[] teams);
}
