package ui;

import java.net.InetAddress;
import java.util.List;

public interface IHostObserver {
    void provideHostList(List<InetAddress> hosts);

    void connected();
}
