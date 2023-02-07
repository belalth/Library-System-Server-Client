package com.librarysytsem; 

import com.librarysytsem.DataBase.* ; 
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;


public class Launcher extends Application {
    @Override
     public void start(Stage stage) throws Exception {
        RWDatabase.reader();
        RWDatabase.readUsersData();
        RWDatabase.readOwnedBooks();
//        Image img = new Image("librarysytsem/icons/appIcon.png");
//        stage.getIcons().add(img);
        stage.setResizable(false);

        Scene scene = new Scene(loadFXML("Login"));

        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    public static void main(String[] args) {
        launch(args);
    }
}