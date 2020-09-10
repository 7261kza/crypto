package sample;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
import javax.crypto.spec.SecretKeySpec;

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

    public Controller() throws IOException {
    }


    @FXML
    void initialize() {

        textFieldEncryption.setText("Вы можете найти, что размер \"длина\" два байта.\n" +
                "" +
                "То, что тип возврата определенного метода (например, String.length()) составляет int, не всегда означает, что его допустимое максимальное значение равно Integer.MAX_VALUE. Вместо этого, в большинстве случаев, int выбирается только по соображениям производительности. Спецификация языка Java говорит, что целые числа, размер которых меньше, чем int, преобразуются в int перед вычислением (если моя память служит мне правильно), и это одна из причин выбора int, когда нет особой причины.\n" +
                "" +
                "Максимальная длина во время компиляции не более 65536. Заметим еще раз, что длина представляет собой количество байтов измененного представления UTF-8, а не количество символов в объекте String.\n" +
                "" +
                "String объекты могут иметь гораздо больше символов во время выполнения. Однако, если вы хотите использовать объекты String с интерфейсами DataInput и DataOutput, лучше избегать использования слишком длинных объектов String. Я нашел это ограничение, когда я внедрил Objective-C эквиваленты DataInput.readUTF() и DataOutput.writeUTF(String).\n" +
                "" +
                "  +17  ");
        Path path = Path.of("data");

        try (DirectoryStream<Path> files = Files.newDirectoryStream(path)) {
            for (Path temp : files) {

//                System.out.println( temp.toString());
//                System.out.println(temp.getFileName());
                mapAndObservList.getlangs().add(temp.getFileName().toString());
                mapAndObservList.getMap().put(temp.getFileName().toString(), ReadObject.main(temp.toString()));
//                    System.out.println(temp.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(mapAndObservList.getMap().keySet());
        comboBox.setItems(mapAndObservList.getlangs());

        buttonGenKey.setOnAction(actionEvent -> {

            if (!nameField.getText().isEmpty() && !mapAndObservList.getlangs().contains(nameField.getText())) {

                KeyPair keyPair = generationKey.RSA();
                mapAndObservList.getlangs().add(nameField.getText());
                mapAndObservList.getMap().put(nameField.getText(), keyPair);

                WriteObject.main(keyPair, nameField.getText());

                comboBox.setItems(mapAndObservList.getlangs());
            } else {
                System.out.println("Empty name");
            }
        });

        buttonEncrypt.setOnAction(actionEvent -> {

            String recipient = comboBox.getValue();
            String plainText = textFieldEncryption.getText();
            // сначала создали секретный ключ
            SecretKey secretKey = generationKey.AES();
            // затем зашифровали им исходный текст
            textFieldDecryption.setText(Message.sendMessage(secretKey, plainText));
            // дальше шифруем ключ (secretKey.getEncoded()) и возвращаем его в качестве String через Base64
            sessionKey.setText(Message.encryptedKey(secretKey, mapAndObservList.getMap().get(recipient).getPublic()));


//            String recipient = comboBox.getValue();
//            if (mapAndObservList.getMap().containsKey(recipient) && !textFieldEncryption.getText().isEmpty()) {
//                String message = textFieldEncryption.getText();
//                String cipherText = Message.sendMessage(mapAndObservList.getMap().get(recipient).getPublic(), message);
//                textFieldDecryption.setText(cipherText);
//            } else {
//                System.out.println("Пусто");
//            }


        });

        buttonDecrypt.setOnAction(actionEvent -> {

            String recipient = comboBox.getValue();
            // теперь надо взять зашифрованный ключ
            String encryptedKey = sessionKey.getText();
            // далее дешифруем ключ, но перед этим превращаем строку в массив байтов через Base64
            SecretKey secretKey = Message.decryptedKey(encryptedKey, mapAndObservList.getMap().get(recipient).getPrivate());
            // расшифровываем шифртекст
            textFieldDecryption.setText(Message.receiveMessage(secretKey, textFieldEncryption.getText()));

//            String recipient = comboBox.getValue();
//            if (mapAndObservList.getMap().containsKey(recipient) && !textFieldEncryption.getText().isEmpty()) {
//                String message = textFieldEncryption.getText();
//                String openText = Message.receiveMessage(mapAndObservList.getMap().get(recipient).getPrivate(), message);
//                textFieldDecryption.setText(openText);
//            } else {
//                System.out.println("Пусто nj;t");
//            }
        });

        comboBox.setOnAction(actionEvent -> {

            String recipient = comboBox.getValue();
            if (mapAndObservList.getMap().containsKey(recipient)) {

                nameAddingKey.setText(Base64.getEncoder().encodeToString(mapAndObservList.getMap().get(recipient).getPublic().getEncoded()));
//                System.out.println(Base64.getEncoder().encodeToString(mapAndObservList.getMap().get(recipient).getPublic().getEncoded()));
//                System.out.println(mapAndObservList.getMap().get(recipient).getPublic().getEncoded());
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
                System.out.println("Add empty name");
            }
        });

        buttonDeleteKey.setOnAction(actionEvent -> {
//            if (!comboBox.пуеШ) {
//                textFieldEncryption.setWrapText("dfkl;j;dfljk");
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
                System.out.println("MAP keys delete");
            } else System.out.println("MAP keys not found");
            if (mapAndObservList.getlangs().contains(comboBox.getValue())) {
                mapAndObservList.getlangs().remove(comboBox.getValue());
                System.out.println("LANGS keys delete");
            } else System.out.println("LANGS keys not found");
            System.out.println(mapAndObservList.getMap().keySet());
            comboBox.setItems(mapAndObservList.getlangs());
//            }
        });
    }
}
