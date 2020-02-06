package network;

import com.esotericsoftware.kryonet.Connection;
import lombok.Getter;
import lombok.Setter;

public class ServerAccommodationConnection extends Connection {
    @Getter
    @Setter
    private Accommodator accommodator;
}
