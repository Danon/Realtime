package app;

import game.CountedCharacters;
import gameplay.PlayerCharacter;
import gameplay.ServerWorld;
import network.*;
import network.Network.Command;
import ui.ServerUserInterface;
import util.Validate;
import util.save.PrimitiveReader;
import util.save.SaveManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class GameServer implements ServerConnectionListener {
    private ServerConnectionManager server;

    // GameRoom
    private HashSet<Accommodator> loggedIn = new HashSet<>();
    private ServerWorld world = new ServerWorld();
    private static boolean matchStarted = false;

    // GameRoom Requirements (which can be shared among Rooms)
    private final static int maxPlayers = 20;
    public final static int requiredPlayers = Application.RunOptions.isUsed("-Debug") ? 1 : 2;

    GameServer() throws IOException {
        this.world.setMap(SaveManager.Map.load("Standard"));

        server = new ServerConnectionManager();
        try {
            server.openSocket();
            server.addConnectionListener(this);

            world.startLoop();
            System.out.println(String.format("Server started v%s (::%d :: %d)", Application.VERSION, Network.Port.forTCP, Network.Port.forUDP));
            ServerUserInterface ui = new ServerUserInterface();
            ui.showWindow();
            System.out.println("World's running");
        } catch (IOException ex) {
            System.err.println("Socket port is probably in use.");
        }
    }

    private void tryLoginAccomodator(ServerAccommodationConnection conn, Accommodator accom) {
        if (loggedIn.size() >= GameServer.maxPlayers) {
            conn.sendTCP(new Command.LoginRejected(String.format(
                    "Max capability of players logged at a time (%d) reached. Try again later.",
                    maxPlayers)));
            return;
        }

        // Apply a ServerAccomodator to the connection.
        conn.setAccomodator(accom);

        // Add accomodator to the room
        loggedIn.add(accom);

        // Inform the player that login was successfull.
        conn.sendTCP(new Command.LoggedIn(accom.user.getId(), accom.user.getUsername()));

        if (!matchStarted) // check if game already is running
        {
            recheckGameStart(); // if not, check if match start condition is met
        } else {
            conn.sendTCP(new Command.MatchAlreadyStarted()); // if the match is running, inform the player
        }

        System.out.println("Logged: " + accom.user);
    }

    boolean matchStartCondition(CountedCharacters charsTable) {
        return (charsTable.inTeams == charsTable.ready && charsTable.inTeams > 0);
    }

    void recheckGameStart() {
        // See how many players are willing to play
        CountedCharacters counted = Accommodator.countedCharacters(loggedIn);

        if (matchStartCondition(counted)) {
            matchStarted = true;
            informAboutMatchStart(counted);
        }
    }

    void informAboutMatchStart(CountedCharacters counted) {
        // Prepare match start Command
        Command.MatchStarted matchStartMsg = new Command.MatchStarted();

        // Populate message with information about all characters
        // which will inform players about each other
        int loggedInIndex = 0;
        List<PlayerCharacter> characters = new ArrayList<>(counted.ready);
        for (Accommodator accomod : loggedIn) {
            if (accomod.lobbyEntry.isReadyForGame()) {
                PlayerCharacter playerCharacter = new PlayerCharacter(
                        accomod.user.getId(),
                        accomod.user.getUsername(),
                        accomod.lobbyEntry.getChosenTeamId(),
                        loggedInIndex
                );

                accomod.setPlayerCharacter(playerCharacter);
                world.addCharacter(playerCharacter);
                characters.add(playerCharacter);
            }
        }
        matchStartMsg.characters = characters.toArray(new PlayerCharacter[counted.ready]);

        // Send information with players to all players, with their charactersId.
        server.forAllConnections((conn) -> {
            matchStartMsg.clientsCharacterId = conn.getAccomodator().getPlayerCharacter().getCharacterId();
            conn.sendTCP(matchStartMsg);
        });

    }

    @Override
    public void connected() {

    }

    @Override
    public void disconnected(Accommodator accomod) {

        if (matchStarted) {
            if (accomod != null) {
                loggedIn.remove(accomod);
                Command.UserLeft removeCharacter = new Command.UserLeft(
                        accomod.user.getUsername(),
                        accomod.user.getId(),
                        accomod.getPlayerCharacter().getCharacterId()
                );
                server.sendToAllTCP(removeCharacter);
                world.messageArrived(accomod.user.getId(), removeCharacter);
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

        if (!SaveManager.Accounts.save(user)) {
            conn.sendTCP(new Command.LoginRejected("Couldn't save the characer. Maybe try again later?"));
            return;
        } else {
            conn.sendTCP(new Command.Registered(user.getUsername()));
        }
    }

    @Override
    public void message(ServerAccommodationConnection conn, Command.JoinTeam command) {

        if (matchStarted) return;

        Command.JoinTeam joinTeamRequest = command;
        conn.getAccomodator().lobbyEntry.setChosenTeamId(joinTeamRequest.teamId);

        server.sendToAllTCP(new Command.LobbyTeamsChanged(Accommodator.getLobbyTeams(loggedIn)));
    }

    @Override
    public void message(ServerAccommodationConnection conn, Command.ReadyForGame command) {

        if (matchStarted) return;

        Command.ReadyForGame readyMsg = command;
        conn.getAccomodator().lobbyEntry.setReadyForGame(readyMsg.state);
        recheckGameStart();
    }

    @Override
    public void message(ServerAccommodationConnection conn, Command.ChangePlayerControls command) {

        if (!matchStarted) return;

        Command.ChangePlayerControls controlsChanged = command;

        int charId = conn.getAccomodator().getPlayerCharacter().getCharacterId();

        world.messageArrived(charId, controlsChanged);
        server.sendToAllTCP(world.waitForAcceptance(charId));
    }

    @Override
    public void message(ServerAccommodationConnection conn, Command.ChatMessage command) {

        Command.ChatMessage chatMessage = command;
        chatMessage.senderId = conn.getAccomodator().user.getId();
        server.sendToAllTCP(chatMessage);
    }

}