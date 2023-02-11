package com.librarysytsem;



import com.librarysytsem.database.*  ;
import java.io.*;
import java.util.*;

public class Database {
    protected final String BOOKS_DATABASE_PATH = "src/main/java/com/librarysytsem/database/books_database.csv";
    protected final String USERS_DATABASE_PATH = "src/main/java/com/librarysytsem/database/users_database.csv";
    protected final String RELATION_DATABASE_PATH = "src/main/java/com/librarysytsem/database/UsersBooksRelationDB.csv";

    protected  TreeMap<Integer, Book> BooksList = new TreeMap<>();
    protected  TreeMap<Integer, User> UsersList = new TreeMap<>();
    protected  HashMap<Integer, LinkedList<Book>> OwnedBooks = new HashMap<>();
    
    Database()  {
        loadBooksAndUsers();
        writeBooksAndUsers();
        loadOwnedBooks();
        writeOwnedBooks();
        System.out.println("Database loaded successfully");
    }


    public  void loadBooksAndUsers() {
        try (Scanner usersIn = new Scanner(new File(USERS_DATABASE_PATH));
             Scanner booksIn = new Scanner(new File(BOOKS_DATABASE_PATH))) {
            while (usersIn.hasNextLine()) {
                String[] tempUsersList = usersIn.nextLine().split(",");
                if (tempUsersList.length != 6) {
                    throw new Exception("User object is corrupted. Can't read user data correctly.");
                }
                int id = Integer.parseInt(tempUsersList[0]);
                if (id != -1) {
                    User user = new User(id, tempUsersList[1], tempUsersList[2], tempUsersList[3], tempUsersList[4], Integer.parseInt(tempUsersList[5]));
                    UsersList.put(id, user);
                }
            }
            while (booksIn.hasNextLine()) {
                String[] tempBooksList = booksIn.nextLine().split(",");
                if (tempBooksList.length != 9) {
                    throw new Exception("Book object is corrupted. Can't read Book data correctly.");
                }
                int id = Integer.parseInt(tempBooksList[0]);
                if (id != -1) {
                    Book book = new Book(id, tempBooksList[1], tempBooksList[2], tempBooksList[3],
                            tempBooksList[4] , Integer.parseInt(tempBooksList[5]) , Float.parseFloat(tempBooksList[6]) , tempBooksList[7] , Integer.parseInt(tempBooksList[8]) );
                    BooksList.put(id, book);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage()+ ":loading users/books data failed ");;
        }
    }

    public  void writeBooksAndUsers() {
        try (BufferedWriter usersOut = new BufferedWriter(new FileWriter(USERS_DATABASE_PATH, false));
             BufferedWriter booksOut = new BufferedWriter(new FileWriter(BOOKS_DATABASE_PATH, false))) {
            UsersList.forEach((key, value) -> {
                if (value.getId() != -1) {
                    try {
                        usersOut.write(value.getId() + "," + value.getEmail() + "," + value.getPassword() + "," + value.getFirstName() + "," + value.getLastName() + "," + value.getAge() + "\n");
                    } catch (IOException e) {
                      System.out.println(e.getMessage() ); ;
                    }
                }
            });
            BooksList.forEach((key, value) -> {
                if (value.getId() != -1) {
                    try {
                        booksOut.write(value.getId() + "," + value.getTitle() + "," + value.getAuthor() + "," + value.getIsbn() + "," + value.getPublisher() + "," + value.getTotalPages() + "," + value.getRating() + "," + value.getPublishedDate() + "," + value.getQuantity() + "\n");
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            });
        }catch(IOException e) {
            System.out.println(e.getMessage() + ":writing on books/users datavase failed");
        }
    }

    public  void loadOwnedBooks() {
        try(Scanner input = new Scanner(new File(RELATION_DATABASE_PATH))){
        while(input.hasNext())  {
            String[] tempList = input.nextLine().split(",") ;
            LinkedList<Book> usersArr = new LinkedList<>( );
            for(int i = 1 ; i < tempList.length ;  i++){
                usersArr.add(BooksList.get(Integer.parseInt(tempList[i]))) ;
            }
            OwnedBooks.put(Integer.parseInt(tempList[0]) ,usersArr);
        }
     }catch(Exception e){
        System.out.println(e.getMessage() + ":loading UsersBooksRelationDB.csv failed"  );
    }
}

    public  void writeOwnedBooks()   {
        try (FileWriter fileWriter = new FileWriter(RELATION_DATABASE_PATH, false);
             BufferedWriter out = new BufferedWriter(fileWriter)) {
            OwnedBooks.forEach((key, value) -> {
                try {
                    out.write(key + ",");
                    for (Book book : value) {
                        out.write(book.getId() + ",");
                    }
                    out.write("\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }catch(IOException e) {
            System.out.println(e.getMessage()  + ":write to owned books db failed");
        }
    }
}
