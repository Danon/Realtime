package ui;

public interface ILobbyOperator extends IChatOperator {
    void joinTeam(int teamId);

    void setReady(boolean readyState);
}
