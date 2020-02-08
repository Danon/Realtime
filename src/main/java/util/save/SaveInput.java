package util.save;

import security.Password;

import java.io.IOException;

public interface SaveInput {
    byte readByte() throws IOException;

    int readInt() throws IOException;

    double readDouble() throws IOException;

    String readString() throws IOException;

    Password readPassword() throws IOException;
}
