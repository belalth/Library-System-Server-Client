package data_pro;

import data_pro.DataBase.RWDatabase;
import data_pro.DataBase.Users;
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
    TableView<Users> usersTableView;
    @FXML
    TableColumn<Users , Integer> id ;
    @FXML
    TableColumn<Users , String> name  ;;
    @FXML
    TableColumn<Users , String> quan ; ;
    @FXML
    TextField usersSearchBar;
    @FXML
    Text userChanged ;


    private final ObservableList<Users> usersObservableList = FXCollections.observableArrayList();
    @Override
    public  void initialize(URL url, ResourceBundle resourceBundle) {
        //here we initialize the values of the clumns of the table
        id.setCellValueFactory(new PropertyValueFactory<Users , Integer>("id"));
        name.setCellValueFactory(new PropertyValueFactory<Users , String>("gmail"));
        quan.setCellValueFactory(new PropertyValueFactory<Users , String>("first"));
        /**
         * adding the content of the treeMap to an ObservableList
         * to add it then in the Table accordingly
         * */
        usersObservableList.clear(); ;
        RWDatabase.UsersList.forEach(
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
            ObservableList<Users> filterdList = FXCollections.observableArrayList();
            if ( !usersSearchBar.getText().equals("") && !isString(usersSearchBar.getText())  ){
                if (RWDatabase.UsersList.containsKey(Integer.parseInt(usersSearchBar.getText()))  && !usersSearchBar.getText().isEmpty()){
                    Users target = RWDatabase.UsersList.get(Integer.parseInt(usersSearchBar.getText()) );
                    filterdList.add(target);
                    usersTableView.setItems(filterdList);
                }
            }else if (usersSearchBar.getText().equals("")){
                usersTableView.setItems(usersObservableList);
            }
            usersObservableList.forEach(e -> {
                if (usersSearchBar.getText().equalsIgnoreCase(e.getFirst()) ||
                        usersSearchBar.getText().equalsIgnoreCase(e.getLast().toLowerCase())||
                        usersSearchBar.getText().equalsIgnoreCase(e.getGmail().toLowerCase())){
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
        if (usersTableView.getSelectionModel().getSelectedItem() != null && !RWDatabase.UsersList.isEmpty())
       {
            int selectedID = usersTableView.getSelectionModel().getSelectedIndex();
            //remove the uesr from the database
            RWDatabase.UsersList.remove(usersTableView.getSelectionModel().getSelectedItem().getId());
            //remove the user from the relations dataBase
           RWDatabase.OwnedBooks.remove(usersTableView.getSelectionModel().getSelectedItem().getId());
            usersTableView.getItems().remove(selectedID);
            RWDatabase.writeUsersData();
            RWDatabase.writeOwnedBooks();
        }
    }


    public void editUser(MouseEvent event) throws IOException {
        if (usersTableView.getSelectionModel().getSelectedItem() != null) {
            RWDatabase.UsersList.get(Integer.parseInt(showId.getText())).setGmail(showGmail.getText());
            RWDatabase.UsersList.get(Integer.parseInt(showId.getText())).setPassword(showPass.getText());
            RWDatabase.UsersList.get(Integer.parseInt(showId.getText())).setFirst(showFirst.getText());
            RWDatabase.UsersList.get(Integer.parseInt(showId.getText())).setLast(showLast.getText());
            RWDatabase.UsersList.get(Integer.parseInt(showId.getText())).setAge(Integer.parseInt(showAge.getText()));

            userChanged.setText("User's Data Changed Successfully!");
            int selectedID = usersTableView.getSelectionModel().getSelectedIndex();
            //we edit directly to the table view without any refreshing or more complexity
            usersTableView.getItems().set(selectedID , RWDatabase.UsersList.get(Integer.parseInt(showId.getText())));
            //write the data to the database in sure that its complexity is not accountable
            RWDatabase.writeUsersData();
        }
    }

    public void selectedItem(MouseEvent event) {
        if (usersTableView.getSelectionModel().getSelectedItem() != null) {
        //showSth is the textfield that we display to the user and we get the data from the usersTableview
        showId.setText(usersTableView.getSelectionModel().getSelectedItem().getId()+"");
        showGmail.setText(usersTableView.getSelectionModel().getSelectedItem().getGmail());
        showPass.setText(usersTableView.getSelectionModel().getSelectedItem().getPassword());
        showFirst.setText(usersTableView.getSelectionModel().getSelectedItem().getFirst()+"");
        showLast.setText(usersTableView.getSelectionModel().getSelectedItem().getLast());
        showAge.setText(usersTableView.getSelectionModel().getSelectedItem().getAge()+"");
    }}
}
