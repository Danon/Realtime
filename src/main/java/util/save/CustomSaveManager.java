package util.save;

import lombok.SneakyThrows;
import ui.gfx.resources.duality.FileHandler;
import util.Password;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CustomSaveManager implements SaveInput, SaveOutput {
    private DataInputStream inpStream;
    private DataOutputStream outStream;

    public void writeByte(byte bajt) throws IOException {
        outStream.write(bajt);
    }

    public void writeInt(int value) throws IOException {
        outStream.writeInt(value);
    }

    public void writeDouble(double value) throws IOException {
        outStream.writeDouble(value);
    }

    public void writeString(String value) throws IOException {
        outStream.writeUTF(value);
    }

    public void writePassword(Password password) throws IOException {
        writeInt(password.getHash().length);
        for (byte bajt : password.getHash()) {
            writeByte(bajt);
        }
    }

    public byte readByte() throws IOException {
        return inpStream.readByte();
    }

    public int readInt() throws IOException {
        return inpStream.readInt();
    }

    public double readDouble() throws IOException {
        return inpStream.readDouble();
    }

    public String readString() throws IOException {
        return inpStream.readUTF();
    }

    public Password readPassword() throws IOException {
        int bytesCount = readInt();
        byte[] bytes = new byte[bytesCount];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = readByte();
        }
        return new Password(bytes);
    }

    boolean saveObject(String folderName, String fileName, Savable item) {
        File file = new File(folderName, fileName.toLowerCase());
        if (!file.getParentFile().mkdirs()) {
            return false;
        }

        try {
            outStream = new DataOutputStream(new FileOutputStream(file));
            item.storeState(this);
            outStream.close();
            return true;
        } catch (IOException couldNotSave) {
            return false;
        } finally {
            try {
                if (outStream != null) outStream.close();
            } catch (IOException ignored) {
            }
        }
    }

    @SneakyThrows
    void loadObject(String folderName, String fileName, Savable savable) {
        FileHandler file = new FileHandler(folderName + "/" + fileName.toLowerCase());
        if (!file.exists()) {
            throw new RuntimeException(String.format("No such file '%s' found", fileName));
        }

        try {
            inpStream = new DataInputStream(file.getInputStream());
            savable.restoreState(this);
        } finally {
            inpStream.close();
        }
    }
}
