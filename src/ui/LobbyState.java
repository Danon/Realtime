package ui;

import network.LobbyEntry;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class LobbyState extends AbstractListModel<LobbyEntry> {
    private List<LobbyEntry> lobbyEntries = new ArrayList<>();

    public LobbyState(JList<LobbyEntry> list) {
        list.setModel(this);
    }

    public void setLobbyEntries(List<LobbyEntry> lobbyEntries) {
        this.lobbyEntries = lobbyEntries;
        this.fireContentsChanged(this, 0, lobbyEntries.size() - 1);
    }

    @Override
    public int getSize() {
        return lobbyEntries.size();
    }

    @Override
    public LobbyEntry getElementAt(int index) {
        return lobbyEntries.get(index);
    }

    public void addUser(int userId, int chosenId, boolean readyForGame) {
        lobbyEntries.add(new LobbyEntry(userId, "Unkknown", readyForGame, chosenId));
        this.fireContentsChanged(this, lobbyEntries.size() - 1, lobbyEntries.size() - 1);
    }

    public void removeUser(int userId) {
        for (int i = 0; i < lobbyEntries.size(); i++) {
            LobbyEntry entry = lobbyEntries.get(i);
            if (entry.getUserId() == userId) {
                lobbyEntries.remove(entry);
                this.fireContentsChanged(this, 0, 10);
            }
        }
    }
}
