package postgre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DeleteCustomers {
    private Scanner scanner;

    public DeleteCustomers() {
        this.scanner = new Scanner(System.in);
    }

    public void deleteCustomer() {
        System.out.println("Choose the deletion method:");
        System.out.println("1. Find and Delete by Primary Key (customer_id)");
        System.out.println("2. Find and Delete by Full Record Information");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                deleteCustomerByPrimaryKey();
                break;
            case 2:
                deleteCustomerByRecordInfo();
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    private void deleteCustomerByPrimaryKey() {
        System.out.println("Enter the customer_id to delete:");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        deleteCustomerWithId(customerId);
    }

    private void deleteCustomerByRecordInfo() {
        System.out.println("Enter customer details (customer_id, first_name, last_name, email, address) to delete:");
        String input = scanner.nextLine().trim();

        // Split the input by commas
        String[] parts = input.split(",");

        if (parts.length != 5) {
            System.out.println("Invalid input format.");
            return;
        }

        // Extract values and remove parentheses
        int customerId = Integer.parseInt(parts[0].replaceAll("[()]", "").trim());

        deleteCustomerWithId(customerId);
    }

    private void deleteCustomerWithId(int customerId) {
        App app = new App();

        String deleteOrdersSQL = "DELETE FROM Orders WHERE customer_id = ?";
        String deleteCustomerSQL = "DELETE FROM Customers WHERE customer_id = ?";

        try (Connection conn = app.connect();
             PreparedStatement deleteOrdersStmt = conn.prepareStatement(deleteOrdersSQL);
             PreparedStatement deleteCustomerStmt = conn.prepareStatement(deleteCustomerSQL)) {

            conn.setAutoCommit(false);

            // Delete tuples in Orders table with the specified customer_id
            deleteOrdersStmt.setInt(1, customerId);
            int affectedRows = deleteOrdersStmt.executeUpdate();

            // Now, delete the customer
            deleteCustomerStmt.setInt(1, customerId);
            affectedRows = deleteCustomerStmt.executeUpdate();

            if (affectedRows > 0) {
                conn.commit();
                System.out.println("Customer deleted successfully.");
            } else {
                conn.rollback();
                System.out.println("No customer found with the given customer_id.");
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