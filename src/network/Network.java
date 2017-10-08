package network;

import app.Application;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import gameplay.Character;
import gameplay.*;
import ui.CharacterEndScore;
import ui.TeamEndStats;
import util.Password;

import static app.Application.RunOptions;

public class Network {
    public static class Port {
        public static final int
                forTCP = RunOptions.getValue("-TCPPort"), //default 33455;
                forUDP = RunOptions.getValue("-UDPPort"); //default 33456;
    }

    static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();

        kryo.register(Command.WantsLogin.class);
        kryo.register(Command.LoggedIn.class);
        kryo.register(Command.LoginRejected.class);
        kryo.register(Command.WantsRegister.class);
        kryo.register(Command.Registered.class);

        kryo.register(Command.MatchStarted.class);
        kryo.register(Command.MatchAlreadyStarted.class);
        kryo.register(Command.LobbyTeamChanged.class);
        kryo.register(Command.ReadyForGame.class);
        kryo.register(Command.JoinTeam.class);
        kryo.register(Command.MatchEnded.class);

        kryo.register(Command.UserLeft.class);
        kryo.register(Command.UpdateSharedState.class);
        kryo.register(Command.ChangePlayerControls.class);
        kryo.register(Command.ChatMessage.class);

        kryo.register(LobbyEntry[].class);
        kryo.register(LobbyEntry.class);

        kryo.register(Character[].class);
        kryo.register(PlayerCharacter[].class);
        kryo.register(CharacterSharedState.class);
        kryo.register(CharacterCommonState.class);
        kryo.register(LadderCollide.class);
        kryo.register(WalkDirection.class);

        kryo.register(TeamEndStats[].class);
        kryo.register(TeamEndStats.class);
        kryo.register(CharacterEndScore[].class);
        kryo.register(CharacterEndScore.class);

        kryo.register(PlayerCharacter.class);
        kryo.register(Character.class);
        kryo.register(Password.class);
        kryo.register(byte[].class);
        kryo.register(KeysState.class);
    }

    public interface WorldMessage {
    }

    public static class Command {
        static public class WantsLogin {
            public String username;
            public Password password;
            public String clientVersion = Application.VERSION;

            public WantsLogin() {
            }

            WantsLogin(String username, Password password) {
                this.username = username;
                this.password = password;
            }
        }

        static public class LoggedIn {
            public int userId;
            public String username;

            public LoggedIn() {
            }

            public LoggedIn(int userId, String username) {
                this.userId = userId;
                this.username = username;
            }
        }

        static public class LoginRejected {
            public String reason;

            public LoginRejected() {
                this.reason = "";
            }

            public LoginRejected(String reason) {
                this.reason = reason;
            }
        }

        static public class WantsRegister {
            public String username;
            public String plainPassword;

            public WantsRegister() {
            }

            WantsRegister(String username, String plainPassword) {
                this.username = username;
                this.plainPassword = plainPassword;
            }
        }

        static public class Registered {
            public String username;

            public Registered() {
            }

            public Registered(String username) {
                this.username = username;
            }
        }

        static public class MatchAlreadyStarted {
        }

        static public class JoinTeam {
            public int teamId;

            public JoinTeam() {
            }

            JoinTeam(int teamId) {
                this.teamId = teamId;
            }
        }

        static public class ReadyForGame {
            public boolean state;

            public ReadyForGame() {
            }

            ReadyForGame(boolean readyState) {
                this.state = readyState;
            }
        }

        static public class LobbyTeamChanged {
            public int userId;
            public int previousTeamId;
            public int currentTeamId;
            public boolean readyForGame;

            public LobbyTeamChanged() {
            }

            public LobbyTeamChanged(int userId, int previousTeamId, int currentTeamId, boolean readyForGame) {
                this.userId = userId;
                this.previousTeamId = previousTeamId;
                this.currentTeamId = currentTeamId;
                this.readyForGame = readyForGame;
            }
        }

        static public class MatchStarted {
            public PlayerCharacter[] characters;
            public int clientsCharacterId;
        }

        static class MatchEnded {
        }

        static public class UserLeft implements WorldMessage {
            public String username;
            public int userId;
            public int characterId;

            public UserLeft() {
            }

            public UserLeft(String username, int id, int characterId) {
                this.username = username;
                this.userId = id;
                this.characterId = characterId;
            }
        }

        static public class UpdateSharedState implements WorldMessage {
            public CharacterSharedState state;

            public UpdateSharedState() {
            }

            public UpdateSharedState(CharacterSharedState base) {
                this.state = base.copy();
            }
        }

        static public class ChangePlayerControls implements WorldMessage {
            public KeysState keysState;
            public boolean leftClick, rightClick;

            public ChangePlayerControls() {
            }

            ChangePlayerControls(KeysState keysState, boolean left, boolean right) {
                this.keysState = keysState.copy();
                this.leftClick = left;
                this.rightClick = right;
            }
        }

        static public class ChatMessage implements WorldMessage {
            public int senderId;
            public String text;

            public ChatMessage() {
            }

            ChatMessage(String chatText) {
                this.text = chatText;
            }
        }
    }
}
