package ui;

public interface ILobbyObserver {
    void teamChanged(int userId, int previousTeamId, int currentTeamId);
}
