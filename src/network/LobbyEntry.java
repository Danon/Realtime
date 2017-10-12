package network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LobbyEntry {
    public static final int ROOMLESS = -1;

    private int userId;
    private String username;
    private boolean readyForGame;
    private int chosenTeam;

    LobbyEntry() {
        userId = -1;
    }

    public LobbyEntry(int userId, String username, boolean readyForGame, int chosenTeam) {
        this.userId = userId;
        this.readyForGame = readyForGame;
        this.username = username;
        this.chosenTeam = chosenTeam;
    }

    public int getUserId() {
        return userId;
    }

    public int getChosenTeamId() {
        return this.chosenTeam;
    }

    public boolean isReadyForGame() {
        return this.readyForGame;
    }

    public void setReadyForGame(boolean state) {
        this.readyForGame = state;
    }

    public void setChosenTeamId(int chosenTeamId) {
        this.chosenTeam = chosenTeamId;
    }

    @Override
    public String toString() {
        return String.format("#%d %s (%s)",
                this.userId,
                this.username,
                this.readyForGame ? "ready" : "waiting");
    }
}
