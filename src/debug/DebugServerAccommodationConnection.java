package debug;

import network.ServerAccommodationConnection;

public class DebugServerAccommodationConnection extends ServerAccommodationConnection {
    @Override
    public int sendTCP(Object object) {
        System.out.println("-> " + object.getClass().getSimpleName() + " -> ");
        return super.sendTCP(object);
    }
}
