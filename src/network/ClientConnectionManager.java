package network;

import com.esotericsoftware.kryonet.Client;
import network.Network.Command;
import ui.IHostObserver;
import ui.IHostOperator;
import ui.IKeyStateNotifier;
import ui.ILobbyOperator;
import util.Password;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientConnectionManager extends KryonetListener implements IHostOperator, ILobbyOperator, IKeyStateNotifier {
    private final Client kryoClient = new Client();
    private final List<IHostObserver> hostObservers = new CopyOnWriteArrayList<>();

    public ClientConnectionManager() {
        kryoClient.addListener(new ThreadedListener(this));
    }

    public void openSocket(ClientConnectionListener listener) {
        // For consistency, the classes to be sent over the network are
        // registered by the same method for both the kryoClient and kryoServer.
        Network.register(kryoClient);
        addConnectionListener(listener);
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
        if (kryoClient.isConnected()) {
            System.out.println("Already connected");
            return;
        }
        try {
            tryConnect(address);
            connectionListeners.forEach(network.ClientConnectionListener::connected);
            hostObservers.forEach(IHostObserver::connected);
        } catch (ClientConnectionException ex) {
            connectionListeners.forEach(network.ClientConnectionListener::connectError);
        }
    }

    private void tryConnect(InetAddress address) {
        try {
            kryoClient.connect(5000, address, Network.Port.forTCP, Network.Port.forUDP);
        } catch (IOException e) {
            throw new ClientConnectionException(e);
        }
    }

    @Override
    public boolean isConnected() {
        return kryoClient.isConnected();
    }

    @Override
    public void disconnect() {
        kryoClient.close();
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
