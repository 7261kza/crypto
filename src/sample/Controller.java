package sample;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.crypto.*;

public class Controller implements Serializable {


    GenerationKey generationKey = new GenerationKey();
    MapAndObservList mapAndObservList = new MapAndObservList();


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea textFieldEncryption;

    @FXML
    private TextArea textFieldDecryption;

    @FXML
    private TextField nameField;

    @FXML
    private Button buttonGenKey;

    @FXML
    private Button buttonEncrypt;

    @FXML
    private Button buttonDecrypt;

    @FXML
    public ComboBox<String> comboBox;

    @FXML
    private TextField nameAddingKey;

    @FXML
    private Button buttonAddKey;

    @FXML
    private Button buttonDeleteKey;

    @FXML
    private TextField sessionKey;

    @FXML
    void initialize() {

        textFieldEncryption.setText("Сюда напишите свое сообщение, которое необходимо защифровать или расшифровать");
        Path path = Path.of("data");

        try (DirectoryStream<Path> files = Files.newDirectoryStream(path)) {
            for (Path temp : files) {
                mapAndObservList.getlangs().add(temp.getFileName().toString());
                mapAndObservList.getMap().put(temp.getFileName().toString(), ReadObject.main(temp.toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        comboBox.setItems(mapAndObservList.getlangs());

        buttonGenKey.setOnAction(actionEvent -> {

            if (!nameField.getText().isEmpty() && !mapAndObservList.getlangs().contains(nameField.getText())) {

                KeyPair keyPair = generationKey.RSA();
                mapAndObservList.getlangs().add(nameField.getText());
                mapAndObservList.getMap().put(nameField.getText(), keyPair);

                WriteObject.main(keyPair, nameField.getText());

                comboBox.setItems(mapAndObservList.getlangs());
            } else {
                AnimationShake as = new AnimationShake(nameField);
                as.playAnim();
            }
        });

        buttonEncrypt.setOnAction(actionEvent -> {

            if (!comboBox.getValue().isEmpty()) {
                String recipient = comboBox.getValue();
                String plainText = textFieldEncryption.getText();
                // сначала создали секретный ключ
                SecretKey secretKey = generationKey.AES();
                // затем зашифровали им исходный текст
                textFieldDecryption.setText(Encryption.sendMessage(secretKey, plainText));
                // дальше шифруем ключ (secretKey.getEncoded()) и возвращаем его в качестве String через Base64
                sessionKey.setText(Encryption.encryptedKey(secretKey, mapAndObservList.getMap().get(recipient).getPublic()));
            } else {
                System.out.println("Animation");
            }
        });

        buttonDecrypt.setOnAction(actionEvent -> {

            String recipient = comboBox.getValue();
            if (!recipient.isEmpty() && !sessionKey.getText().isEmpty()) {
                // теперь надо взять зашифрованный ключ
                String encryptedKey = sessionKey.getText();
                // далее дешифруем ключ, но перед этим превращаем строку в массив байтов через Base64
                SecretKey secretKey = Encryption.decryptedKey(encryptedKey, mapAndObservList.getMap().get(recipient).getPrivate());
                // расшифровываем шифртекст
                textFieldDecryption.setText(Encryption.receiveMessage(secretKey, textFieldEncryption.getText()));
            } else {
                System.out.println("Animation");
            }
        });

        comboBox.setOnAction(actionEvent -> {

            String recipient = comboBox.getValue();
            if (mapAndObservList.getMap().containsKey(recipient)) {

                nameAddingKey.setText(Base64.getEncoder().encodeToString(mapAndObservList.getMap().get(recipient).getPublic().getEncoded()));
            }
        });

        buttonAddKey.setOnAction(actionEvent -> {

            if (!nameField.getText().isEmpty() && !mapAndObservList.getlangs().contains(nameField.getText())
                    && !nameAddingKey.getText().isEmpty()) {

                try {
                    byte[] keyEncoded = Base64.getDecoder().decode(nameAddingKey.getText());
                    KeyFactory bobKeyFactory = KeyFactory.getInstance("RSA");
                    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyEncoded);
                    PublicKey publicKey = bobKeyFactory.generatePublic(x509KeySpec);
                    KeyPair keyPair = new KeyPair(publicKey, null);
                    mapAndObservList.getMap().put(nameField.getText(), keyPair);
                    mapAndObservList.getlangs().add(nameField.getText());
                    WriteObject.main(keyPair, nameField.getText());
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    e.printStackTrace();
                }
            } else {
                AnimationShake as1 = new AnimationShake(nameField);
                AnimationShake as2 = new AnimationShake(nameAddingKey);
                as1.playAnim();
                as2.playAnim();
            }
        });

        buttonDeleteKey.setOnAction(actionEvent -> {

            System.out.println(comboBox.getValue());
            Path path1 = Path.of("data/" + comboBox.getValue());
            try {
                Files.delete(path1);
                System.out.println("Secsesful");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mapAndObservList.getMap().containsKey(comboBox.getValue())) {
                mapAndObservList.getMap().remove(comboBox.getValue());
            }
            if (mapAndObservList.getlangs().contains(comboBox.getValue())) {
                mapAndObservList.getlangs().remove(comboBox.getValue());
            }
            comboBox.setItems(mapAndObservList.getlangs());
        });
    }
}
