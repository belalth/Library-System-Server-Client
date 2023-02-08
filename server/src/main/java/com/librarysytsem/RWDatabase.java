package com.librarysytsem;


import com.librarysytsem.database.* ;

import java.io.*;
import java.util.*;

public class RWDatabase {
    public static int BooksNumber;
    public static TreeMap<Integer, Book> BooksList = new TreeMap<>();
    public static TreeMap<Integer, Users> UsersList = new TreeMap<>();
    public static HashMap<Integer, LinkedList<Book>> OwnedBooks = new HashMap<>();
    private static final String bookPath = "src/main/java/com/librarysytsem/database/BooksDB.txt";
    private static final String userPath = "src/main/java/com/librarysytsem/database/UsersDB.csv";
    private static final String relationPath = "src/main/java/com/librarysytsem/database/UsersBooksRelationDB.csv";





    public static void main(String[] args) throws Exception {
        reader();
        doWrite();
        readUsersData() ;
        readOwnedBooks();
        writeOwnedBooks();
        System.out.println("TEST PASS");
//        Date dateCreated = new java.util.Date();
//        System.out.println(dateCreated);
    }
    public static void writeUsersData() throws IOException {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(userPath, false))) {
            UsersList.forEach((key, value) -> {
                if (value.getId() != -1) {
                    try {
                        out.write(value.getId() + "," + value.getGmail() + "," + value.getPassword() + "," + value.getFirst() + "," + value.getLast() + "," + value.getAge() + "\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    public static void doWrite() throws IOException {
        if (BooksList == null) {
            throw new NullPointerException("THERE IS NO DATA IN THE TREEMAP TO WRITE!");
        }
        try (BufferedWriter out = new BufferedWriter(new FileWriter(bookPath))) {
            out.write(BooksNumber + "\n");
        }
        for (Book e : BooksList.values()) {
            writer(e, bookPath);
        }
    }


    public static void reader( ) throws FileNotFoundException{
        Scanner input = new Scanner(new File(bookPath) );
        BooksNumber = Integer.parseInt(input.nextLine() );

        for(int i =  0 ; i < BooksNumber ; i++ ){
            input.nextLine() ;
            Book tempBook = new Book(
                    Integer.parseInt(input.nextLine()),
                    input.nextLine().substring(18),
                    input.nextLine().substring(18) ,
                    input.nextLine().substring(18) ,
                    input.nextLine().substring(18) ,
                    Integer.parseInt(input.nextLine().substring(18)),
                    Float.parseFloat(input.nextLine().substring(18)) ,
                    input.nextLine().substring(18),
                    Integer.parseInt(input.nextLine().substring(18)) ) ;

            BooksList.put(tempBook.getId() ,tempBook   );
        }
        BooksNumber = BooksList.size();


    }

    public static void writer(Book bookOb , String pathName) throws IOException {
        FileWriter fileWriter = new FileWriter(pathName, true);
        BufferedWriter out = new BufferedWriter(fileWriter);
        out.write("\n");
        out.write( bookOb.getId() + "\n" );
        out.write("title          -> "+bookOb.getTitle()+ "\n");
        out.write("author         -> "+bookOb.getAuthor()+ "\n");
        out.write("isbn           -> "+bookOb.getIsbn()+ "\n");
        out.write("publisher      -> "+bookOb.getPublisher()+ "\n");
        out.write("total_pages    -> "+bookOb.getTotalPages()+ "\n");
        out.write("rating         -> "+bookOb.getRating()+ "\n");
        out.write("published_date -> "+bookOb.getDate()+ "\n");
        out.write("quantity       -> "+bookOb.getQuantity()+ "\n");
        out.close();
    }

    public static void readUsersData() throws Exception {
        try (Scanner input = new Scanner(new File(userPath))) {
            while (input.hasNextLine()) {
                String[] tempList = input.nextLine().split(",");
                if (tempList.length != 6) {
                    throw new Exception("User object is corrupted. Can't read user data correctly.");
                }
                int id = Integer.parseInt(tempList[0]);
                if (id != -1) {
                    Users user = new Users(id, tempList[1], tempList[2], tempList[3], tempList[4], Integer.parseInt(tempList[5]));
                    UsersList.put(id, user);
                }
            }
        }
    }

    public static void readOwnedBooks() throws FileNotFoundException {
        try(Scanner input = new Scanner(new File(relationPath))){
        while(input.hasNext())  {
            String[] tempList = input.nextLine().split(",") ;
            LinkedList<Book> usersArr = new LinkedList<>( );
            for(int i = 1 ; i < tempList.length ;  i++){
                usersArr.add(BooksList.get(Integer.parseInt(tempList[i]))) ;
            }
            OwnedBooks.put(Integer.parseInt(tempList[0]) ,usersArr);
        }
    }}

    public static void writeOwnedBooks() throws IOException {
        try (FileWriter fileWriter = new FileWriter(relationPath, false);
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
        }
    }
}
