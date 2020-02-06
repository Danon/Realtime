package network;

import app.Application;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import gameplay.Character;
import gameplay.CharacterCommonState;
import gameplay.CharacterSharedState;
import gameplay.LadderCollide;
import gameplay.PlayerCharacter;
import gameplay.WalkDirection;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import util.Password;

import static app.Application.RunOptions;

public class Network {
    public static class Port {
        public static final int
                forTCP = RunOptions.getNumber("-TCPPort"), //default 33455;
                forUDP = RunOptions.getNumber("-UDPPort"); //default 33456;
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
        kryo.register(Command.LobbyWelcome.class);
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
        kryo.register(String[].class);

        kryo.register(Character[].class);
        kryo.register(PlayerCharacter[].class);
        kryo.register(CharacterSharedState.class);
        kryo.register(CharacterCommonState.class);
        kryo.register(LadderCollide.class);
        kryo.register(WalkDirection.class);

        kryo.register(PlayerCharacter.class);
        kryo.register(Character.class);
        kryo.register(Password.class);
        kryo.register(byte[].class);
        kryo.register(KeysState.class);
    }

    public interface WorldMessage {
    }

    public static class Command {
        @NoArgsConstructor
        static public class WantsLogin {
            public String username;
            public Password password;
            public String clientVersion = Application.VERSION;

            WantsLogin(String username, Password password) {
                this.username = username;
                this.password = password;
            }
        }

        @NoArgsConstructor
        @AllArgsConstructor
        static public class LoggedIn {
            public int userId;
            public String username;
        }

        @NoArgsConstructor
        @AllArgsConstructor
        static public class LoginRejected {
            public String reason = "";
        }

        @NoArgsConstructor
        @AllArgsConstructor
        static public class WantsRegister {
            public String username;
            public String plainPassword;
        }

        @NoArgsConstructor
        @AllArgsConstructor
        static public class Registered {
            public String username;
        }

        static public class MatchAlreadyStarted {
        }

        @NoArgsConstructor
        @AllArgsConstructor
        static public class JoinTeam {
            public int teamId;
        }

        @NoArgsConstructor
        @AllArgsConstructor
        static public class ReadyForGame {
            public boolean state;
        }

        @NoArgsConstructor
        @AllArgsConstructor
        static public class LobbyTeamChanged {
            public int userId;
            public int previousTeamId;
            public int currentTeamId;
            public boolean readyForGame;
        }

        @NoArgsConstructor
        @AllArgsConstructor
        static public class LobbyWelcome {
            public LobbyEntry[] teams;
            public String[] chatHistory;
        }

        static public class MatchStarted {
            public PlayerCharacter[] characters;
            public int clientsCharacterId;
        }

        static class MatchEnded {
        }

        @NoArgsConstructor
        @AllArgsConstructor
        static public class UserLeft implements WorldMessage {
            public String username;
            public int userId;
            public int characterId;
        }

        @NoArgsConstructor
        static public class UpdateSharedState implements WorldMessage {
            public CharacterSharedState state;

            public UpdateSharedState(CharacterSharedState base) {
                this.state = base.copy();
            }
        }

        @NoArgsConstructor
        @AllArgsConstructor
        static public class ChangePlayerControls implements WorldMessage {
            public KeysState keysState;
            public boolean leftClick;
            public boolean rightClick;
        }

        @NoArgsConstructor
        static public class ChatMessage implements WorldMessage {
            public int senderId;
            public String text;

            ChatMessage(String chatText) {
                this.text = chatText;
            }
        }
    }
}
