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

    }

    private void showBookMenu() {

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
