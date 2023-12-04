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
            System.out.println("Enter the new value for " + attribute + ":");
            String newValue = scanner.nextLine().trim();
            attributeValues.put(attribute, newValue);
        }

        updateBook(app, bookId, attributeValues);

        // Close the scanner when done
       // scanner.close();
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
                String value = attributeValues.get(attribute);
                // Check the type of the attribute and set it accordingly
                switch (attribute) {
                    case "author_id":
                    case "stock_quantity":
                        pstmt.setInt(parameterIndex++, Integer.parseInt(value));
                        break;
                    case "price":
                        pstmt.setDouble(parameterIndex++, Double.parseDouble(value));
                        break;
                    // Handle other cases for different data types if need
                    default:
                        pstmt.setString(parameterIndex++, value);
                        break;
                }
            }
            
         // Check if author_id exists in the Authors table
            if (attributeValues.containsKey("author_id")) {
                int authorId = Integer.parseInt(attributeValues.get("author_id"));
                if (!isAuthorIdPresent(conn, authorId)) {
                    System.out.println("Cannot Update! Your Author_id is not present in Authors table");
                    return;
                }
            }
            
            pstmt.setInt(parameterIndex, bookId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Book details updated successful.");
            } else {
                System.out.println("No book found with the given book_id or no update performed.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private boolean isAuthorIdPresent(Connection conn, int authorId) throws SQLException {
        String query = "SELECT COUNT(*) FROM authors WHERE author_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, authorId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        }
        return false;
    }

}
