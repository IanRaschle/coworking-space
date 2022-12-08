package ch.zli.iraschle.util;

import org.apache.commons.codec.binary.Hex;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PasswortHashing {

    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 512;
    private static final byte[] SALT_BYTES = "W3x01!".getBytes();

    public static String hashPassword(final String password) {
        char[] passwordChars = password.toCharArray();
        byte[] hashedBytes = hashPassword(passwordChars, SALT_BYTES, ITERATIONS, KEY_LENGTH);
        return Hex.encodeHexString(hashedBytes);
    }

    public static String hashPassword(final String password, final String salt, final int iterations, final int keyLength) {
        char[] passwordChars = password.toCharArray();
        byte[] saltBytes = salt.getBytes();
        byte[] hashedBytes = hashPassword(passwordChars, saltBytes, iterations, keyLength);
        return Hex.encodeHexString(hashedBytes);
    }

    /**This code is copied from https://medium.com/@kasunpdh/how-to-store-passwords-securely-with-pbkdf2-204487f14e84 and slightly changed*/
    private static byte[] hashPassword(final char[] password, final byte[] salt, final int iterations, final int keyLength) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
            PBEKeySpec spec = new PBEKeySpec( password, salt, iterations, keyLength );
            SecretKey key = skf.generateSecret( spec );
            return key.getEncoded( );
        } catch ( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
    }
}
