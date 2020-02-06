package network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class LobbyEntry {
    public static final int ROOMLESS = -1;

    @Getter
    private int userId;

    private String username;

    @Getter
    @Setter
    private boolean readyForGame;
    @Getter
    @Setter
    private int chosenTeamId;

    LobbyEntry() {
        userId = -1;
    }

    @Override
    public String toString() {
        return String.format("#%d %s (%s)",
                this.userId,
                this.username,
                this.readyForGame ? "ready" : "waiting");
    }
}
