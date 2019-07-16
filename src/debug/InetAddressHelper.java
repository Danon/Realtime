package debug;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressHelper {
     static InetAddress getLocalHost() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
