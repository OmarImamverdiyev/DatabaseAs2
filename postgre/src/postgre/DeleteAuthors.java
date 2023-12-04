package postgre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DeleteAuthors {
    private Scanner scanner;

    public DeleteAuthors() {
        this.scanner = new Scanner(System.in);
    }

    public void deleteAuthor() {
        System.out.println("Choose the deletion method:");
        System.out.println("1. Find and Delete by Primary Key (author_id)");
        System.out.println("2. Find and Delete by Full Record Information");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                deleteAuthorByPrimaryKey();
                break;
            case 2:
                deleteAuthorByRecordInfo();
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    private void deleteAuthorByPrimaryKey() {
        System.out.println("Enter the author_id to delete:");
        int authorId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        deleteAuthorWithId(authorId);
    }

    private void deleteAuthorByRecordInfo() {
        System.out.println("Enter author details (author_id, author_name, nationality) to delete:");
        String input = scanner.nextLine().trim();

        // Split the input by commas
        String[] parts = input.split(",");

        if (parts.length != 3) {
            System.out.println("Invalid input format.");
            return;
        }

        // Extract values and remove parentheses
        int authorId = Integer.parseInt(parts[0].replaceAll("[()]", "").trim());

        deleteAuthorWithId(authorId);
    }

    private void deleteAuthorWithId(int authorId) {
        App app = new App();

        String deleteBooksSQL = "DELETE FROM Books WHERE author_id = ?";
        String deleteAuthorSQL = "DELETE FROM Authors WHERE author_id = ?";

        try (Connection conn = app.connect();
             PreparedStatement deleteBooksStmt = conn.prepareStatement(deleteBooksSQL);
             PreparedStatement deleteAuthorStmt = conn.prepareStatement(deleteAuthorSQL)) {

            conn.setAutoCommit(false);

            // Delete tuples in Books table with the specified author_id
            deleteBooksStmt.setInt(1, authorId);
            int affectedRows = deleteBooksStmt.executeUpdate();

            // Now, delete the author
            deleteAuthorStmt.setInt(1, authorId);
            affectedRows = deleteAuthorStmt.executeUpdate();

            if (affectedRows > 0) {
                conn.commit();
                System.out.println("Author deleted successfully.");
            } else {
                conn.rollback();
                System.out.println("No author found with the given author_id.");
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
