package sample;

import java.io.*;
import java.security.KeyPair;

public class ReadObject {
    public static KeyPair main(String path){

        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);

            KeyPair keyPair = (KeyPair) ois.readObject();

            ois.close();
            return keyPair;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

