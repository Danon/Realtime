package network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LobbyEntry {
    private int userId;
    private String username;
    private boolean readyForGame;
    private int chosenTeam;

    public LobbyEntry() {
        userId = -1;
    }

    LobbyEntry(int userId, String username, boolean readyForGame, int chosenTeam) {
        this.userId = userId;
        this.readyForGame = readyForGame;
        this.username = username;
        this.chosenTeam = chosenTeam;
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

    public static List<LobbyEntry> asList(LobbyEntry... lobbyEntries) {
        return new ArrayList<>(Arrays.asList(lobbyEntries));
    }

    @Override
    public String toString() {
        return String.format("%s (%s) team: %d",
                this.username,
                this.readyForGame ? "ready" : "waiting",
                this.chosenTeam);
    }
}
