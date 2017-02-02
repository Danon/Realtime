package util.save;

import util.Password;

import java.io.*;

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
        file.getParentFile().mkdirs();

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

    boolean loadObject(String folderName, String fileName, Savable savable) {
        File file = new File(folderName, fileName.toLowerCase());
        if (!file.exists()) {
            throw new RuntimeException("No such file found " + fileName);
        }

        try {
            inpStream = new DataInputStream(new FileInputStream(file));
            savable.restoreState(this);
            return true;
        } catch (IOException couldNotLoadState) {
            return false;
        } finally {
            try {
                inpStream.close();
            } catch (IOException ignored) {
            }
        }
    }
}
