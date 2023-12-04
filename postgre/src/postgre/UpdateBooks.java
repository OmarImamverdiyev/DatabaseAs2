package postgre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UpdateBooks {
    private Scanner scanner;

    public UpdateBooks() {
        this.scanner = new Scanner(System.in);
    }

    public void updateBookDetails() {
        App app = new App();

        GetBooks getBooks = new GetBooks();
        getBooks.getBooks();

        System.out.println("Choose the book_id you want to update:");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Which attribute(s) of this book do you want to change? (title, author_id, genre, price, stock_quantity)");
        String attributeInput = scanner.nextLine().toLowerCase().trim();
        String[] attributes = attributeInput.split(", ");

        Map<String, String> attributeValues = new HashMap<>();

        for (String attribute : attributes) {
            if (attribute.equals("author_id")) {
                System.out.println("Enter the new author_id:");
                int newAuthorId = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (!checkAuthorExists(app, newAuthorId)) {
                    // If the author_id doesn't exist, require details to add it to Authors table
                    System.out.println("The author_id doesn't exist in the Authors table.");
                    System.out.println("Please provide details to add this author:");
                    System.out.print("Author Name: ");
                    String authorName = scanner.nextLine().trim();
                    System.out.print("Nationality: ");
                    String nationality = scanner.nextLine().trim();

                    addAuthor(app, newAuthorId, authorName, nationality);
                }

                updateBookAuthorId(app, bookId, newAuthorId);
            } else {
                System.out.println("Enter the new value for " + attribute + ":");
                String newValue = scanner.nextLine().trim();
                attributeValues.put(attribute, newValue);
            }
        }

        updateBook(app, bookId, attributeValues);

        // Close the scanner when done
        // scanner.close();
    }

    private boolean checkAuthorExists(App app, int authorId) {
        String SQL = "SELECT author_id FROM Authors WHERE author_id = ?";

        try (Connection conn = app.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, authorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // If there's a result, author exists
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    private void addAuthor(App app, int authorId, String authorName, String nationality) {
        String SQL = "INSERT INTO Authors (author_id, author_name, nationality) VALUES (?, ?, ?)";

        try (Connection conn = app.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, authorId);
            pstmt.setString(2, authorName);
            pstmt.setString(3, nationality);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("New author added to Authors table.");
            } else {
                System.out.println("Failed to add the author.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void updateBook(App app, int bookId, Map<String, String> attributeValues) {
        // Constructing the SQL query based on the chosen attribute
        String SQL = "UPDATE books SET ";

        for (String attribute : attributeValues.keySet()) {
            SQL += attribute + " = ?, ";
        }

        SQL = SQL.substring(0, SQL.length() - 2); // Remove the last comma and space
        SQL += " WHERE book_id = ?";

        try (Connection conn = app.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            int parameterIndex = 1;
            for (String attribute : attributeValues.keySet()) {
                pstmt.setString(parameterIndex++, attributeValues.get(attribute));
            }
            pstmt.setInt(parameterIndex, bookId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Book details updated successfully.");
            } else {
                System.out.println("No book found with the given book_id or no update performed.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void updateBookAuthorId(App app, int bookId, int newAuthorId) {
        String SQL = "UPDATE books SET author_id = ? WHERE book_id = ?";

        try (Connection conn = app.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, newAuthorId);
            pstmt.setInt(2, bookId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Book author_id updated successfully.");
            } else {
                System.out.println("No book found with the given book_id or no update performed.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
}
}
