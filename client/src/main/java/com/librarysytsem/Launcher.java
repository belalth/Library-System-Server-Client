package com.librarysytsem; 


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.Socket;


public class Launcher extends Application {
    @Override
     public void start(Stage stage) throws Exception {

//        Image img = new Image("librarysytsem/icons/appIcon.png");
//        stage.getIcons().add(img);
        boolean autApproved = false ;
        try(Socket socket = new Socket("localhost", 8080)){
            System.out.println("hi there ");
        }



        stage.setResizable(false);
        Scene scene = new Scene(loadFXML("Login"));
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    public static void main(String[] args) {
        launch(args);
    }
}