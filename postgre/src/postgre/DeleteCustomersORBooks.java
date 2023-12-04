package postgre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DeleteCustomersORBooks {
    private Scanner scanner;

    public DeleteCustomersORBooks() {
        this.scanner = new Scanner(System.in);
    }

    public void deleteRecord(String tableName) {
        App app = new App();

        System.out.println("Enter details for " + tableName + " to delete:");

        // Prompt user to enter details based on the chosen table
        String input = scanner.nextLine().trim();
        String[] parts = input.split(",");

        if (parts.length != getExpectedColumnCount(tableName)) {
            System.out.println("Invalid input format.");
            return;
        }

        try (Connection conn = app.connect()) {
            conn.setAutoCommit(false);

            // Delete record based on the chosen table
            if (tableName.equalsIgnoreCase("Customers")) {
                deleteCustomer(conn, parts);
            } else if (tableName.equalsIgnoreCase("Books")) {
                deleteBook(conn, parts);
            }

            conn.commit();
        } catch (SQLException | NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private int getExpectedColumnCount(String tableName) {
        // Define the expected number of columns based on the table
        if (tableName.equalsIgnoreCase("Customers")) {
            return 5; // customer_id, first_name, last_name, email, address
        } else if (tableName.equalsIgnoreCase("Books")) {
            return 6; // book_id, title, author_id, genre, price, stock_quantity
        }
        return 0;
    }

    private void deleteCustomer(Connection conn, String[] parts) throws SQLException {
        String deleteCustomerSQL = "DELETE FROM Customers WHERE customer_id = ?";
        String deleteOrderSQL = "DELETE FROM Orders WHERE customer_id = ?";

        int customerId = Integer.parseInt(parts[0].trim());

        try (PreparedStatement deleteCustomerStmt = conn.prepareStatement(deleteCustomerSQL);
             PreparedStatement deleteOrderStmt = conn.prepareStatement(deleteOrderSQL)) {

            deleteOrderStmt.setInt(1, customerId);
            deleteOrderStmt.executeUpdate();

            deleteCustomerStmt.setInt(1, customerId);
            int affectedRows = deleteCustomerStmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Customer deleted successfully.");
            } else {
                System.out.println("No customer found with the given details.");
            }
        }
    }

    private void deleteBook(Connection conn, String[] parts) throws SQLException {
        String deleteBookSQL = "DELETE FROM Books WHERE book_id = ?";
        String deleteOrderSQL = "DELETE FROM Orders WHERE book_id = ?";

        int bookId = Integer.parseInt(parts[0].trim());

        try (PreparedStatement deleteBookStmt = conn.prepareStatement(deleteBookSQL);
             PreparedStatement deleteOrderStmt = conn.prepareStatement(deleteOrderSQL)) {

            deleteOrderStmt.setInt(1, bookId);
            deleteOrderStmt.executeUpdate();

            deleteBookStmt.setInt(1, bookId);
            int affectedRows = deleteBookStmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Book deleted successfully.");
            } else {
                System.out.println("No book found with the given details.");
            }
        }
    }

    public void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }
}