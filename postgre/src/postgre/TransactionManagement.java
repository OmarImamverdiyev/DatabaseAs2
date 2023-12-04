package postgre;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TransactionManagement {
    private Scanner scanner;

    public TransactionManagement() {
        this.scanner = new Scanner(System.in);
    }

    public void placeOrder() {
        App app = new App();
        Connection conn = null;
        PreparedStatement orderStmt = null;
        PreparedStatement updateBooksStmt = null;

        try {
            conn = app.connect();
            conn.setAutoCommit(false); // Start transaction

            System.out.println("Enter customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine().trim());
            
         // Check if the customer exists in the Customers table
            if (!customerExists(conn, customerId)) {
                // If the customer doesn't exist, prompt for new customer details
                System.out.println("Customer does not exist. Please provide details to create a new customer.");

                System.out.println("Enter first name:");
                String firstName = scanner.nextLine();

                System.out.println("Enter last name:");
                String lastName = scanner.nextLine();

                System.out.println("Enter address:");
                String address = scanner.nextLine();

                System.out.println("Enter email:");
                String email = scanner.nextLine();

                // Insert new customer details into the Customers table
                insertNewCustomer(conn, customerId, firstName, lastName, email, address);
            }

            System.out.println("Enter book ID: ");
            int bookId = Integer.parseInt(scanner.nextLine().trim());

            System.out.println("Enter order date (YYYY-MM-DD): ");
            String orderDateString = scanner.nextLine().trim();
            Date orderDate = Date.valueOf(orderDateString);

            System.out.println("Enter total amount: ");
            int totalAmount = Integer.parseInt(scanner.nextLine().trim());
            
         // Check if the book exists in the Books table
            String checkBookExistenceSQL = "SELECT COUNT(*) AS count FROM Books WHERE book_id = ?";
            PreparedStatement checkBookExistenceStmt = conn.prepareStatement(checkBookExistenceSQL);
            checkBookExistenceStmt.setInt(1, bookId);
            ResultSet bookExistenceResult = checkBookExistenceStmt.executeQuery();
            bookExistenceResult.next();
            int bookCount = bookExistenceResult.getInt("count");
            if (bookCount == 0) {
                System.out.println("This book does not exist in the inventory.");
                return;
            }

            // Check if enough books are in stock
            String checkStockSQL = "SELECT stock_quantity FROM Books WHERE book_id = ?";
            PreparedStatement checkStockStmt = conn.prepareStatement(checkStockSQL);
            checkStockStmt.setInt(1, bookId);
            ResultSet resultSet = checkStockStmt.executeQuery();
            resultSet.next();
            int currentStock = resultSet.getInt("stock_quantity");
            if (currentStock < totalAmount) {
                System.out.println("There are not enough books in stock.");
                return;
            }
            
         // Validate order date
            Date currentDate = new Date(System.currentTimeMillis());
            if (!isValidDate(orderDate, currentDate)) {
                System.out.println("Invalid order date. Date constraints: "
                    + "Year between 2023 and 2070, Month between 01 and 12, Day between 01 and 28.");
                return;
            }

            // Insert order into Orders table
            String orderSQL = "INSERT INTO Orders (customer_id, book_id, order_date, total_amount) VALUES (?, ?, ?, ?)";
            orderStmt = conn.prepareStatement(orderSQL);
            orderStmt.setInt(1, customerId);
            orderStmt.setInt(2, bookId);
            orderStmt.setDate(3, orderDate);
            orderStmt.setInt(4, totalAmount);
            orderStmt.executeUpdate();

            // Update Books table (decrement stock_quantity by totalAmount)
            String updateBooksSQL = "UPDATE Books SET stock_quantity = stock_quantity - ? WHERE book_id = ?";
            updateBooksStmt = conn.prepareStatement(updateBooksSQL);
            updateBooksStmt.setInt(1, totalAmount);
            updateBooksStmt.setInt(2, bookId);
            updateBooksStmt.executeUpdate();

            conn.commit(); // Commit the transaction
            System.out.println("Order placed successfully. Book inventory updated.");
        } catch (SQLException | NumberFormatException ex) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction if any exception occurs
                } catch (SQLException rollbackEx) {
                    System.out.println("Failed to rollback transaction: " + rollbackEx.getMessage());
                }
            }
            System.out.println("Transaction failed: " + ex.getMessage());
        } finally {
        	try {
                if (orderStmt != null) {
                    orderStmt.close();
                }
                if (updateBooksStmt != null) {
                    updateBooksStmt.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true); // Restore default auto-commit behavior
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println("Failed to close resources: " + ex.getMessage());
            }
        }
        
        
    }
    
    private boolean customerExists(Connection conn, int customerId) throws SQLException {
        String query = "SELECT * FROM Customers WHERE customer_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }

    private void insertNewCustomer(Connection conn, int customerId, String firstName, String lastName, String email, String address) throws SQLException {
        String insertQuery = "INSERT INTO Customers (customer_id, first_name, last_name, email, address) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
        	pstmt.setInt(1, customerId);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, email);
            pstmt.setString(5, address);
            pstmt.executeUpdate();
        }
    }
    
    private boolean isValidDate(Date orderDate, Date currentDate) {
        String[] parts = orderDate.toString().split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        return (year >= 2023 && year <= 2070) &&
               (month >= 1 && month <= 12) &&
               (day >= 1 && day <= 28) &&
               orderDate.compareTo(currentDate) <= 0;
    }
    
    public void initiateOrderTransactions() {
        boolean continueTransactions = true;

        while (continueTransactions) {
            try {
                placeOrder();
            } catch (Exception e) {
                System.out.println("Invalid value");
            }

            System.out.println("Do you want to place another order? (Yes/No)");
            String input = scanner.nextLine().trim().toLowerCase();

            if (!input.equals("yes")) {
                continueTransactions = false;
            }
        }
    }

    
    
}
