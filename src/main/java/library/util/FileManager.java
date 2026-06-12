package main.java.library.util;

import main.java.library.model.Book;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    public static final String BOOKS_TEXT_DIR = "data/books_text/";
    public static final String BOOK_LIST_CSV = "data/books_text/Book_List.txt";

    public static List<Book> loadBooksFromCSV() {
        List<Book> books_list = new ArrayList<>();
        File f = new File(BOOK_LIST_CSV);
        if (!f.exists()) {
            System.out.println("The book list was not found. Please try again.");
        } else {
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                boolean flag = true;
                int id = 1;
                while ((line = br.readLine()) != null) {
                    if (flag) {
                        flag = false;
                        continue;
                    }
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    String[] bk = line.split(",");
                    if (bk.length >= 5) {
                        Book book = new Book(id++, bk[0].trim(), bk[4].trim(), bk[1].trim(), bk[2].trim(), Integer.parseInt(bk[3].trim()));
                        books_list.add(book);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading CSV file: " + e.getMessage());
            }
        }
        return books_list;
    }

    public static void saveBooksToCSV(List<Book> books) {
        File f = new File(BOOK_LIST_CSV);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            bw.write("title,author,publisher,year,file");
            bw.newLine();
            for (Book b : books) {
                String l = String.format("%s,%s,%s,%d,%s",
                        b.getTitle(),
                        b.getAuthor(),
                        b.getPublisher(),
                        b.getPublicationYear(),
                        b.getTextFilePath()
                );
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving CSV file: " + e.getMessage());
        }
    }

    public static List<String> readBookPages(String filePath, int linesPerPage) {
        List<String> pages = new ArrayList<>();
        File f = new File(BOOKS_TEXT_DIR + filePath);
        if (!f.exists()) {
            System.out.println("This book was not found.");
            return pages;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            StringBuilder pg = new StringBuilder();
            int linecnt = 0;
            while ((line = br.readLine()) != null) {
                pg.append(line).append("\n");
                linecnt++;
                if (linecnt == linesPerPage) {
                    pages.add(pg.toString());
                    pg.setLength(0);
                    linecnt = 0;
                }
            }
            if (pg.length() > 0) {
                pages.add(pg.toString());
            }
        } catch (IOException e) {
            pages.clear();
            System.out.println("Error reading book pages: " + e.getMessage());
        }
        return pages;
    }

    public static int countLines(String filePath) {
        File f = new File(BOOKS_TEXT_DIR + filePath);
        if (!f.exists()) {
            System.out.println("This book was not found.");
            return 0;
        }
        int cnt = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            while (br.readLine() != null) {
                cnt++;
            }
        } catch (IOException e) {
            System.out.println("Error counting lines: " + e.getMessage());
            return 0;
        }
        return cnt;
    }

    public static void writeBookText(String filePath, String content) throws IOException {
        File f = new File(BOOKS_TEXT_DIR + filePath);
        File parentDir = f.getParentFile();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f, false))) {
            bw.write(content);
        }
    }

    public static String readFullText(String filePath) {
        File f = new File(BOOKS_TEXT_DIR + filePath);
        if (!f.exists()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String l;
            while ((l = br.readLine()) != null) {
                sb.append(l).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Error reading full text: " + e.getMessage());
            return "";
        }
        return sb.toString();
    }

}