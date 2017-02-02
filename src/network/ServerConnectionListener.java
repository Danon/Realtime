package network;

import network.Network.Command;

public interface ServerConnectionListener {
    void connected();

    void disconnected(Accommodator accommodator);

    void message(ServerAccommodationConnection conn, Command.WantsLogin command, boolean loggedIn);

    void message(ServerAccommodationConnection conn, Command.WantsRegister command, boolean loggedIn);

    void message(ServerAccommodationConnection conn, Command.JoinTeam command);

    void message(ServerAccommodationConnection conn, Command.ReadyForGame command);

    void message(ServerAccommodationConnection conn, Command.ChangePlayerControls command);

    void message(ServerAccommodationConnection conn, Command.ChatMessage command);
}
