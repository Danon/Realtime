package ui;

import javax.swing.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class DiscoveredHostsListModel extends AbstractListModel<InetAddress> {
    private final List<InetAddress> addresses = new ArrayList<>();

    public synchronized void setDiscoveredHosts(final List<InetAddress> discoveredHosts) {
        addresses.addAll(discoveredHosts);
    }

    @Override
    public synchronized int getSize() {
        return addresses.size();
    }

    @Override
    public synchronized InetAddress getElementAt(int i) {
        return addresses.get(i);
    }
}