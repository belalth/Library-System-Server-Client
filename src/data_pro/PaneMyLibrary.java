package data_pro;

import data_pro.DataBase.Book;
import data_pro.DataBase.RWDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;



public class PaneMyLibrary implements Initializable {
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
    private TextField miniRating;
    @FXML
    private TextField miniTitle;
    @FXML
    private TextField miniTotal;
    @FXML
    TableView<Book> booksViewTable ;
    @FXML
    TableColumn<Book , Integer> idColumn ;
    @FXML
    TableColumn<Book , String> nameColumn  ;;
    @FXML
    TableColumn<Book , String> authorColumn ; ;


    public static  int userId ;
    public static  String name ;
    public static void initializeId(int id , String n){
        userId = id  ;
        name = n ;

    }

    public ObservableList<Book> BooksObservableList =  FXCollections.observableArrayList(); ;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //here we initialize the values of the columns of the table
        idColumn.setCellValueFactory(new PropertyValueFactory<Book , Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Book , String>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<Book , String>("author"));
        //clear the observable list avoiding problems
        BooksObservableList.clear();
        //get the owned arrayList of books from the database
        LinkedList<Book> listOfOwnedBooks = RWDatabase.OwnedBooks.get(userId);
        //add all the element in the arrayList to the observableList
        BooksObservableList.addAll(listOfOwnedBooks);
        //adding items to the table to display them
        booksViewTable.setItems(BooksObservableList);
    }

    //whem selecet element from the table it will display the content of Book/user in the right slots
    @FXML
    private void selectedItem(MouseEvent event) {
        if (booksViewTable.getSelectionModel().getSelectedItem() != null){
            miniId.setText(booksViewTable.getSelectionModel().getSelectedItem().getId()+"");
            miniTitle.setText(booksViewTable.getSelectionModel().getSelectedItem().getTitle());
            miniAuthor.setText(booksViewTable.getSelectionModel().getSelectedItem().getAuthor());
            miniIsbn.setText(booksViewTable.getSelectionModel().getSelectedItem().getIsbn());
            miniPublisher.setText(booksViewTable.getSelectionModel().getSelectedItem().getPublisher());
            miniTotal.setText(booksViewTable.getSelectionModel().getSelectedItem().getTotalPages()+"");
            miniRating.setText(booksViewTable.getSelectionModel().getSelectedItem().getRating()+"");
            miniDate.setText(booksViewTable.getSelectionModel().getSelectedItem().getDate());
        }
    }
    @FXML
    public void DeleteBook(MouseEvent event) throws IOException {
        //we can't delete the last book in the database to void errors
        if (RWDatabase.OwnedBooks.get(userId).size() != 1  && booksViewTable.getSelectionModel().getSelectedItem() != null){
            int selectedID = booksViewTable.getSelectionModel().getSelectedIndex();
            //we remove it from the DateBase first
            RWDatabase.OwnedBooks.get(userId).remove(booksViewTable.getSelectionModel().getSelectedItem());
            //then remove from the Tableview
            booksViewTable.getItems().remove(selectedID);
            //write the data on the txt/csv file cuz we dont have REAL dataBase yet
            RWDatabase.writeOwnedBooks();
        }
//        BookstableViewRefresh() ;
    }

}
