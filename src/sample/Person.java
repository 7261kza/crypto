package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.crypto.Cipher;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Person implements Serializable {

    public static ObservableList<String> langs = FXCollections.observableArrayList();
    private String name;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public static Map<String, KeyPair> contacts = new HashMap<>();

    public Person(String name) {
        this.name = name;
        generateKeyPair(name);

    }

    public void generateKeyPair(String name) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();

            contacts.put(name, pair);

            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public String getName() {
        return name;
    }

//    public void addFriend(String name, PublicKey publicKey) {
//        contacts.put(name, publicKey);
//    }

    public String sendMessage(String recipient, String message) {
        PublicKey publicKey = contacts.get(recipient).getPublic();
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

    public String receiveMessage(String cipherText) {
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

