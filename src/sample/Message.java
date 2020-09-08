package sample;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class Message {

    public static String sendMessage(String recipient, String message) {
        PublicKey publicKey = Person.contacts.get(recipient).getPublic();
        if (publicKey == null) {
            throw new RuntimeException("Unknown recipient!");
        }
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] msgBytes = message.getBytes(StandardCharsets.UTF_8);
            byte[] encrypted = cipher.doFinal(msgBytes);

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String receiveMessage(String recipient, String cipherText) {
        PrivateKey privateKey = Person.contacts.get(recipient).getPrivate();
        byte[] encrypted = Base64.getDecoder().decode(cipherText);

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] decrypted = cipher.doFinal(encrypted);

            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
