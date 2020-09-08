package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("RSA");
        primaryStage.setScene(new Scene(root, 1018, 560));
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException {

        launch(args);
        WriteObject.main();
    }

}
