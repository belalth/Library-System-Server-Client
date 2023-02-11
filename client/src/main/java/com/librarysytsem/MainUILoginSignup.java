
package com.librarysytsem;


import com.librarysytsem.database.Book;
import com.librarysytsem.database.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.TreeMap;



public class MainUILoginSignup implements Initializable {

    @FXML private TextField textfield_id_welcome;
    @FXML private TextField textfield_pass_welcome;
    @FXML private Text text_loginStart_Windows;
    @FXML private Label print_create;
    @FXML private TextField textfield_id_create;
    @FXML private TextField Textfirst_create;
    @FXML private TextField Textsecond_create;
    @FXML private TextField Textgmail_create;
    @FXML private TextField Textage_create;
    @FXML private TextField Textpass_create;

    public static TreeMap<Integer, Book> BooksList;
    public static TreeMap<Integer, User> UsersList;
    public static HashMap<Integer, LinkedList<Book>> OwnedBooks;
    public static HashMap<String, String> usernameNdPassInput;

    public static Long TOKEN = 0L;

    public void swithslogin(ActionEvent event) throws IOException, ClassNotFoundException {
        if (Launcher.connection.isClosed()) {
            Launcher.connection.reconnect();
        }

        text_loginStart_Windows.setText("");
        usernameNdPassInput = new HashMap<>();
        boolean checkOne = false;
        boolean checkTwo = false;

        String password = textfield_pass_welcome.getText();
        String id = textfield_id_welcome.getText();

        usernameNdPassInput.put("username", id);
        usernameNdPassInput.put("password", password);

        if (!id.isEmpty() || !password.isEmpty() && isNumeric(id)) {
            Launcher.connection.sendData(usernameNdPassInput);
            TOKEN = (Long) Launcher.connection.receiveData();
            checkOne = (boolean) Launcher.connection.receiveData();
            checkTwo = (boolean) Launcher.connection.receiveData();
        } else {
            text_loginStart_Windows.setText("Wrong Id or password, Please Try Again.");
        }

        if (checkOne && checkTwo && TOKEN == 111L) {
            receiveData();
            PaneMyLibrary.initializeId(Integer.parseInt(usernameNdPassInput.get("username")), usernameNdPassInput.get("password"));
            loadScene("MainUIAdmin.fxml", event);
        } else if (checkOne && TOKEN != 0L) {
            receiveData();
            PaneMyLibrary.initializeId(Integer.parseInt(usernameNdPassInput.get("username" )), usernameNdPassInput.get("password" ));
            loadScene("MainUIUsers.fxml" , event  ); ;          
        }
    
    } 
    private void receiveData() throws IOException, ClassNotFoundException {
        UsersList = (TreeMap<Integer, User>) Launcher.connection.receiveData();
        BooksList = (TreeMap<Integer, Book>) Launcher.connection.receiveData();
        OwnedBooks = (HashMap<Integer, LinkedList<Book>>) Launcher.connection.receiveData();
    }
    
    /**
     * Create a new user Windows
     * you need to abstract the id text field to make it only for Integers
     * maximum id integer digits is 5 */
    public static boolean isNumeric(String str) { 
        try {  
          Double.parseDouble(str);  
          return true;
        } catch(NumberFormatException e){  
          return false;  
        }  
      }


    private void loadScene(String fxml, ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void SwitchToSignUp(ActionEvent event) throws IOException {
        loadScene("SignUp.fxml", event);
    }
    
   
    public void SignUpButton(ActionEvent event) throws Exception {
        if (isSignUpFormEmpty()) {
            print_create.setText("Please fill the empty slots!");
            return;
        }
    
        int userId = Integer.parseInt(textfield_id_create.getText());
        if (UsersList.containsKey(userId)) {
            print_create.setText("User id already exists!");
            return;
        }
    
        int age = Integer.parseInt(Textage_create.getText());
        if (age <= 0 || age >= 100) {
            print_create.setText("Error! Try again");
            return;
        }
    
        User tempUser = new User(
                userId,
                Textgmail_create.getText(),
                Textpass_create.getText(),
                Textfirst_create.getText(),
                Textsecond_create.getText(),
                age
        );
    
        UsersList.put(tempUser.getId(), tempUser);
        OwnedBooks.put(tempUser.getId(), new LinkedList<>());
    
        print_create.setText("Account created successfully :)");
        loadLoginScreen(event);
    }
    
    private boolean isSignUpFormEmpty() {
        return textfield_id_create.getText().isEmpty() ||
                Textgmail_create.getText().isEmpty() ||
                Textpass_create.getText().isEmpty() ||
                Textfirst_create.getText().isEmpty() ||
                Textsecond_create.getText().isEmpty() ||
                Textage_create.getText().isEmpty();
    }
    
    private void loadLoginScreen(ActionEvent event) throws IOException {
        loadScene("Login.fxml", event);
    }
    
    public void signUp_back_button(ActionEvent event) throws IOException {
        loadScene("Login.fxml", event);
    }
    
    public void closeButton(MouseEvent event) throws IOException {
        Launcher.connection.closeConnection();
        System.exit(1 );
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
       
        
    }
    @FXML
    public void loginTextFieldAction(){
    
    }
    
}
