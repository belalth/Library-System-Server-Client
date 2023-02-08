package com.librarysytsem; 


import com.librarysytsem.models.Book;
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
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import static com.librarysytsem.MainUILoginSignup.BooksList;
import static com.librarysytsem.MainUILoginSignup.OwnedBooks;
import static java.lang.Character.isDigit;

public class PaneUserTableView_users implements Initializable {
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
    TableView<Book> booksViewTable ;
    @FXML
    TableColumn<Book , Integer> idColumn ;
    @FXML
    TableColumn<Book , String> nameColumn  ;;
    @FXML
    TableColumn<Book , Integer> quanColumn ; ;
    @FXML
    TextField BookSearchBar ;
    @FXML
    Text textMessage ;


    /**
     * adding the content of the treeMap to an ObservableList when initializing the program
     * to add it then in the Table accordingly
     * */
    public ObservableList<Book> BooksObservableList =  FXCollections.observableArrayList(); ;
    @Override
    public  void initialize(URL url, ResourceBundle resourceBundle) {
        //here we initialize the values of the clumns of the table
        idColumn.setCellValueFactory(new PropertyValueFactory<Book , Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Book , String>("title"));
        quanColumn.setCellValueFactory(new PropertyValueFactory<Book , Integer>("quantity"));

        //we clear the observablelist cuz when we edit any book/user the content Doubled
        //cuz we refresh the observalbe one more time after the editing
        BooksObservableList.clear();
        BooksList.forEach(
                (key, value) -> {
                    BooksObservableList.add(value);
                }
        );
        //adding items to the table to display them
        booksViewTable.setItems(BooksObservableList);

        /**
         * The Searching algorithm for id's -> O(log(n)) , for title isbn etc any string the complexity -> O(n).
         * we add for the search bar A listener to observe any changes of typing.
         *then we match the result of the search bar and the values in the database.
         * and after every secessful search operation we return to the normal TableView from the ..
         * BooksObservableList above with O(1) complexity cuz we have the list already :)
         * */
        BookSearchBar.textProperty().addListener(a -> {
            ObservableList<Book> filterdList = FXCollections.observableArrayList();
            if ( !BookSearchBar.getText().equals("") && !isString(BookSearchBar.getText())  ){
                if (BooksList.containsKey(Integer.parseInt(BookSearchBar.getText()))  && !BookSearchBar.getText().isEmpty()){
                    Book target = BooksList.get(Integer.parseInt(BookSearchBar.getText()) );
                    filterdList.add(target);
                    booksViewTable.setItems(filterdList);
            }
            }else if (BookSearchBar.getText().equals("")){
                booksViewTable.setItems(BooksObservableList);
            }
            BooksObservableList.forEach(e -> {
                if (BookSearchBar.getText().equalsIgnoreCase(e.getTitle()) ||
                        BookSearchBar.getText().equalsIgnoreCase(e.getAuthor().toLowerCase())||
                           BookSearchBar.getText().equalsIgnoreCase(e.getIsbn().toLowerCase())){
                    filterdList.add(e);
                    booksViewTable.setItems(filterdList);
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

    @FXML
    private void BuyBook(MouseEvent event) throws IOException, InterruptedException {
        if (OwnedBooks.get(PaneMyLibrary.userId).contains(booksViewTable.getSelectionModel().getSelectedItem())){
            textMessage.setFill(Paint.valueOf("red"));
            textMessage.setText("You Have This Book Already!");
        }
         else if (booksViewTable.getSelectionModel().getSelectedItem() != null){
            //adding the book to the library of user
            OwnedBooks.get(PaneMyLibrary.userId).add(booksViewTable.getSelectionModel().getSelectedItem());
            //using the dicresing method to dicrease books quantity-1
            BooksList.get(booksViewTable.getSelectionModel().getSelectedItem().getId()).decreaseQuantity();
            //writing the edited book with quantity-1
            doWrite();
            //write the relation DB
            writeOwnedBooks();
            //make changes on the tableView
            int selectedID = booksViewTable.getSelectionModel().getSelectedIndex();
            booksViewTable.getItems().set(selectedID , BooksList.get(Integer.parseInt(miniId.getText())));

            textMessage.setFill(Paint.valueOf("green"));
            textMessage.setText("Book Purchased Successfully!");


        }
    }
    @FXML
    private void selectedItem(MouseEvent event) {
        //to change the text of the purchesed message
        textMessage.setText("");
        if (booksViewTable.getSelectionModel().getSelectedItem() != null){
            miniId.setText(booksViewTable.getSelectionModel().getSelectedItem().getId()+"");
            miniTitle.setText(booksViewTable.getSelectionModel().getSelectedItem().getTitle());
            miniAuthor.setText(booksViewTable.getSelectionModel().getSelectedItem().getAuthor());
            miniQuan.setText(booksViewTable.getSelectionModel().getSelectedItem().getQuantity()+"");
            miniIsbn.setText(booksViewTable.getSelectionModel().getSelectedItem().getIsbn());
            miniPublisher.setText(booksViewTable.getSelectionModel().getSelectedItem().getPublisher());
            miniTotal.setText(booksViewTable.getSelectionModel().getSelectedItem().getTotalPages()+"");
            miniRating.setText(booksViewTable.getSelectionModel().getSelectedItem().getRating()+"");
            miniDate.setText(booksViewTable.getSelectionModel().getSelectedItem().getPublishedDate());
        }
    }
}
