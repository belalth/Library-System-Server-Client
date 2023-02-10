package com.librarysytsem; 


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.Socket;

public class Launcher extends Application {
    public static Socket socket ; 
    
   

    @Override
     public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        Scene scene = new Scene(loadFXML("Login"));
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

      
        socket = new Socket();
        

    }
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    public static void main(String[] args) throws IOException {
        
        launch(args);
        
    }
}


