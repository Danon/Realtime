package network;

@FunctionalInterface
public interface ServerConnectionListenerIterator {
    void iterate(ServerConnectionListener listener);
}