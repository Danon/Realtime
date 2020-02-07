package network;

import gameplay.CountedCharacters;
import gameplay.PlayerCharacter;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

public class Accommodator {
    // Lobby
    public UserAccount user;
    public LobbyEntry lobbyEntry;

    // In-Game
    @Getter
    @Setter
    private PlayerCharacter playerCharacter;

    public Accommodator(UserAccount user) {
        this.user = user;
        this.lobbyEntry = new LobbyEntry(user.getId(), user.getUsername(), false, LobbyEntry.ROOMLESS);
    }

    public static CountedCharacters countedCharacters(Set<Accommodator> populatedRoom) {
        CountedCharacters counted = new CountedCharacters();

        for (Accommodator accommodator : populatedRoom) {
            if (accommodator.lobbyEntry.isReadyForGame()) {
                counted.ready++;
            }
            if (accommodator.lobbyEntry.getChosenTeamId() > 0) {
                counted.inTeams++;
            }
        }
        counted.all = populatedRoom.size();
        counted.inSpectate = counted.all - counted.inTeams;

        return counted;
    }
}
