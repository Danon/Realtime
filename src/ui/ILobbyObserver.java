package ui;

public interface ILobbyObserver extends ClientChatListener {
    void teamChanged(int userId, int previousTeamId, int currentTeamId, boolean readyForGame);
}
