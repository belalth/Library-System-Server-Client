package com.librarysytsem; 

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class MainUIAdmin implements Initializable {
    public Text text;
    @FXML
    private TextField miniAuthor;
    @FXML
    private TextField miniDate;
    @FXML
    private TextField miniId;
    @FXML
    private TextField miniIsbn;
    @FXML
    private ImageView miniPicture;
    @FXML
    private TextField miniPublisher;
    @FXML
    private TextField miniQuan;
    @FXML
    private TextField miniRating;
    @FXML
    private TextField miniTitle;
    @FXML
    private TextField miniTotal;
    @FXML
    private BorderPane borderPane ;

    //switching the slide scenes
    @FXML
    private void Home(MouseEvent event) throws IOException {
        loadPage("PaneBooksTableView");
    }
    @FXML
    private void Users(MouseEvent event) throws IOException {
        loadPage("PaneUsersTableView");
    }
    @FXML
    private void AddBook(MouseEvent event) throws IOException {
        loadPage("PaneAddBook");
    }
    @FXML
    private void AddUser(MouseEvent event) throws IOException{
        loadPage("PaneAddUser");
    }
    @FXML
    private void LogOut(MouseEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }
    /**
     *Method for the swtiching the slid buttons
     */
    private void loadPage(String pageName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(pageName + ".fxml"));
            Parent root =  fxmlLoader.load() ; // FXMLLoader.load(getClass().getResource(pageName + ".fxml"));
            borderPane.setCenter(root);
        } catch (IOException e) {
            System.out.println("Failed to load page: " + pageName + ".fxml");
        }
    }

    public void closeButton(MouseEvent event) {
        System.exit(1 );
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { loadPage("PaneBooksTableView");}
}
