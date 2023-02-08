package com.librarysytsem; 

import com.librarysytsem.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import static com.librarysytsem.MainUILoginSignup.OwnedBooks;
import static com.librarysytsem.MainUILoginSignup.UsersList;
import static java.lang.Character.isDigit;

public class PaneUsersTableView implements Initializable {
    @FXML
    private TextField showAge;
    @FXML
    private TextField showFirst;
    @FXML
    private TextField showGmail;
    @FXML
    private TextField showId;
    @FXML
    private TextField showLast;
    @FXML
    private TextField showPass;
    @FXML
    TableView<User> usersTableView;
    @FXML
    TableColumn<User , Integer> id ;
    @FXML
    TableColumn<User , String> name  ;;
    @FXML
    TableColumn<User , String> quan ; ;
    @FXML
    TextField usersSearchBar;
    @FXML
    Text userChanged ;


    private final ObservableList<User> usersObservableList = FXCollections.observableArrayList();
    @Override
    public  void initialize(URL url, ResourceBundle resourceBundle) {
        //here we initialize the values of the clumns of the table
        id.setCellValueFactory(new PropertyValueFactory<User , Integer>("id"));
        name.setCellValueFactory(new PropertyValueFactory<User , String>("gmail"));
        quan.setCellValueFactory(new PropertyValueFactory<User , String>("first"));
        /**
         * adding the content of the treeMap to an ObservableList
         * to add it then in the Table accordingly
         * */
        usersObservableList.clear(); ;
        UsersList.forEach(
                (key, value) -> {
                    usersObservableList.add(value);
                }
        );
        usersTableView.setItems(usersObservableList);

        /**
         * The Searching algorithm for id's -> O(log(n)) , for title isbn etc any string the complexity -> O(n).
         * we add for the search bar A listener to observe any changes of typing.
         *then we match the result of the search bar and the values in the database.
         * and after every secessful search operation we return to the normal TableView from the ..
         * BooksObservableList above with O(1) complexity cuz we have the list already :)
         * */
        usersSearchBar.textProperty().addListener(a -> {
            ObservableList<User> filterdList = FXCollections.observableArrayList();
            if ( !usersSearchBar.getText().equals("") && !isString(usersSearchBar.getText())  ){
                if (UsersList.containsKey(Integer.parseInt(usersSearchBar.getText()))  && !usersSearchBar.getText().isEmpty()){
                    User target = UsersList.get(Integer.parseInt(usersSearchBar.getText()) );
                    filterdList.add(target);
                    usersTableView.setItems(filterdList);
                }
            }else if (usersSearchBar.getText().equals("")){
                usersTableView.setItems(usersObservableList);
            }
            usersObservableList.forEach(e -> {
                if (usersSearchBar.getText().equalsIgnoreCase(e.getFirstName()) ||
                        usersSearchBar.getText().equalsIgnoreCase(e.getLastName().toLowerCase())||
                        usersSearchBar.getText().equalsIgnoreCase(e.getEmail().toLowerCase())){
                    filterdList.add(e);
                    usersTableView.setItems(filterdList);
                }
            });

        });

    }
    private boolean isString(String str){
        for (int i = 0 ; i <str.length() ; i++){
            if (isDigit(str.charAt(i)))
                return false ;
        }
        return true ;
    }


    public void deleteUser(MouseEvent event) throws IOException {
        if (usersTableView.getSelectionModel().getSelectedItem() != null && !UsersList.isEmpty())
       {
            int selectedID = usersTableView.getSelectionModel().getSelectedIndex();
            //remove the uesr from the database
            UsersList.remove(usersTableView.getSelectionModel().getSelectedItem().getId());
            //remove the user from the relations dataBase
           OwnedBooks.remove(usersTableView.getSelectionModel().getSelectedItem().getId());
            usersTableView.getItems().remove(selectedID);
            writeUsersData();
            writeOwnedBooks();
        }
    }


    public void editUser(MouseEvent event) throws IOException {
        if (usersTableView.getSelectionModel().getSelectedItem() != null) {
            UsersList.get(Integer.parseInt(showId.getText())).setEmail(showGmail.getText());
            UsersList.get(Integer.parseInt(showId.getText())).setPassword(showPass.getText());
            UsersList.get(Integer.parseInt(showId.getText())).setFirstName(showFirst.getText());
            UsersList.get(Integer.parseInt(showId.getText())).setLastName(showLast.getText());
            UsersList.get(Integer.parseInt(showId.getText())).setAge(Integer.parseInt(showAge.getText()));

            userChanged.setText("User's Data Changed Successfully!");
            int selectedID = usersTableView.getSelectionModel().getSelectedIndex();
            //we edit directly to the table view without any refreshing or more complexity
            usersTableView.getItems().set(selectedID , UsersList.get(Integer.parseInt(showId.getText())));
            //write the data to the database in sure that its complexity is not accountable
            writeUsersData();
        }
    }

    public void selectedItem(MouseEvent event) {
        if (usersTableView.getSelectionModel().getSelectedItem() != null) {
        //showSth is the textfield that we display to the user and we get the data from the usersTableview
        showId.setText(usersTableView.getSelectionModel().getSelectedItem().getId()+"");
        showGmail.setText(usersTableView.getSelectionModel().getSelectedItem().getEmail());
        showPass.setText(usersTableView.getSelectionModel().getSelectedItem().getPassword());
        showFirst.setText(usersTableView.getSelectionModel().getSelectedItem().getFirstName()+"");
        showLast.setText(usersTableView.getSelectionModel().getSelectedItem().getLastName());
        showAge.setText(usersTableView.getSelectionModel().getSelectedItem().getAge()+"");
    }}
}
