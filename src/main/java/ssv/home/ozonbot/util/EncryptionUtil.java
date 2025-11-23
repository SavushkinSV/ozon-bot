package ssv.home.ozonbot.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ssv.home.ozonbot.entity.client.Client;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
@Component
public class EncryptionUtil {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int KEY_LENGTH_BYTES = 16; // AES-128
    private static final int IV_LENGTH_BYTES = 16;

    private final byte[] masterKey;

    public EncryptionUtil(@Value("${security.crypto.master-key}") String masterKey) {
        if (masterKey.length() != KEY_LENGTH_BYTES) {
            throw new IllegalArgumentException("Master key должен быть ровно 16 символов (16 байт для UTF-8)");
        }
        this.masterKey = masterKey.getBytes(StandardCharsets.UTF_8);
    }

    public String encrypt(String plainText, Client client) {
        try {
            // Генерируем уникальный IV для каждого шифрования
            byte[] iv = generateIv(client);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // Получаем ключ на основе masterKey и данных клиента
            byte[] derivedKey = deriveKey(client);
            SecretKeySpec keySpec = new SecretKeySpec(derivedKey, "AES");

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            // Объединяем IV и зашифрованные данные
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            log.error("Шифрование не удалось для клиента ID: {}", client.getId(), e);
            throw new RuntimeException("Шифрование не удалось", e);
        }
    }

    public String decrypt(String encryptedText, Client client) {
        try {
            byte[] combined = Base64.getDecoder().decode(encryptedText);
            byte[] iv = Arrays.copyOfRange(combined, 0, IV_LENGTH_BYTES); // Извлекаем IV (первые 16 байт)
            byte[] cipherText = Arrays.copyOfRange(combined, IV_LENGTH_BYTES, combined.length); // Извлекаем зашифрованные данные (остальное)

            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            byte[] derivedKey = deriveKey(client);
            SecretKeySpec keySpec = new SecretKeySpec(derivedKey, "AES");

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] original = cipher.doFinal(cipherText);
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Расшифрование не удалось для клиента ID: {}", client.getId(), e);
            throw new RuntimeException("Расшифрование не удалось", e);
        }
    }

    private byte[] generateIv(Client client) {
        byte[] iv = new byte[IV_LENGTH_BYTES];
        new SecureRandom().nextBytes(iv); // Используем SecureRandom для генерации случайного IV

        // Добавляем энтропию из данных клиента (опционально)
        String token = client.getToken().replace("-", "");
        if (!token.isEmpty()) {
            for (int i = 0; i < iv.length; i++) {
                iv[i] ^= (byte) token.charAt(i % token.length());
            }
        }
        return iv;
    }

    private byte[] deriveKey(Client client) {
        String token = client.getToken();
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Токен клиента не может быть пустым");
        }
        token = token.replace("-", ""); // Удаляем дефисы
        if (token.length() < KEY_LENGTH_BYTES) { // Проверяем длину токена
            throw new IllegalArgumentException("Токен клиента слишком короткий для генерации ключа");
        }

        // Генерируем ключ на основе masterKey и токена клиента
        byte[] tokenBytes = token.getBytes(StandardCharsets.UTF_8);
        byte[] derivedKey = new byte[KEY_LENGTH_BYTES];

        for (int i = 0; i < KEY_LENGTH_BYTES; i++) {
            derivedKey[i] = (byte) (masterKey[i] ^ tokenBytes[i % tokenBytes.length]);
        }
        return derivedKey;
    }

}
