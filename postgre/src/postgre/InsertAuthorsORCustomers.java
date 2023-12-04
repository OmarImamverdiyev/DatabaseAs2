package postgre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class InsertAuthorsORCustomers {
    private Scanner scanner;

    public InsertAuthorsORCustomers() {
        this.scanner = new Scanner(System.in);
    }

    public void insertRecord(String table) {
        App app = new App();

        if (table.equalsIgnoreCase("Authors")) {
            insertIntoAuthors(app);
        } else if (table.equalsIgnoreCase("Customers")) {
            insertIntoCustomers(app);
        } else {
            System.out.println("Invalid table name.");
        }
    }

    private void insertIntoAuthors(App app) {
        try (Connection conn = app.connect()) {
            if (conn != null) {
                System.out.println("Insert record to Authors (author_id, author_name, nationality):");
                String input = scanner.nextLine().trim();

                String[] parts = input.split(",");

                if (parts.length != 3) {
                    System.out.println("Invalid input format.");
                    return;
                }

                int authorId = Integer.parseInt(parts[0].trim());
                String authorName = parts[1].trim();
                String nationality = parts[2].trim();

                String SQL = "INSERT INTO Authors (author_id, author_name, nationality) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
                    pstmt.setInt(1, authorId);
                    pstmt.setString(2, authorName);
                    pstmt.setString(3, nationality);

                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("New author inserted successfully.");
                    } else {
                        System.out.println("Failed to insert author.");
                    }
                }
            }
        } catch (SQLException | NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void insertIntoCustomers(App app) {
        try (Connection conn = app.connect()) {
            if (conn != null) {
                System.out.println("Insert record to Customers (customer_id, first_name, last_name, email, address):");
                String input = scanner.nextLine().trim();

                String[] parts = input.split(",");

                if (parts.length != 5) {
                    System.out.println("Invalid input format.");
                    return;
                }

                int customerId = Integer.parseInt(parts[0].trim());
                String firstName = parts[1].trim();
                String lastName = parts[2].trim();
                String email = parts[3].trim();
                String address = parts[4].trim();

                String SQL = "INSERT INTO Customers (customer_id, first_name, last_name, email, address) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
                    pstmt.setInt(1, customerId);
                    pstmt.setString(2, firstName);
                    pstmt.setString(3, lastName);
                    pstmt.setString(4, email);
                    pstmt.setString(5, address);

                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("New customer inserted successfully.");
                    } else {
                        System.out.println("Failed to insert customer.");
                    }
                }
            }
        } catch (SQLException | NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
