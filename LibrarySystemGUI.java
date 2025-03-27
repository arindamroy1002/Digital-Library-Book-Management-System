import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

class Book {
    private String bookID, title, author, genre, availability;

    public Book(String bookID, String title, String author, String genre, String availability) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.genre = genre;
        setAvailability(availability);
    }

    public String getBookID() {
        return bookID;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getAvailability() {
        return availability;
    }

    public void setTitle(String title) {
        if (!title.isEmpty())
            this.title = title;
    }

    public void setAuthor(String author) {
        if (!author.isEmpty())
            this.author = author;
    }

    public void setAvailability(String availability) {
        if (availability.equalsIgnoreCase("Available") || availability.equalsIgnoreCase("Checked Out")) {
            this.availability = availability;
        } else {
            this.availability = "Available";
        }
    }
}

public class LibrarySystemGUI {
    private static Map<String, Book> bookCatalog = new HashMap<>();
    private static DefaultTableModel tableModel;
    private static JTable bookTable;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibrarySystemGUI::createGUI);
    }

    private static void createGUI() {
        JFrame frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout());

        // Table
        String[] columns = { "Book ID", "Title", "Author", "Genre", "Availability" };
        tableModel = new DefaultTableModel(columns, 0);
        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel panel = new JPanel();
        String[] buttons = { "Add Book", "View Books", "Search Book", "Update Book", "Delete Book", "Exit" };
        for (String btn : buttons) {
            JButton button = new JButton(btn);
            button.addActionListener(e -> handleButtonClick(btn));
            panel.add(button);
        }
        frame.add(panel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static void handleButtonClick(String action) {
        switch (action) {
            case "Add Book":
                addBook();
                break;
            case "View Books":
                viewAllBooks();
                break;
            case "Search Book":
                searchBook();
                break;
            case "Update Book":
                updateBook();
                break;
            case "Delete Book":
                deleteBook();
                break;
            case "Exit":
                System.exit(0);
                break;
        }
    }

    private static void addBook() {
        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField genreField = new JTextField();
        String[] statusOptions = { "Available", "Checked Out" };
        JComboBox<String> availabilityBox = new JComboBox<>(statusOptions);

        Object[] message = {
                "Book ID:", idField,
                "Title:", titleField,
                "Author:", authorField,
                "Genre:", genreField,
                "Availability:", availabilityBox
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String bookID = idField.getText().trim();
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String genre = genreField.getText().trim();
            String availability = (String) availabilityBox.getSelectedItem();

            if (bookID.isEmpty() || title.isEmpty() || author.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Book ID, Title, and Author cannot be empty!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (bookCatalog.containsKey(bookID)) {
                JOptionPane.showMessageDialog(null, "Book ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Book book = new Book(bookID, title, author, genre, availability);
            bookCatalog.put(bookID, book);
            JOptionPane.showMessageDialog(null, "Book added successfully!");
            viewAllBooks();
        }
    }

    private static void viewAllBooks() {
        tableModel.setRowCount(0);
        for (Book book : bookCatalog.values()) {
            tableModel.addRow(new Object[] { book.getBookID(), book.getTitle(), book.getAuthor(), book.getGenre(),
                    book.getAvailability() });
        }
    }

    private static void searchBook() {
        String searchKey = JOptionPane.showInputDialog("Enter Book ID or Title:");
        if (searchKey == null || searchKey.trim().isEmpty())
            return;

        for (Book book : bookCatalog.values()) {
            if (book.getBookID().equalsIgnoreCase(searchKey) || book.getTitle().equalsIgnoreCase(searchKey)) {
                JOptionPane.showMessageDialog(null, "Book Found:\n" +
                        "Book ID: " + book.getBookID() + "\n" +
                        "Title: " + book.getTitle() + "\n" +
                        "Author: " + book.getAuthor() + "\n" +
                        "Genre: " + book.getGenre() + "\n" +
                        "Availability: " + book.getAvailability());
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Book not found.");
    }

    private static void updateBook() {
        String bookID = JOptionPane.showInputDialog("Enter Book ID to Update:");
        if (bookID == null || !bookCatalog.containsKey(bookID)) {
            JOptionPane.showMessageDialog(null, "Book ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Book book = bookCatalog.get(bookID);
        JTextField titleField = new JTextField(book.getTitle());
        JTextField authorField = new JTextField(book.getAuthor());
        JTextField genreField = new JTextField(book.getGenre());
        String[] statusOptions = { "Available", "Checked Out" };
        JComboBox<String> availabilityBox = new JComboBox<>(statusOptions);
        availabilityBox.setSelectedItem(book.getAvailability());

        Object[] message = {
                "Title:", titleField,
                "Author:", authorField,
                "Genre:", genreField,
                "Availability:", availabilityBox
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Update Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            book.setTitle(titleField.getText().trim());
            book.setAuthor(authorField.getText().trim());
            book.setAvailability((String) availabilityBox.getSelectedItem());
            JOptionPane.showMessageDialog(null, "Book updated successfully!");
            viewAllBooks();
        }
    }

    private static void deleteBook() {
        String bookID = JOptionPane.showInputDialog("Enter Book ID to Delete:");
        if (bookID == null || !bookCatalog.containsKey(bookID)) {
            JOptionPane.showMessageDialog(null, "Book ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        bookCatalog.remove(bookID);
        JOptionPane.showMessageDialog(null, "Book deleted successfully!");
        viewAllBooks();
    }
}
