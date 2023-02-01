package data_pro;

import data_pro.DataBase.Book;
import data_pro.DataBase.RWDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Character.isDigit;

public class PaneBooksTableView implements Initializable {
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
    Text BookEdittedMssg ;

    /**
     * adding the content of the treeMap to an ObservableList when initializing the program
     * to add it then in the Table accordingly
     * */
    private ObservableList<Book> BooksObservableList =  FXCollections.observableArrayList(); ;
    @Override
    public  void initialize(URL url, ResourceBundle resourceBundle) {
        //here we initialize the values of the clumns of the table
        idColumn.setCellValueFactory(new PropertyValueFactory<Book , Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Book , String>("title"));
        quanColumn.setCellValueFactory(new PropertyValueFactory<Book , Integer>("quantity"));

        //we clear the observablelist cuz when we edit any book/user the content Doubled
        //cuz we refresh the observalbe one more time after the editing
        BooksObservableList.clear();
        RWDatabase.BooksList.forEach(
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
                if (RWDatabase.BooksList.containsKey(Integer.parseInt(BookSearchBar.getText()))  && !BookSearchBar.getText().isEmpty()){
                    Book target = RWDatabase.BooksList.get(Integer.parseInt(BookSearchBar.getText()) );
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
    public void delete(MouseEvent event) throws IOException {
        //we can't delete the last book in the database to void errors
        if (RWDatabase.BooksNumber != 1 && booksViewTable.getSelectionModel().getSelectedItem().getId() != 0 && booksViewTable.getSelectionModel().getSelectedItem() != null){
            int selectedID = booksViewTable.getSelectionModel().getSelectedIndex();
            //we remove it from the DateBase first
            RWDatabase.BooksList.remove(booksViewTable.getSelectionModel().getSelectedItem().getId());
            //then remove from the Tableview
            booksViewTable.getItems().remove(selectedID);
            RWDatabase.BooksNumber-- ;
            //write the data on the txt/csv file cuz we dont have REAL dataBase yet
            RWDatabase.doWrite();
        }
//        BookstableViewRefresh() ;
    }
    @FXML
    private void selectedItem(MouseEvent event) {
        if (booksViewTable.getSelectionModel().getSelectedItem() != null){
            miniId.setText(booksViewTable.getSelectionModel().getSelectedItem().getId()+"");
            miniTitle.setText(booksViewTable.getSelectionModel().getSelectedItem().getTitle());
            miniAuthor.setText(booksViewTable.getSelectionModel().getSelectedItem().getAuthor());
            miniQuan.setText(booksViewTable.getSelectionModel().getSelectedItem().getQuantity()+"");
            miniIsbn.setText(booksViewTable.getSelectionModel().getSelectedItem().getIsbn());
            miniPublisher.setText(booksViewTable.getSelectionModel().getSelectedItem().getPublisher());
            miniTotal.setText(booksViewTable.getSelectionModel().getSelectedItem().getTotalPages()+"");
            miniRating.setText(booksViewTable.getSelectionModel().getSelectedItem().getRating()+"");
            miniDate.setText(booksViewTable.getSelectionModel().getSelectedItem().getDate());
        }
    }
    @FXML
    public void editBook(ActionEvent event) throws IOException {
        if (booksViewTable.getSelectionModel().getSelectedItem() != null) {
            RWDatabase.BooksList.get(Integer.parseInt(miniId.getText())).setTitle(miniTitle.getText());
            RWDatabase.BooksList.get(Integer.parseInt(miniId.getText())).setAuthor(miniAuthor.getText());
            RWDatabase.BooksList.get(Integer.parseInt(miniId.getText())).setQuantity(Integer.parseInt(miniQuan.getText()));
            RWDatabase.BooksList.get(Integer.parseInt(miniId.getText())).setIsbn(miniIsbn.getText());
            RWDatabase.BooksList.get(Integer.parseInt(miniId.getText())).setPublisher(miniPublisher.getText());
            RWDatabase.BooksList.get(Integer.parseInt(miniId.getText())).setTotal_pages(Integer.parseInt(miniTotal.getText()));
            RWDatabase.BooksList.get(Integer.parseInt(miniId.getText())).setRating(Float.parseFloat(miniRating.getText()));
            RWDatabase.BooksList.get(Integer.parseInt(miniId.getText())).setPublished_date(miniDate.getText());

            BookEdittedMssg.setText("Book's Data Changed successfully!");
            //make changes on the table view
            int selectedID = booksViewTable.getSelectionModel().getSelectedIndex();
            booksViewTable.getItems().set(selectedID , RWDatabase.BooksList.get(Integer.parseInt(miniId.getText())));
//           BookstableViewRefresh();
            RWDatabase.doWrite();
        }
    }
}
