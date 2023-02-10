package com.librarysytsem.database;

import java.io.Serializable;

public class Book implements Serializable {
    private int id;
    private String title;
    private String author;
    private int quantity;
    private String isbn;
    private String publisher;
    private int totalPages;
    private float rating;
    private String publishedDate;

    public Book(int id, String title, String author, String isbn, String publisher, int totalPages, float rating, String publishedDate, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.totalPages = totalPages;
        this.rating = rating;
        this.publishedDate = publishedDate;
        this.quantity = quantity;
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
        return totalPages;
    }

    public float getRating() {
        return rating;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void decreaseQuantity() {
        this.quantity--;
    }

    @Override
    public String toString() {
        return this.id + "\n" + this.title + "\n" + this.quantity + "\n";
    }
}
