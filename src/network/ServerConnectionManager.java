package network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import debug.decorator.DebugServerAccommodationConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static network.Network.*;

public class ServerConnectionManager extends com.esotericsoftware.kryonet.Listener {
    private Server kryoServer;
    private List<ServerConnectionListener> connectionListeners = new ArrayList<>();

    public ServerConnectionManager() {
        kryoServer = new Server() {
            @Override
            protected Connection newConnection() {
                return new DebugServerAccommodationConnection();
            }
        };
        kryoServer.addListener(this);
    }

    public void openSocket() throws IOException {
        // For consistency, the classes to be sent over the network are
        // registered by the same method for both the kryoClient and kryoServer.
        register(kryoServer);

        kryoServer.bind(Port.forTCP, Port.forUDP);
        kryoServer.start();
    }

    public void addConnectionListener(ServerConnectionListener listener) {
        if (listener == null) {
            return;
        }
        connectionListeners.add(listener);
    }

    @Override
    public void received(Connection con, Object command) {
        ServerAccommodationConnection connection = (ServerAccommodationConnection) con;
        Accommodator accomodator = connection.getAccomodator();

        if (command instanceof Command.WantsLogin)
            forEach(list -> list.message(connection, (Command.WantsLogin) command, accomodator != null));
        if (command instanceof Command.WantsRegister)
            forEach(list -> list.message(connection, (Command.WantsRegister) command, accomodator != null));

        // Ignore the rest of messages, if not logged in
        if (accomodator == null) {
            return;
        }

        if (command instanceof Command.JoinTeam)
            forEach(list -> list.message(connection, (Command.JoinTeam) command));
        if (command instanceof Command.ReadyForGame)
            forEach(list -> list.message(connection, (Command.ReadyForGame) command));
        if (command instanceof Command.ChangePlayerControls)
            forEach(list -> list.message(connection, (Command.ChangePlayerControls) command));
        if (command instanceof Command.ChatMessage)
            forEach(list -> list.message(connection, (Command.ChatMessage) command));
    }

    @Override
    public void connected(Connection c) {
        forEach(listener -> listener.connected((ServerAccommodationConnection) c));
    }

    @Override
    public void disconnected(Connection c) {
        ServerAccommodationConnection connection = (ServerAccommodationConnection) c;
        Accommodator accomodator = connection.getAccomodator();

        forEach(list -> list.disconnected(accomodator));
    }

    private void forEach(ServerConnectionListenerIterator action) {
        for (ServerConnectionListener listener : connectionListeners) {
            action.iterate(listener);
        }
    }

    public void forAllConnections(ConnectionIterable iteratable) {
        // Send information with players to all players, with their charactersId.
        for (Connection connection : kryoServer.getConnections()) {
            iteratable.iterate((ServerAccommodationConnection) connection);
        }
    }

    public void sendLoginRejectedMessage(Connection con, String reason) {
        con.sendTCP(new Command.LoginRejected(reason));
    }

    public void rejectConnection(Connection con) {
        con.close();
    }

    public void sendToAllTCP(Object o) {
        kryoServer.sendToAllTCP(o);
    }

    public void disconnect() {
        kryoServer.close();
    }
}
