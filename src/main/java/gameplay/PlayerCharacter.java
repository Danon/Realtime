package gameplay;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PlayerCharacter extends Character {
    private int userId;
    @Getter
    private String username;
    private int teamId;

    public PlayerCharacter(int userId, String username, int teamId, int characterId) {
        super(characterId);
        this.userId = userId;
        this.username = username;
        this.teamId = teamId;
    }

    @Override
    public String getDisplayName() {
        return this.username;
    }

    @Override
    public String toString() {
        return String.format("player #%d %s (charId: %d, teamId: %d)", userId, username, characterId, teamId);
    }
}
