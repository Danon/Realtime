package ui;

import network.LobbyEntry;

public interface ILobbyObserver {
    void teamsChanged(LobbyEntry[] lobbyEntries);
}
