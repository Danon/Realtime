package network;

import com.esotericsoftware.kryonet.Connection;

public class ServerAccommodationConnection extends Connection {
    private Accommodator accommodator;

    public Accommodator getAccomodator() {
        return this.accommodator;
    }

    public void setAccomodator(Accommodator accomodator) {
        this.accommodator = accomodator;
    }
}
