package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.security.MessageDigest.getInstance;

class Encrypt {
    static byte[] SHA1(String password, String salt) {
        return SHA1(password.getBytes(), salt.getBytes());
    }

    private static byte[] SHA1(byte[] password, byte[] salt) {
        try {
            MessageDigest md = getInstance("SHA-1");
            md.update(salt);
            return md.digest(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
