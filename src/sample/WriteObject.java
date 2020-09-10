package sample;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyPair;

public class WriteObject {

    public static void main(KeyPair keyPair, String name){

        try {
            FileOutputStream fos = new FileOutputStream("data/" + name);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(keyPair);
            System.out.println("ZBS");
            oos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

