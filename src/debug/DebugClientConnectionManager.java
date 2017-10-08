package debug;

import com.esotericsoftware.kryonet.Connection;
import network.ClientConnectionManager;

public class DebugClientConnectionManager extends ClientConnectionManager {
    @Override
    public void received(Connection connection, Object object) {
        super.received(connection, object);
        System.out.println("Received '" + object.getClass().getSimpleName() + "'");
    }
}
