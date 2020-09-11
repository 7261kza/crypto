package sample;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class Encryption {

    public static String sendMessage(SecretKey secretKey, String message) {

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] msgBytes = message.getBytes(StandardCharsets.UTF_8);
            byte[] encrypted = cipher.doFinal(msgBytes);

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String receiveMessage(SecretKey secretKey, String cipherText) {
        byte[] encrypted = Base64.getDecoder().decode(cipherText);

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decrypted = cipher.doFinal(encrypted);

            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String encryptedKey(SecretKey secretKey, PublicKey publicKey){

        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.PUBLIC_KEY, publicKey);
            byte[] encryptedKey = cipher.doFinal(secretKey.getEncoded());
            return Base64.getEncoder().encodeToString(encryptedKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SecretKey decryptedKey(String encryptedKey, PrivateKey privateKey){
        byte[] keyEncode = Base64.getDecoder().decode(encryptedKey);
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.PRIVATE_KEY, privateKey);
            byte[] decryptedKey = cipher.doFinal(keyEncode);

            SecretKey originalKey = new SecretKeySpec(decryptedKey, 0, decryptedKey.length, "AES");
            return originalKey;
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
