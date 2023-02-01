package data_pro.DataBase;

import java.io.*;
import java.util.*;

public class RWDatabase {
    //the number of books
    public static int BooksNumber ;
    //A data structure for store the   books objects
    public static TreeMap<Integer , Book> BooksList = new TreeMap<>() ;
    //A data structure for store the users objects
    public static TreeMap<Integer , Users> UsersList = new TreeMap<>() ;
    //A One-To-many relation datastructures between user and his books
    public static HashMap<Integer , LinkedList<Book>> OwnedBooks = new HashMap<>();
    //Books objects Data content file
    private static final String filePath = "src/data_pro/DataBase/BooksDB.txt" ;
    //users Data file
    private static final String usersFilePath = "src/data_pro/DataBase/UsersDB.csv" ;
    //owned books data file
    private static final String ownedBooksPath = "src/data_pro/DataBase/UsersBooksRelationDB.csv" ;

    public static void main(String[] args) throws Exception {
        reader();
        doWrite();
        readUsersData() ;
        readOwnedBooks();
        writeOwnedBooks();
        System.out.print(OwnedBooks.values());


    }
    public static void writeUsersData( ) throws IOException {
        FileWriter fileWriter = new FileWriter(usersFilePath, false );
        BufferedWriter out = new BufferedWriter(fileWriter);
        UsersList.forEach(
                (key, value)
                        -> {
                    try {
                        if (value.getId() != -1 )
                            out.write( value.getId() + "," + value.getGmail() + ","+ value.getPassword() + ","+ value.getFirst() + ","+ value.getLast() + ","+ value.getAge() + "\n"  );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        out.close();
    }
    public static void doWrite() throws IOException {
        if(BooksList == null)
            throw new NullPointerException("THERE IS NO DATA IN THE TREEMAP TO WRITE ! ");
        new FileWriter(filePath, false).close();
        FileWriter fileWriter = new FileWriter(filePath, true);
        BufferedWriter out = new BufferedWriter(fileWriter);
        out.write(BooksNumber + "\n");
        out.close();

        //here we have the name of the book that we want to store information into
        for (Book e: BooksList.values() ){
            writer(e , filePath);
        }
    }

    public static void reader( ) throws FileNotFoundException{
        Scanner input = new Scanner(new File(filePath) );
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
        Scanner input = new Scanner(new File(usersFilePath));
        while(input.hasNextLine()){
           String[] tempList =  input.nextLine().split(",") ;
           if (tempList.length != 6)
               throw new Exception("User object is  Corrupted. Can't read user Data coreectly ")     ;
            Users User = new Users(Integer.parseInt(tempList[0]), tempList[1],tempList[2],tempList[3],tempList[4],Integer.parseInt(tempList[5]));
            if (User.getId() != -1  )
                 UsersList.put(User.getId() , User) ;

        }
    }
    public static void readOwnedBooks() throws FileNotFoundException {
        Scanner input = new Scanner(new File(ownedBooksPath));
        while(input.hasNext())  {
            String[] tempList = input.nextLine().split(",") ;
            LinkedList<Book> usersArr = new LinkedList<>( );
            for(int i = 1 ; i < tempList.length ;  i++){
                usersArr.add(BooksList.get(Integer.parseInt(tempList[i]))) ;
            }
            OwnedBooks.put(Integer.parseInt(tempList[0]) ,usersArr);
        }
    }
    public static void writeOwnedBooks() throws IOException {
        FileWriter fileWriter = new FileWriter(ownedBooksPath, false );
        BufferedWriter out = new BufferedWriter(fileWriter);
        OwnedBooks.forEach(
                (key, value)
                        -> {
                    try {
                            out.write( key +",") ;
                            for(Book e : value){

                                out.write( e.getId() + ","  );
                            }
                            out.write("\n");

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        out.close();

    }
}
