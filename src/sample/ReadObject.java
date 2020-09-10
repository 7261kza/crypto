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
//            System.out.println("ZBS too");
            return keyPair;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
//        System.out.println("ne ZBS too");
        return null;
    }
}

