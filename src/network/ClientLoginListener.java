package network;

import network.Network.Command;

public interface ClientLoginListener {
    void messageLoggedIn(Command.LoggedIn command);

    void messageLoginRejected(Command.LoginRejected command);
}
