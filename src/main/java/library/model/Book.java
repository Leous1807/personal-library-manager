package main.java.library.model;

public class Book {
    private int id;
    private  String title;
    private String textFilePath;
    private String author;
    private String publisher;
    private int publicationYear;

    public Book(int id, String title, String textFilePath, String author, String publisher, int publicationYear) {
        this.id = id;
        this.title = title;
        this.textFilePath = textFilePath;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String  getPublisher() { return publisher; }
    public String getTextFilePath() { return textFilePath; }
    public int getPublicationYear() { return publicationYear; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public void setTextFilePath(String textFilePath) { this.textFilePath = textFilePath; }

    @Override
    public String toString() {
        return id + " " + title + " | " + author + " | " + publisher + " | " + publicationYear;
    }
}
