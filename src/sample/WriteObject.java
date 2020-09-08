package sample;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class WriteObject {
    public static void main() throws IOException {

        try {
            FileOutputStream fos = new FileOutputStream("data/keys1.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(Person.langs);

            oos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("ZBS");
    }
}

