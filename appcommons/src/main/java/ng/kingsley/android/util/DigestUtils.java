package ng.kingsley.android.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author ADIO Kingsley O.
 * @since 14 Oct, 2016
 */

public class DigestUtils {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final String MD5 = "MD5";
    private static final String SHA1 = "SHA-1";
    private static final String SHA256 = "SHA-256";
    private static final String SHA512 = "SHA-512";
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String md5(String input) {
        byte[] hex = getDigest(MD5).digest(input.getBytes(UTF_8));
        return hexBytesToString(hex);
    }

    public static String sha1(String input) {
        byte[] hex = getDigest(SHA1).digest(input.getBytes(UTF_8));
        return hexBytesToString(hex);
    }

    public static String sha256(String input) {
        byte[] hex = getDigest(SHA256).digest(input.getBytes(UTF_8));
        return hexBytesToString(hex);
    }

    public static String sha512(String input) {
        byte[] hex = getDigest(SHA512).digest(input.getBytes(UTF_8));
        return hexBytesToString(hex);
    }


    private static MessageDigest getDigest(final String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static String hexBytesToString(byte[] bytes) {
        char[] hexChars = new char[bytes.length << 1];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j << 1] = hexArray[v >>> 4];
            hexChars[(j << 1) + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
