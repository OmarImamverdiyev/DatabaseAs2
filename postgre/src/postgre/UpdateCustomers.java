package postgre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UpdateCustomers {
    private Scanner scanner;

    public UpdateCustomers() {
        this.scanner = new Scanner(System.in);
    }

    public void updateCustomerDetails() {
        App app = new App();

        GetCustomers getCustomers = new GetCustomers();
        getCustomers.getCustomers();

        System.out.println("Choose the customer_id you want to update:");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Which attribute(s) of this customer do you want to change? (first_name, last_name, email, address)");
        String attributeInput = scanner.nextLine().toLowerCase().trim();
        String[] attributes = attributeInput.split(", ");

        Map<String, String> attributeValues = new HashMap<>();

        for (String attribute : attributes) {
            System.out.println("Enter the new value for " + attribute + ":");
            String newValue = scanner.nextLine().trim();
            attributeValues.put(attribute, newValue);
        }

        updateCustomer(app, customerId, attributeValues);

        // Close the scanner when done
        scanner.close();
    }

    private void updateCustomer(App app, int customerId, Map<String, String> attributeValues) {
        // Constructing the SQL query based on the chosen attribute
        String SQL = "UPDATE customers SET ";

        for (String attribute : attributeValues.keySet()) {
            SQL += attribute + " = ?, ";
        }

        SQL = SQL.substring(0, SQL.length() - 2); // Remove the last comma and space
        SQL += " WHERE customer_id = ?";

        try (Connection conn = app.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            int parameterIndex = 1;
            for (String attribute : attributeValues.keySet()) {
                pstmt.setString(parameterIndex++, attributeValues.get(attribute));
            }
            pstmt.setInt(parameterIndex, customerId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Customer details updated successfully.");
            } else {
                System.out.println("No customer found with the given customer_id or no update performed.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
