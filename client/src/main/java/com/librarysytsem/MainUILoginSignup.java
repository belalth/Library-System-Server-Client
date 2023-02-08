
package com.librarysytsem;

import com.librarysytsem.models.* ;
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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.TreeMap;



public class MainUILoginSignup implements Initializable {
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

    public static TreeMap<Integer, Book> BooksList = new TreeMap<>();
    public static TreeMap<Integer, User> UsersList = new TreeMap<>();
    public static HashMap<Integer, LinkedList<Book>> OwnedBooks = new HashMap<>();
    public static HashMap<String , String> usernameNdPassInput  = new HashMap<>();
    /**
     * Start login window
     * when the user enter the id number and pass.
     * to log in the main menu of users .
     * Or the main menu for Admins.
     */
    public void swithslogin(ActionEvent event) throws IOException {
        String id = textfield_id_welcome.getText();
        String password = textfield_pass_welcome.getText();
        usernameNdPassInput.put(id , password);

        

        

       
//        if (id.isEmpty() || password.isEmpty()) {
//            text_loginStart_Windows.setText("Please fill The Empty slots!");
//        } else if (UsersList.containsKey(Integer.parseInt(id)) && password.equals(UsersList.get(Integer.parseInt(id)).getPassword())) {
//            PaneMyLibrary.initializeId(Integer.parseInt(id), UsersList.get(Integer.parseInt(id)).getFirstName());
//            loadScene("MainUIUsers.fxml", event);
//        } else if (id.equals("1111") && password.equals("admin")) {
//            loadScene("MainUIAdmin.fxml", event);
//        } else {
//            text_loginStart_Windows.setText("Wrong Id or password, Please Try Again.");
//        }
    }

    private void loadScene(String fxml, ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void SwitchToSignUp(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("SignUp.fxml")));
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
//        writeUsersData();
//        writeOwnedBooks();

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

    private void loadLoginScreen(ActionEvent event) throws IOException, InterruptedException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        Thread.sleep(200);
        stage.setScene(scene);
        stage.show();
    }

    public void signUp_back_button(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void closeButton(MouseEvent event) {
        System.exit(1 );
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        try(Socket socket = new Socket("localhost", 8000)){
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            // outputStream.writeObject(usernameNdPassInput);

            DataInputStream token = new DataInputStream(socket.getInputStream());
            DataInputStream approved = new DataInputStream(socket.getInputStream());

            if (approved.readBoolean()){
                System.out.println("approved");
                System.out.println(token.readLong());
            }

        }catch (IOException e){
        System.out.println("MainUILoginSignup.initialize()");
    }
    }
}
