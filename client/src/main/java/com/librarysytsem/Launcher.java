package com.librarysytsem; 


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;




public class Launcher extends Application {

    public static SocketConnection connection ;
    
    @Override
     public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        Scene scene = new Scene(loadFXML("Login"));
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        connection = new SocketConnection("localhost", 8000);
    

    }
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    public static void main(String[] args) throws IOException {
        
        launch(args);
        
    }
}



