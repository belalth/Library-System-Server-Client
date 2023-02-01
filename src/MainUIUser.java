package data_pro;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainUIUser implements Initializable {
    public Text text;
    @FXML
    private BorderPane bp ;

    @FXML
    private Text welcomName;

    @FXML
    private Text welcomUserId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //to display the user id and the name
        welcomName.setText(PaneMyLibrary.name);
        welcomUserId.setText(String.valueOf(PaneMyLibrary.userId));

        try {
            loadPage("Fxml/PaneBooksTV_users");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void Home(MouseEvent event) throws IOException {
        loadPage("Fxml/PaneBooksTV_users");
    }
    public void myLibrary(MouseEvent event) throws IOException {
        loadPage("Fxml/PaneMyLibrary");
    }
    public void LogOut(MouseEvent event) throws IOException {
        Stage stage ;
        Scene scene ;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Fxml/Login.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    /**
     *Method for the swtiching the slid buttons
     */
    public void loadPage(String page ) throws IOException {
        Parent root = null ;
        try{
            root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource(page + ".fxml"))));
        }
        catch(IOException ignored){
        }
        bp.setCenter(root);
    }
    public void closeButton(MouseEvent event) {
        System.exit(1 );
    }

}
