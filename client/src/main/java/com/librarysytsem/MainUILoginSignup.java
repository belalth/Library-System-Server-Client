
package com.librarysytsem;

import com.librarysytsem.models.* ;

import javafx.beans.binding.BooleanExpression;
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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.TreeMap;


// import static com.librarysytsem.Launcher.socketConnection;

public class MainUILoginSignup  implements Initializable {
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

    public static TreeMap<Integer, Book> BooksList  = new TreeMap<Integer, Book>();
    public static TreeMap<Integer, User> UsersList = new TreeMap<Integer, User>();
    public static HashMap<Integer, LinkedList<Book>> OwnedBooks = new HashMap<Integer, LinkedList<Book>>();

    public static Long TOKEN = 0L;
 ; 
    
    /**
     * Start login window
     * when the user enter the id number and pass.
     * to log in the main menu of users .
     * Or the main menu for Admins.
     * @throws ClassNotFoundException
     */
    public static ObjectInputStream inputFromServer  = null ;
    public static ObjectOutputStream outputToServer  = null   ;
   
    
    public void swithslogin(ActionEvent event) throws IOException, ClassNotFoundException {
        text_loginStart_Windows.setText("");
        HashMap<String , String> usernameNdPassInput  = new HashMap<>();
        boolean isAuthenticated  = false ; 

        String password = textfield_pass_welcome.getText() ;
        String id = textfield_id_welcome.getText();

        usernameNdPassInput.put("username" ,id );
        usernameNdPassInput.put("password" , password);        
    
        if ( (!id.isEmpty() || !password.isEmpty()) && isNumeric(id)) {
            outputToServer.writeObject(usernameNdPassInput);
            outputToServer.flush();
            TOKEN = (Long)inputFromServer.readObject();  
            isAuthenticated = (boolean) inputFromServer.readObject() ; 

       } 
       else {
            text_loginStart_Windows.setText("Wrong Id or password, Please Try Again.");
       }
       if (isAuthenticated && TOKEN != 0L){
            System.out.println(TOKEN);
            loadScene("MainUIAdmin.fxml" , event  ); ; 
            
            
            
       }
    
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

    public void closeButton(MouseEvent event) throws IOException {
        Launcher.socket.close();
        System.exit(1 );
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Launcher.socket = new Socket();
        try {
            Launcher.socket.connect(new InetSocketAddress("localhost", 8000));
            outputToServer = new ObjectOutputStream(Launcher.socket.getOutputStream());
            inputFromServer = new ObjectInputStream(Launcher.socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

}
