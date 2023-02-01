package data_pro;

import data_pro.DataBase.RWDatabase;
import data_pro.DataBase.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;


public class MainUILoginSignup {
    @FXML
    private TextField textfield_id_welcome;
    @FXML
    private TextField textfield_pass_welcome;
    @FXML
    private Text text_loginStart_Windows ;

    @FXML
    private Label print_create;
    @FXML
    private TextField textfield_id_create;
    @FXML
    private TextField Textfirst_create;
    @FXML
    private TextField Textsecond_create;
    @FXML
    private TextField Textgmail_create;
    @FXML
    private TextField Textage_create;
    @FXML
    private TextField Textpass_create;
    private Stage stage;
    private Scene scene;

    /**
     * Start login window
     * when the user enter the id number and pass.
     * to log in the main menu of users .
     * Or the main menu for Admins.
     */
    public void swithslogin(ActionEvent event) throws IOException {
        if (textfield_id_welcome.getText().isEmpty() || textfield_pass_welcome.getText().isEmpty())
            text_loginStart_Windows.setText("Please fill The Empty slots!");
        else if(RWDatabase.UsersList.containsKey(Integer.parseInt(textfield_id_welcome.getText())) && textfield_pass_welcome.getText().equals(RWDatabase.UsersList.get(Integer.parseInt(textfield_id_welcome.getText())).getPassword()) ){
            PaneMyLibrary.initializeId(Integer.parseInt(textfield_id_welcome.getText()) , RWDatabase.UsersList.get(Integer.parseInt(textfield_id_welcome.getText())).getFirst());
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Fxml/MainUIUsers.fxml")));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }else if (Integer.parseInt(textfield_id_welcome.getText())==1111 && textfield_pass_welcome.getText().equals("admin" + "" )){
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Fxml/MainUIAdmin.fxml")));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
//            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        }
        else
            text_loginStart_Windows.setText("Wrong Id or password, Please Try Again.");
    }

    public void SwitchToSignUp(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Fxml/SignUp.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void loginTextFieldAction(){

    }

    /**
     * Create a new user Windows
     * you need to abstract the id text field to make it only for Integers
     * maximum id integer digits is 5
     *
     */
    public void SignUpButton(ActionEvent event)throws Exception{
        if (
                textfield_id_create.getText().isEmpty() ||
                        Textgmail_create.getText().isEmpty()||
                        Textpass_create.getText().isEmpty()||
                        Textfirst_create.getText().isEmpty()||
                        Textsecond_create.getText().isEmpty()||
                        Textage_create.getText().isEmpty()
        ){
            print_create.setText("Please fill the empty slots ! ");
        }
        else if (RWDatabase.UsersList.containsKey(Integer.parseInt(textfield_id_create.getText())))
            print_create.setText("User id already exist ! ");
        else if (

                Integer.parseInt(Textage_create.getText())>0 &&
                        Integer.parseInt(Textage_create.getText())<100

        ){

            Users tempUser = new Users( Integer.parseInt(textfield_id_create.getText()) ,
                    Textgmail_create.getText(),
                    Textpass_create.getText(),
                    Textfirst_create.getText(),
                    Textsecond_create.getText() ,
                    Integer.parseInt(Textage_create.getText()));
            //here we add a new user object to the maptree that we made earliear
            RWDatabase.UsersList.put(tempUser.getId() ,tempUser );
            //add the id to the ralationd db
            RWDatabase.OwnedBooks.put(tempUser.getId(), new LinkedList<>());
            //here is adding the user data to the database :
            RWDatabase.writeUsersData();
            //write the relation db
            RWDatabase.writeOwnedBooks();


            print_create.setText("Account Created Successfully :) ");
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Fxml/Login.fxml")));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            Thread.sleep( 200   );
//            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        }else{
            print_create.setText("Error ! try again");
        }
    }

    public void signUp_back_button(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Fxml/Login.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void closeButton(MouseEvent event) {
        System.exit(1 );
    }
}
