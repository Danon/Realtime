package network;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import save.UserAccountSaveableFactory;
import security.Password;
import util.save.SaveOutput;
import util.save.Saveable;

import java.io.IOException;

@EqualsAndHashCode
public class UserAccount implements Saveable {
    @Getter
    private int id;
    @Getter
    private String username;
    @Getter
    private Password password;

    public UserAccount(int id, String username, String plainPassword) {
        this(id, username, new Password(plainPassword, ""));
    }

    public UserAccount(int id, String username, Password password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public UserAccount() {
    }

    @Override
    public String toString() {
        return String.format("#%d %s", id, username);
    }

    @Override
    public void storeState(SaveOutput output) throws IOException {
        output.writeInt(id);
        output.writeString(username);
        output.writePassword(password);
    }

    public static UserAccountSaveableFactory factory() {
        return new UserAccountSaveableFactory();
    }
}
