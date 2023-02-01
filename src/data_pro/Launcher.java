package data_pro;

import data_pro.DataBase.RWDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.Objects;

public class Launcher extends Application {
    @Override
     public void start(Stage stage) throws Exception {
        RWDatabase.reader();
        RWDatabase.readUsersData();
        RWDatabase.readOwnedBooks();
        Image img = new Image("data_pro/icons/appIcon.png");
        stage.getIcons().add(img);
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("Fxml/Login.fxml"))));
        stage.setResizable(false);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}