package network;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import security.Password;
import util.save.Savable;
import util.save.SaveInput;
import util.save.SaveOutput;
import util.save.SaveableFactory;

import java.io.IOException;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserAccount implements Savable {
    @Getter
    private int id;
    @Getter
    private String username;
    @Getter
    private Password password;

    public UserAccount(int id, String username, String plainPassword) {
        this(id, username, new Password(plainPassword, ""));
    }

    @Override
    public String toString() {
        return String.format("#%d %s", id, username);
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

    @NotNull
    @Override
    public SaveableFactory factory(@NotNull SaveInput input) throws IOException {
        return null;
    }
}
