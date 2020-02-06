package util;

import java.util.Arrays;

public class Password {
    private final byte[] hashBytes;

    public Password() {
        hashBytes = new byte[0];
    }

    public Password(String plain) {
        this(plain, "");
    }

    public Password(String plain, String salt) {
        hashBytes = Encrypt.SHA1(plain, salt);
    }

    public Password(byte[] hash) {
        hashBytes = hash;
    }

    public byte[] getHash() {
        return hashBytes;
    }

    public boolean compare(String plainPassword) {
        return this.compare(new Password(plainPassword));
    }

    public boolean compare(String plainPassword, String salt) {
        return compare(new Password(plainPassword, salt));
    }

    public boolean compare(Password otherPassword) {
        for (int i = 0; i < hashBytes.length; i++) {
            if (this.hashBytes[i] != otherPassword.hashBytes[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Password)) return false;
        return this.compare((Password) o);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Arrays.hashCode(this.hashBytes);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (byte hashByte : hashBytes) {
            sb.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
