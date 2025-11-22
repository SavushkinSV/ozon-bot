package ssv.home.ozonbot.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
public class EncryptionUtil {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private final String secretKey; // 16 байт для AES-128
    private final String initVector; // 16 байт IV

    public EncryptionUtil(@Value("${security.crypto.secret-key}") String secretKey,
                          @Value("${security.crypto.vector}") String initVector) {
        if (secretKey.length() != 16 || initVector.length() != 16) {
            throw new IllegalArgumentException("Неправильная длина ключа или вектора инициализации.");
        }
        this.secretKey = secretKey;
        this.initVector = initVector;
    }

    public String encrypt(String plainText) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public String decrypt(String encryptedText) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] original = cipher.doFinal(decodedBytes);
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

}
