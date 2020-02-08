package util.save;

import security.Password;

import java.io.IOException;

public interface SaveOutput {
    void writeByte(byte value) throws IOException;

    void writeInt(int value) throws IOException;

    void writeDouble(double value) throws IOException;

    void writeString(String value) throws IOException;

    void writePassword(Password password) throws IOException;
}
