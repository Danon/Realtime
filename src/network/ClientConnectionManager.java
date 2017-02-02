package network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import network.Network.Command;
import ui.*;
import ui.window.ServerLoginForm;
import util.Password;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

abstract class KryonetListener extends com.esotericsoftware.kryonet.Listener {
    List<ClientConnectionListener> connectionListeners = new CopyOnWriteArrayList<>();

    public void addConnectionListener(ClientConnectionListener listener) {
        connectionListeners.add(listener);
    }

    private void forAll(Consumer<ClientConnectionListener> consumer) {
        connectionListeners.forEach(consumer);
    }

    @Override
    public void connected(Connection connection) {
        connectionListeners.forEach(network.ClientConnectionListener::connected);
    }

    @Override
    public void disconnected(Connection connection) {
        connectionListeners.forEach(ClientConnectionListener::disconnected);
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof Command.LoginRejected) {
            forAll(list -> list.messageLoginRejected((Command.LoginRejected) object));
            return;
        }

        if (object instanceof Command.LoggedIn) {
            forAll(listener -> listener.messageLoggedIn((Command.LoggedIn) object));
            return;
        }

        if (object instanceof Command.MatchStarted) {
            forAll(listener -> listener.messageMatchStarted((Command.MatchStarted) object));
            return;
        }

        if (object instanceof Command.MatchAlreadyStarted) {
            forAll(listener -> listener.messageMatchAlreadyStarted((Command.MatchAlreadyStarted) object));
            return;
        }

        if (object instanceof Command.LobbyTeamsChanged) {
            forAll(listener -> listener.messageLobbyTeamsChanged((Command.LobbyTeamsChanged) object));
            return;
        }

        if (object instanceof Command.UpdateSharedState) {
            forAll(listener -> listener.messageUpdateSharedState((Command.UpdateSharedState) object));
            return;
        }

        if (object instanceof Command.UserLeft) {
            forAll(listener -> listener.messageUserLeft((Command.UserLeft) object));
            return;
        }

        if (object instanceof Command.Registered) {
            forAll(listener -> listener.messageRegistered((Command.Registered) object));
            return;
        }

        if (object instanceof Command.ChatMessage) {
            forAll(listener -> listener.messageChatMessage((Command.ChatMessage) object));
        }
    }

}

public class ClientConnectionManager extends KryonetListener
        implements IHostOperator, ILobbyOperator, IKeyStateNotifier {
    private Client kryoClient = new Client();

    private List<IHostObserver> hostObservers = new CopyOnWriteArrayList<>();

    public ClientConnectionManager() {
        kryoClient.addListener(new ThreadedListener(this));
    }

    public void openSocket() {
        // For consistency, the classes to be sent over the network are
        // registered by the same method for both the kryoClient and kryoServer.
        Network.register(kryoClient);
        kryoClient.start();
    }

    public void addHostObserver(IHostObserver listener) {
        hostObservers.add(listener);
    }

    @Override
    public void discoverHosts() {
        final List<InetAddress> hosts = kryoClient.discoverHosts(Network.Port.forUDP, 5000);
        hostObservers.forEach(observer -> observer.provideHostList(hosts));
    }

    @Override
    public void connectToHost(InetAddress address) {
        try {
            kryoClient.connect(5000, address, Network.Port.forTCP, Network.Port.forUDP);
            connectionListeners.forEach(network.ClientConnectionListener::connected);
        } catch (IOException ex) {
            connectionListeners.forEach(network.ClientConnectionListener::connectError);
            return;
        }

        new ServerLoginForm(this).setVisible(true);
    }

    @Override
    public void loginToHost(String username, String plainPassword) {
        kryoClient.sendTCP(new Command.WantsLogin(username, new Password(plainPassword)));
    }

    @Override
    public void registerOnHost(String username, String plainPassword) {
        kryoClient.sendTCP(new Command.WantsRegister(username, plainPassword));
    }

    @Override
    public void joinTeam(int teamId) {
        kryoClient.sendTCP(new Command.JoinTeam(teamId));
    }

    @Override
    public void setReady(boolean readyState) {
        kryoClient.sendTCP(new Command.ReadyForGame(readyState));
    }

    @Override
    public void sendCurrentMove(KeysState keysState, boolean left, boolean right) {
        kryoClient.sendTCP(new Command.ChangePlayerControls(keysState, left, right));
    }

    @Override
    public void sendTextMessage(String text) {
        kryoClient.sendTCP(new Command.ChatMessage(text));
    }
}
