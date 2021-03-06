package learn.springws.restfulws.shared;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class Utils {

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private final SecureRandom random = new SecureRandom();

    @Value("${publicIdLength}")
    private int publicIdLength;

    public String generatePublicId() {
        return generateRandomString(publicIdLength);
    }

    public String generateUserId(int length) {
        return generateRandomString(length);
    }

    private String generateRandomString(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return builder.toString();
    }

    public static boolean anyFieldIsMissing(String... fields) {
        for (String field : fields) {
            if (field == null || field.isBlank()) {
                return true;
            }
        }
        return false;
    }
}
