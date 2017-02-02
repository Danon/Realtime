package network;

import com.esotericsoftware.kryonet.Connection;

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
        connectionListeners.forEach(ClientConnectionListener::connected);
    }

    @Override
    public void disconnected(Connection connection) {
        connectionListeners.forEach(ClientConnectionListener::disconnected);
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof Network.Command.LoginRejected) {
            forAll(list -> list.messageLoginRejected((Network.Command.LoginRejected) object));
            return;
        }

        if (object instanceof Network.Command.LoggedIn) {
            forAll(listener -> listener.messageLoggedIn((Network.Command.LoggedIn) object));
            return;
        }

        if (object instanceof Network.Command.MatchStarted) {
            forAll(listener -> listener.messageMatchStarted((Network.Command.MatchStarted) object));
            return;
        }

        if (object instanceof Network.Command.MatchAlreadyStarted) {
            forAll(listener -> listener.messageMatchAlreadyStarted((Network.Command.MatchAlreadyStarted) object));
            return;
        }

        if (object instanceof Network.Command.LobbyTeamsChanged) {
            forAll(listener -> listener.messageLobbyTeamsChanged((Network.Command.LobbyTeamsChanged) object));
            return;
        }

        if (object instanceof Network.Command.UpdateSharedState) {
            forAll(listener -> listener.messageUpdateSharedState((Network.Command.UpdateSharedState) object));
            return;
        }

        if (object instanceof Network.Command.UserLeft) {
            forAll(listener -> listener.messageUserLeft((Network.Command.UserLeft) object));
            return;
        }

        if (object instanceof Network.Command.Registered) {
            forAll(listener -> listener.messageRegistered((Network.Command.Registered) object));
            return;
        }

        if (object instanceof Network.Command.ChatMessage) {
            forAll(listener -> listener.messageChatMessage((Network.Command.ChatMessage) object));
        }
    }

}
