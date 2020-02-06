package debug.decorator;

import com.esotericsoftware.kryonet.Connection;
import network.ClientConnectionManager;
import network.Network;

public class DebugClientConnectionManager extends ClientConnectionManager {
    @Override
    public void received(Connection connection, Object object) {
        super.received(connection, object);

        if (object.getClass() == Network.Command.UpdateSharedState.class) {
            return;
        }

        System.out.println("Received '" + object.getClass().getSimpleName() + "'");
    }
}
