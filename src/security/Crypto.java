package security;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crypto {
    private final static String salt = "Mamamamamamama mamama, papapapapa papa papapa, mamamamamama papapapapa ma. Her kommer mumi!";
    public static String hash(String x) {

        MessageDigest d = null;
        try {
            d = MessageDigest.getInstance("SHA-256");
            d.reset();
            d.update((x+salt).getBytes());
            return DatatypeConverter.printBase64Binary(d.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
