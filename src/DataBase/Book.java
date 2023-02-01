
package data_pro.DataBase;

public class Book {
    public int id;
    private String title ;
    private String author;
    public int quantity;
    private String isbn;
    private String publisher = null ;
    private int total_pages = 0 ;
    private float rating = 0 ;
    private String published_date = null;
    
    
    public Book(int id , String title , String auther, String  isbn , String publisher , int total , float rating  , String date, int quan ){
        this.id = id ;
        this.title = title ; 
        this.author = auther ;
        this.isbn = isbn ; 
        this.publisher = publisher ; 
        this.total_pages = total ; 
        this.rating = rating ; 
        this.published_date = date ;
        this.quantity = quan ;
    }

    Book() {
        
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getTotalPages() {
        return total_pages;
    }

    public float getRating() {
        return rating;
    }

    public String getDate() {
        return published_date;
    }

    public void setId(int book_id) {
        this.id = book_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    public void dicreseQuantity(){
        this.quantity-- ;
    }
    @Override
    public String toString(){
        return  this.id +"\n"+ this.title  + "\n" + this.quantity + "\n" ;
    }  
}

    