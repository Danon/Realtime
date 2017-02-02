package ui;

import java.net.InetAddress;

public interface IHostOperator {
    void discoverHosts();

    void connectToHost(InetAddress address);

    void loginToHost(String username, String plainPassword);

    void registerOnHost(String username, String plainPassword);
}
