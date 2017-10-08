package debug;

import network.Network;
import network.ServerAccommodationConnection;

public class DebugServerAccommodationConnection extends ServerAccommodationConnection {
    @Override
    public int sendTCP(Object object) {
        if (object.getClass() != Network.Command.UpdateSharedState.class) {
            System.out.println("-> " + object.getClass().getSimpleName() + " -> ");
        }

        return super.sendTCP(object);
    }
}
