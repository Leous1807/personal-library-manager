package main.java.library.service;

import main.java.library.model.Book;
import main.java.library.util.FileManager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class LibraryService {
    private List<Book> books;

    public LibraryService() {
        this.books = FileManager.loadBooksFromCSV();
        FileManager.saveBooksToCSV(books);
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public Optional<Book> findBookById(int id) {
        Optional<Book> ret = Optional.empty();

        for (Book book : books) {
            if (id == book.getId()) {
                ret = Optional.of(book);
                break;
            }
        }

        return ret;
    }

    public void saveBooks() {
        FileManager.saveBooksToCSV(books);
    }

    public boolean editBookMetadata(int bookId, String newTitle, String newAuthor, String newPublisher, Integer newYear) {
        Optional<Book> bookOptional = findBookById(bookId);
        if (bookOptional.isEmpty()) {
            return false;
        }
        Book book = bookOptional.get();

        if (newTitle != null && !newTitle.isEmpty()) {
            book.setTitle(newTitle);
        }
        if (newAuthor != null && !newAuthor.isEmpty()) {
            book.setAuthor(newAuthor);
        }
        if (newPublisher != null && !newPublisher.isEmpty()) {
            book.setPublisher(newPublisher);
        }
        if (newYear != null) {
            book.setPublicationYear(newYear);
        }

        saveBooks();
        return true;
    }

    public List<String> getBookPages(Book book, int linesPerPage) {
        return FileManager.readBookPages(book.getTextFilePath(), linesPerPage);
    }

    public String readFullText(int bookId) {
        Optional<Book> bookOptional = findBookById(bookId);
        if (bookOptional.isEmpty()) {
            return "";
        }
        Book book = bookOptional.get();

        return FileManager.readFullText(book.getTextFilePath());
    }

    public boolean editBookContent(int bookId, String newContent) throws IOException {
        Optional<Book> bookOptional = findBookById(bookId);
        if (bookOptional.isEmpty()) {
            return false;
        }
        Book book = bookOptional.get();

        FileManager.writeBookText(book.getTextFilePath(), newContent);
        return true;
    }

    public int countLines(Book book) {
        return FileManager.countLines(book.getTextFilePath());
    }
}