package app;

import com.esotericsoftware.kryonet.Connection;
import gameplay.CountedCharacters;
import gameplay.PlayerCharacter;
import gameplay.ServerWorld;
import network.*;
import network.Network.Command;
import ui.PrintStreamJFrame;
import util.Validate;
import util.save.PrimitiveReader;
import util.save.SaveManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class GameServer implements ServerConnectionListener {
    private ServerConnectionManager server;

    // GameRoom
    private Set<Accommodator> loggedIn = new HashSet<>();
    private final ServerWorld world;
    private static boolean matchStarted = false;

    private final static int maxPlayers = 20;

    public static void main(String[] args) {
        new GameServer();
    }

    GameServer() {
        this.world = new ServerWorld(SaveManager.Map.load("Standard"));

        server = new ServerConnectionManager();
        try {
            server.openSocket();
            server.addConnectionListener(this);

            world.startLoop();
            System.out.println(String.format("Server started v%s (::%d :: %d)", Application.VERSION, Network.Port.forTCP, Network.Port.forUDP));
            PrintStreamJFrame ui = new PrintStreamJFrame(() -> server.forAllConnections(Connection::close));
            ui.show();

            System.setOut(ui.getPrintStream());
            System.setErr(ui.getPrintStream());

            System.out.println("World's running");
        } catch (IOException ex) {
            System.err.println("Socket port is probably in use.");
        }
    }

    private void tryLoginAccomodator(ServerAccommodationConnection conn, Accommodator accommodator) {
        if (loggedIn.size() >= GameServer.maxPlayers) {
            conn.sendTCP(new Command.LoginRejected(String.format(
                    "Max capability of players logged at a time (%d) reached. Try again later.",
                    maxPlayers)));
            return;
        }

        conn.setAccomodator(accommodator);
        loggedIn.add(accommodator);

        conn.sendTCP(new Command.LoggedIn(accommodator.user.getId(), accommodator.user.getUsername()));

        if (matchStarted) {
            conn.sendTCP(new Command.MatchAlreadyStarted());
        } else {
            recheckGameStart();
        }

        System.out.println("Logged: " + accommodator.user);
    }

    private boolean matchStartCondition(CountedCharacters charsTable) {
        return (charsTable.inTeams == charsTable.ready && charsTable.inTeams > 0);
    }

    private void recheckGameStart() {
        CountedCharacters counted = Accommodator.countedCharacters(loggedIn);

        if (matchStartCondition(counted)) {
            matchStarted = true;
            informAboutMatchStart(counted);
        }
    }

    private void informAboutMatchStart(CountedCharacters counted) {
        Command.MatchStarted matchStartMsg = new Command.MatchStarted();

        int loggedInIndex = 0;
        List<PlayerCharacter> characters = new ArrayList<>(counted.ready);
        for (Accommodator accommodator : loggedIn) {
            if (accommodator.lobbyEntry.isReadyForGame()) {
                PlayerCharacter playerCharacter = new PlayerCharacter(
                        accommodator.user.getId(),
                        accommodator.user.getUsername(),
                        accommodator.lobbyEntry.getChosenTeamId(),
                        loggedInIndex++
                );

                accommodator.setPlayerCharacter(playerCharacter);
                world.addCharacter(playerCharacter);
                characters.add(playerCharacter);
            }
        }
        matchStartMsg.characters = characters.toArray(new PlayerCharacter[counted.ready]);

        server.forAllConnections(conn -> {
            matchStartMsg.clientsCharacterId = conn.getAccomodator().getPlayerCharacter().getCharacterId();
            conn.sendTCP(matchStartMsg);
        });
    }

    @Override
    public void connected(ServerAccommodationConnection connection) {
        Command.LobbyWelcome welcome = new Command.LobbyWelcome();

        welcome.teams = loggedIn.stream()
                .map(accommodator -> accommodator.lobbyEntry)
                .collect(toList())
                .toArray(new LobbyEntry[loggedIn.size()]);

        connection.sendTCP(welcome);
    }

    @Override
    public void disconnected(Accommodator accommodator) {
        if (matchStarted) {
            if (accommodator != null) {
                loggedIn.remove(accommodator);
                Command.UserLeft removeCharacter = new Command.UserLeft(
                        accommodator.user.getUsername(),
                        accommodator.user.getId(),
                        accommodator.getPlayerCharacter().getCharacterId()
                );
                server.sendToAllTCP(removeCharacter);
                world.messageArrived(accommodator.user.getId(), removeCharacter);
            }
        }
    }

    @Override
    public void message(ServerAccommodationConnection conn, Command.WantsLogin loginRequest, boolean isLoggedIn) {
        // Ignore if already logged in.
        if (isLoggedIn) return;

        // check if server and clients versions match
        if (!loginRequest.clientVersion.equals(Application.VERSION)) {

            conn.sendTCP(new Command.LoginRejected(String.format(
                    "Client VERSION (v%s) doesn't match with server VERSION (v%s).",
                    loginRequest.clientVersion, Application.VERSION))
            );
            conn.close();
            return;
        }

        // Reject if the name is invalid.
        if (!Validate.Specific.username(loginRequest.username)) {
            conn.sendTCP(new Command.LoginRejected(String.format("Name \"%s\" is invalid.", loginRequest.username)));
            return;
        }

        // Reject if someone is logged on this account
        for (Accommodator anyoneLogged : loggedIn) {
            if (anyoneLogged.user.getUsername().equals(loginRequest.username)) {
                conn.sendTCP(new Command.LoginRejected(String.format("Someone is already logged as \"%s\".", loginRequest.username)));
                return;
            }
        }

        Accommodator accommodator = new Accommodator();
        if (SaveManager.Accounts.exists(loginRequest.username)) {
            accommodator.setUser(SaveManager.Accounts.load(loginRequest.username));

            if (accommodator.user == null) {
                conn.sendTCP(new Command.LoginRejected("IO Error : reading account file."));
                return;
            } else {
                if (!Application.RunOptions.isUsed("-IgnorePassword")) {
                    if (!accommodator.user.getPassword().compare(loginRequest.password)) {
                        conn.sendTCP(new Command.LoginRejected("Wrong username or password."));
                        return;
                    }
                }
            }
        } else {
            conn.sendTCP(new Command.LoginRejected("Your account doesn't exists"));
            return;
        }

        tryLoginAccomodator(conn, accommodator);
    }

    @Override
    public void message(ServerAccommodationConnection conn, Command.WantsRegister command, boolean isLoggedIn) {
        // Ignore if already logged in.
        if (isLoggedIn) return;

        // Reject if the login is invalid.
        if (!Validate.Specific.username(command.username)) {
            conn.sendTCP(new Command.LoginRejected(String.format("Name \"%s\" is invalid.", command.username)));
            return;
        }
        if (!Validate.Specific.password(command.plainPassword)) {
            conn.sendTCP(new Command.LoginRejected("Password is invalid."));
            return;
        }

        // Reject if character alread exists.
        if (SaveManager.Accounts.exists(command.username)) {
            conn.sendTCP(new Command.LoginRejected(String.format("Account name \"%s\" is already in use.", command.username)));
            return;
        }

        PrimitiveReader reader = new PrimitiveReader();
        if (SaveManager.exists("settings", "userId")) {
            SaveManager.load("settings", "userId", reader);
            reader.setValue(reader.getValue() + 1);
        } else {
            reader.setValue(10);
        }
        UserAccount user = new UserAccount(reader.getValue(), command.username, command.plainPassword);
        SaveManager.save(reader, "settings", "userId");

        if (SaveManager.Accounts.save(user)) {
            conn.sendTCP(new Command.Registered(user.getUsername()));
        } else {
            conn.sendTCP(new Command.LoginRejected("Couldn't save the characer. Maybe try again later?"));
        }
    }

    @Override
    public void message(ServerAccommodationConnection conn, Command.JoinTeam joinTeam) {
        if (matchStarted) return;

        Accommodator accomodator = conn.getAccomodator();
        int previousTeamId = accomodator.lobbyEntry.getChosenTeamId();
        accomodator.lobbyEntry.setChosenTeamId(joinTeam.teamId);

        server.sendToAllTCP(new Command.LobbyTeamChanged(accomodator.user.getId(), previousTeamId, joinTeam.teamId, accomodator.lobbyEntry.isReadyForGame()));
    }

    @Override
    public void message(ServerAccommodationConnection conn, Command.ReadyForGame readyForGame) {
        if (matchStarted) return;

        Accommodator accomodator = conn.getAccomodator();
        accomodator.lobbyEntry.setReadyForGame(readyForGame.state);
        int teamId = accomodator.lobbyEntry.getChosenTeamId();

        server.sendToAllTCP(new Command.LobbyTeamChanged(accomodator.user.getId(), teamId, teamId, accomodator.lobbyEntry.isReadyForGame()));

        recheckGameStart();
    }

    @Override
    public void message(ServerAccommodationConnection conn, Command.ChangePlayerControls playerControls) {
        if (!matchStarted) return;

        int charId = conn.getAccomodator().getPlayerCharacter().getCharacterId();

        world.messageArrived(charId, playerControls);
        world.waitForAcceptance(charId)
                .ifPresent(state -> server.sendToAllTCP(state));
    }

    @Override
    public void message(ServerAccommodationConnection conn, Command.ChatMessage chatMessage) {
        chatMessage.senderId = conn.getAccomodator().user.getId();
        server.sendToAllTCP(chatMessage);
    }
}
