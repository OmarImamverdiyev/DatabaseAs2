package postgre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DeleteBooks {
    private Scanner scanner;

    public DeleteBooks() {
        this.scanner = new Scanner(System.in);
    }

    public void deleteBook() {
        System.out.println("Choose the deletion method:");
        System.out.println("1. Find and Delete by Primary Key (book_id)");
        System.out.println("2. Find and Delete by Full Record Information");
        System.out.println("3. Find and Delete by Title");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                deleteBookByPrimaryKey();
                break;
            case 2:
                deleteBookByRecordInfo();
                break;
            case 3:
                deleteBookByTitle();
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    private void deleteBookByPrimaryKey() {
        System.out.println("Enter the book_id to delete:");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        deleteBookWithId(bookId);
    }

    private void deleteBookByRecordInfo() {
        System.out.println("Enter book details (book_id, title, author_id, genre, price, stock_quantity) to delete:");
        String input = scanner.nextLine().trim();

        // Split the input by commas
        String[] parts = input.split(",");

        if (parts.length != 6) {
            System.out.println("Invalid input format.");
            return;
        }

        // Extract values and remove parentheses
        int bookId = Integer.parseInt(parts[0].replaceAll("[()]", "").trim());

        deleteBookWithId(bookId);
    }

    private void deleteBookByTitle() {
        System.out.println("Enter the book title to delete:");
        String title = scanner.nextLine().trim();

        deleteBookWithTitle(title);
    }

    private void deleteBookWithTitle(String title) {
        App app = new App();

        String deleteOrdersSQL = "DELETE FROM Orders WHERE book_id = (SELECT book_id FROM Books WHERE title = ?)";
        String deleteBookSQL = "DELETE FROM Books WHERE title = ?";

        try (Connection conn = app.connect();
             PreparedStatement deleteOrdersStmt = conn.prepareStatement(deleteOrdersSQL);
             PreparedStatement deleteBookStmt = conn.prepareStatement(deleteBookSQL)) {

            conn.setAutoCommit(false);

            // Delete tuples in Orders table with the specified title
            deleteOrdersStmt.setString(1, title);
            int affectedRows = deleteOrdersStmt.executeUpdate();

            // Now, delete the book
            deleteBookStmt.setString(1, title);
            affectedRows = deleteBookStmt.executeUpdate();

            if (affectedRows > 0) {
                conn.commit();
                System.out.println("Book(s) deleted successfully.");
            } else {
                conn.rollback();
                System.out.println("No book(s) found with the given title.");
            }
        } catch (SQLException | NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }
    }
    private void deleteBookWithId(int bookId) {
        App app = new App();

        String deleteOrdersSQL = "DELETE FROM Orders WHERE book_id = ?";
        String deleteBookSQL = "DELETE FROM Books WHERE book_id = ?";

        try (Connection conn = app.connect();
             PreparedStatement deleteOrdersStmt = conn.prepareStatement(deleteOrdersSQL);
             PreparedStatement deleteBookStmt = conn.prepareStatement(deleteBookSQL)) {

            conn.setAutoCommit(false);

            // Delete tuples in Orders table with the specified book_id
            deleteOrdersStmt.setInt(1, bookId);
            int affectedRows = deleteOrdersStmt.executeUpdate();

            // Now, delete the book
            deleteBookStmt.setInt(1, bookId);
            affectedRows = deleteBookStmt.executeUpdate();

            if (affectedRows > 0) {
                conn.commit();
                System.out.println("Book deleted successfully.");
            } else {
                conn.rollback();
                System.out.println("No book found with the given book_id.");
            }
        } catch (SQLException | NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }
}