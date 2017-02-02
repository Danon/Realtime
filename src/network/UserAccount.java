package network;

import util.Password;
import util.save.Savable;
import util.save.SaveInput;
import util.save.SaveOutput;

import java.io.IOException;

public class UserAccount implements Savable {
    private int id;
    private String username;
    private Password password;

    public UserAccount() {
    }

    private UserAccount(String username, Password password) {
        this.username = username;
        this.password = password;
    }

    public UserAccount(int id, String username, String plainPassword) {
        this(username, new Password(plainPassword));
        this.id = id;
    }

    public Password getPassword() {
        return this.password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return String.format("#%d %s", id, username);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof UserAccount && id == ((UserAccount) o).id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.id;
        return hash;
    }

    @Override
    public void restoreState(SaveInput input) throws IOException {
        id = input.readInt();
        username = input.readString();
        password = input.readPassword();
    }

    @Override
    public void storeState(SaveOutput output) throws IOException {
        output.writeInt(id);
        output.writeString(username);
        output.writePassword(password);
    }
}
