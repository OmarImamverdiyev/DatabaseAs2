package postgre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class InsertBook {
    private Scanner scanner;

    public InsertBook() {
        this.scanner = new Scanner(System.in);
    }

    public void insertNewBook() {
        App app = new App();

        try (Connection conn = app.connect()) {
            if (conn != null) {
                System.out.println("Insert record to Book table (book_id, title, author_id, genre, price, stock_quantity):");
                String input = scanner.nextLine().trim();

                String[] parts = input.split(",");

                if (parts.length != 6) {
                    System.out.println("Invalid input format.");
                    return;
                }

                int bookId = Integer.parseInt(parts[0].trim());
                String title = parts[1].trim();
                int authorId = Integer.parseInt(parts[2].trim());
                String genre = parts[3].trim();
                double price = Double.parseDouble(parts[4].trim());
                int stockQuantity = Integer.parseInt(parts[5].trim());

                String checkAuthorSQL = "SELECT * FROM Authors WHERE author_id = ?";
                PreparedStatement checkAuthorStmt = conn.prepareStatement(checkAuthorSQL);
                checkAuthorStmt.setInt(1, authorId);
                ResultSet authorResult = checkAuthorStmt.executeQuery();

                if (!authorResult.next()) {
                    System.out.println("Author ID does not exist in Authors table.");
                    System.out.println("Do you want to change the authors table? (1. Yes, 2. No)");
                    int option = Integer.parseInt(scanner.nextLine().trim());

                    if (option == 1) {
                        System.out.println("Enter author_name:");
                        String authorName = scanner.nextLine().trim();

                        System.out.println("Enter nationality:");
                        String nationality = scanner.nextLine().trim();

                        String insertAuthorSQL = "INSERT INTO Authors (author_id, author_name, nationality) VALUES (?, ?, ?)";
                        PreparedStatement insertAuthorStmt = conn.prepareStatement(insertAuthorSQL);
                        insertAuthorStmt.setInt(1, authorId);
                        insertAuthorStmt.setString(2, authorName);
                        insertAuthorStmt.setString(3, nationality);
                        insertAuthorStmt.executeUpdate();
                  }
                }

                String SQL = "INSERT INTO Books (book_id, title, author_id, genre, price, stock_quantity) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(SQL);

                pstmt.setInt(1, bookId);
                pstmt.setString(2, title);
                pstmt.setInt(3, authorId);
                pstmt.setString(4, genre);
                pstmt.setDouble(5, price);
                pstmt.setInt(6, stockQuantity);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("New record inserted successfully in Books table.");
                } else {
                    System.out.println("Failed to insert record in Books table.");
                }
            }
        } catch (SQLException | NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
