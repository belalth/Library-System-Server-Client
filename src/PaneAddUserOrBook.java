package data_pro;

import data_pro.DataBase.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import java.io.*;
import java.util.LinkedList;


public class PaneAddUserOrBook {
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
    @FXML
    private Label print_create;
    @FXML
    private TextField title_new_book ;
    @FXML
    private TextField auther_new_book ;
    @FXML
    private TextField isbn_new_book ;
    @FXML
    private TextField publisher_new_book ;
    @FXML
    private  TextField total_new_book;
    @FXML
    private  TextField rating_new_book;
    @FXML
    private  TextField date_new_book;
    @FXML
    private  TextField quantity_new_book ;
    @FXML
    private Text errorMessage ;

    /**
     * adding a new user in the data base from admin scene
     *
     * */
    public void addNewUser(ActionEvent event) throws IOException, InterruptedException {

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
        //if the conditions of adding new user are applied we add a new user object to RWDB
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
            //adding the uesr id to the relation file
            RWDatabase.OwnedBooks.put(tempUser.getId(), new LinkedList<>());
            //here is adding the user data to the database :
            RWDatabase.writeUsersData();
            RWDatabase.writeOwnedBooks();
            print_create.setText("Account Created Successfully :) ");
        }else{
            print_create.setText("Error ! try again");
        }
    }

    /**
     * Adding a new Book to the books list in RDatabase
     */
    public void AddNewBook(ActionEvent event)throws Exception    {
        int randomeId = (int) (Math.random() * 100000)  ;
        if (
                title_new_book.getText().isEmpty()||
                auther_new_book.getText().isEmpty() ||
                isbn_new_book.getText().isEmpty()||
                publisher_new_book.getText().isEmpty()||
                total_new_book.getText().isEmpty() ||
                rating_new_book.getText().isEmpty()||
                date_new_book.getText().isEmpty() ||
                quantity_new_book.getText().isEmpty()


        )
            errorMessage.setText("Please Fill The Empty Slots! ");
        else if (RWDatabase.BooksList.containsKey(randomeId))
            errorMessage.setText("Something happened from our side :( please try again.  ");
        else{
            RWDatabase.BooksList.put(randomeId, new Book(
                            randomeId ,
                            title_new_book.getText(),
                            auther_new_book.getText() ,
                            isbn_new_book.getText() ,
                            publisher_new_book.getText(),
                            Integer.parseInt(total_new_book.getText()),
                            Float.parseFloat(rating_new_book.getText()),
                            date_new_book.getText(),
                            Integer.parseInt(quantity_new_book.getText())
                    )
            );
            RWDatabase.BooksNumber++ ;
            RWDatabase.doWrite();
            errorMessage.setText("Book stored successfully :) ");
        }
    }
}
