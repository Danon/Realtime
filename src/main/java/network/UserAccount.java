package network;

import lombok.EqualsAndHashCode;
import save.UserAccountSaveableFactory;
import security.Password;
import util.save.Saveable;

@EqualsAndHashCode
public class UserAccount implements Saveable {
    private int id;
    private String username;
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

    public static UserAccountSaveableFactory factory() {
        return new UserAccountSaveableFactory();
    }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public Password getPassword() {
        return this.password;
    }
}
