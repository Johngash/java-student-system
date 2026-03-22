package com.university.system.model;

/**
 * Represents a book in the library system.
 */
public class Book {
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String edition;
    private String version;
    private int yearPublished;
    private int totalCopies;
    private int availableCopies;

    public Book() {}

    public Book(String isbn, String title, String author, String publisher, String edition, 
                String version, int yearPublished, int totalCopies, int availableCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.edition = edition;
        this.version = version;
        this.yearPublished = yearPublished;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    // Getters and Setters
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getEdition() { return edition; }
    public void setEdition(String edition) { this.edition = edition; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public int getYearPublished() { return yearPublished; }
    public void setYearPublished(int yearPublished) { this.yearPublished = yearPublished; }

    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }

    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    public boolean isAvailable() {
        return availableCopies > 0;
    }

    @Override
    public String toString() {
        return "[" + isbn + "] " + title + " by " + author + 
               " | Available: " + availableCopies + "/" + totalCopies;
    }
}
