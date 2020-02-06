package util;

public class Bytes {
    public static String toString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte hashByte : bytes) {
            sb.append(hex(hashByte));
        }
        return sb.toString();
    }

    private static String hex(byte hashByte) {
        return Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1);
    }
}
