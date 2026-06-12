package main.java.library.ui;

import main.java.library.model.Book;
import main.java.library.service.LibraryService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    LibraryService service;
    JPanel booksPanel, menuPanel, readerPanel;
    Book selectedBook;
    JTextField titleField, authorField, publisherField, yearField;
    JTextArea pageArea;
    List<String> pages;
    int currentPage;

    public MainFrame() {
        service = new LibraryService();
        setTitle("Personal Library");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createBooksPanel();
        setVisible(true);
    }

    private void createBooksPanel() {
        booksPanel = new JPanel();
        booksPanel.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridLayout(0, 1, 0, 15));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 50));

        for (Book book : service.getAllBooks()) {
            JButton button = new JButton(book.getTitle());
            button.addActionListener(e -> {
                selectedBook = book;
                showBookMenu();
            });
            centerPanel.add(button);
        }

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> createBooksPanel());

        booksPanel.add(new JScrollPane(centerPanel), BorderLayout.CENTER);
        booksPanel.add(refreshButton, BorderLayout.SOUTH);

        setContentPane(booksPanel);
        revalidate();
        repaint();
    }

    private void showBookMenu() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 50));

        titleField = new JTextField(selectedBook.getTitle());
        authorField = new JTextField(selectedBook.getAuthor());
        publisherField = new JTextField(selectedBook.getPublisher());
        yearField = new JTextField(String.valueOf(selectedBook.getPublicationYear()));

        JLabel lineCnt = new JLabel("Lines: " + service.countLines(selectedBook));

        JButton saveMetadata = new JButton("Save Metadata");
        saveMetadata.addActionListener(e -> {
            if (titleField.getText().trim().isEmpty()
                    || authorField.getText().trim().isEmpty()
                    || publisherField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this, "fields can't be empty!");
                return;
            }
            try {
                boolean edited = service.editBookMetadata(
                        selectedBook.getId(),
                        titleField.getText(),
                        authorField.getText(),
                        publisherField.getText(),
                        Integer.parseInt(yearField.getText())
                );
                JOptionPane.showMessageDialog(
                        this,
                        edited ? "Saved successfully!" : "Error!"
                );
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "year should be a number!!!!");
            }
        });

        JButton readButton = new JButton("Read Book");
        readButton.addActionListener(e -> openBook(false));

        JButton editButton = new JButton("Edit Book");
        editButton.addActionListener(e -> openBook(true));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> createBooksPanel());

        menuPanel.add(new JLabel("Title"));
        menuPanel.add(titleField);
        menuPanel.add(new JLabel("Author"));
        menuPanel.add(authorField);
        menuPanel.add(new JLabel("Publisher"));
        menuPanel.add(publisherField);
        menuPanel.add(new JLabel("Year"));
        menuPanel.add(yearField);

        JPanel centerPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        centerPanel.add(lineCnt);
        centerPanel.add(saveMetadata);
        centerPanel.add(readButton);
        centerPanel.add(editButton);
        centerPanel.add(backButton);

        menuPanel.add(centerPanel);
        setContentPane(menuPanel);
        revalidate();
        repaint();
    }

    private void openBook(boolean canEdit) {
        readerPanel = new JPanel();
        readerPanel.setLayout(new BorderLayout());

        pages = service.getBookPages(selectedBook, 20);
        currentPage = 0;

        pageArea = new JTextArea();
        pageArea.setEditable(canEdit);
        pageArea.setLineWrap(true);
        pageArea.setWrapStyleWord(true);

        if (!pages.isEmpty()) {
            pageArea.setText(pages.get(currentPage));
        }

        JScrollPane scrollPane = new JScrollPane(pageArea);
        JButton previousBTN = new JButton("<");
        JButton nextBTN = new JButton(">");
        JButton backBTN = new JButton("Back");
        JPanel bottomPanel = new JPanel();

        previousBTN.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                pageArea.setText(pages.get(currentPage));
            }
        });

        nextBTN.addActionListener(e -> {
            if (currentPage < pages.size() - 1) {
                currentPage++;
                pageArea.setText(pages.get(currentPage));
            }
        });

        backBTN.addActionListener(e -> showBookMenu());

        bottomPanel.add(previousBTN);
        bottomPanel.add(backBTN);
        bottomPanel.add(nextBTN);

        if (canEdit) {
            JButton applyBTN = new JButton("Apply");
            applyBTN.addActionListener(e -> {
                try {
                    service.editBookContent(selectedBook.getId(), pageArea.getText());
                    JOptionPane.showMessageDialog(this, "Saved successfully!");
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(this, ee.getMessage());
                }
            });
            bottomPanel.add(applyBTN);
        }

        readerPanel.add(scrollPane, BorderLayout.CENTER);
        readerPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(readerPanel);
        revalidate();
        repaint();
    }
}
