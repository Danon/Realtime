package ui;

import network.LobbyEntry;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class LobbyState extends AbstractListModel<LobbyEntry> {
    private List<LobbyEntry> lobbyEntries = new ArrayList<>();

    public LobbyState(JList<LobbyEntry> listModel) {
        listModel.setModel(this);
    }

    public void setLobbyEntries(List<LobbyEntry> lobbyEntries) {
        this.lobbyEntries = lobbyEntries;
    }

    @Override
    public int getSize() {
        return lobbyEntries.size();
    }

    @Override
    public LobbyEntry getElementAt(int index) {
        return lobbyEntries.get(index);
    }
}
