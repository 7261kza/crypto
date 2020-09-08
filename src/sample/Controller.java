package sample;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller implements Serializable {

//    public static ObservableList<String> langs = FXCollections.observableArrayList();

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
    private ComboBox<String> comboBox;

    @FXML
    private TextField nameAddingKey;

    @FXML
    private Button buttonAddKey;

    @FXML
    private Button buttonDeleteKey;


    @FXML
    void initialize() {
        buttonGenKey.setOnAction(actionEvent -> {
            Person person1 = new Person(nameField.getText());
            Person.langs.add(nameField.getText());

//            try {
//                FileOutputStream fos = new FileOutputStream("data/keys.bin");
//                ObjectOutputStream oos = new ObjectOutputStream(fos);
//
//                oos.writeObject(langs);
//                oos.close();
//            }catch (IOException ex){
//                ex.printStackTrace();
//            }
            comboBox.setItems(Person.langs);

//            person1.addFriend("West", person2.getPublicKey());
//            person1.sendMessage("West", "Halo");

//            textFieldDecryption.setText(person1.sendMessage("West", "Halodf;lmdf;bniopurermcb;jdsfpoijuymbx,cvdpfojlknmcvj"));
//
//            textFieldEncryption.setText(person2.receiveMessage(textFieldDecryption.getText()));

        });

        buttonEncrypt.setOnAction(actionEvent -> {
            String recipient = comboBox.getValue();
            if (Person.contacts.containsKey(recipient)){
                String message = textFieldEncryption.getText();
                String cipherText = Message.sendMessage(recipient, message);
                textFieldDecryption.setText(cipherText);
            }
        });

        buttonDecrypt.setOnAction(actionEvent -> {
            String recipient = comboBox.getValue();
            if (Person.contacts.containsKey(recipient)){
                String message = textFieldEncryption.getText();
                String openText = Message.receiveMessage(recipient, message);
                textFieldDecryption.setText(openText);
            }
        });

        comboBox.setOnAction(actionEvent -> {
            String recipient = comboBox.getValue();
            if (Person.contacts.containsKey(recipient)){

                nameAddingKey.setText(Base64.getEncoder().encodeToString(Person.contacts.get(recipient).getPublic().getEncoded()));
                System.out.println(Base64.getEncoder().encodeToString(Person.contacts.get(recipient).getPublic().getEncoded()));
                System.out.println(Person.contacts.get(recipient).getPublic().getEncoded());
            }

        });
        buttonAddKey.setOnAction(actionEvent -> {
            byte[] keyEncoded = Base64.getDecoder().decode(nameAddingKey.getText());

            KeyFactory bobKeyFactory = null;
            PublicKey publicKey = null;
            try {
                bobKeyFactory = KeyFactory.getInstance("RSA");
                X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyEncoded);
                publicKey = bobKeyFactory.generatePublic(x509KeySpec);
                KeyPair keyPair = new KeyPair(publicKey, null);
                Person.contacts.put(nameField.getText(), keyPair);
                Person.langs.add(nameField.getText());
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }

            System.out.println(publicKey.getEncoded());

        });

        buttonDeleteKey.setOnAction(actionEvent -> {
            Person.langs.remove(comboBox.getValue());

        });

    }
}
