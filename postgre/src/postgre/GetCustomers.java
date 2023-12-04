package postgre;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class GetCustomers {
    public void getCustomers() {
        App app = new App();
        try (Connection conn = app.connect()) {
            if (conn != null) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter column(s) to display (customer_id, first_name, last_name, email, address, all_attributes, finish_process):");
                String userInput = scanner.nextLine().toLowerCase().trim();

                boolean continueProcess = true;
                while (continueProcess) {
                    if (userInput.equals("finish_process")) {
                        continueProcess = false;
                        break;
                    }

                    String SQL = buildSQLQuery(userInput);
                    if (SQL != null) {
                        try (Statement stmt = conn.createStatement();
                             ResultSet rs = stmt.executeQuery(SQL)) {
                            // display customer information based on user input
                            displayCustomers(rs, userInput);
                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                    } else {
                        System.out.println("Invalid input or no columns specified.");
                    }

                    System.out.println("Enter column(s) to display (customer_id, first_name, last_name, email, address, all_attributes, finish_process):");
                    userInput = scanner.nextLine().toLowerCase().trim();
                }
             //   scanner.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private String buildSQLQuery(String userInput) {
        if (userInput.isEmpty() || userInput.equals("all_attributes")) {
            return "SELECT * FROM customers";
        }

        StringBuilder queryBuilder = new StringBuilder("SELECT ");
        boolean addedColumn = false;

        if (userInput.contains("customer_id")) {
            queryBuilder.append("customer_id");
            addedColumn = true;
        }

        if (userInput.contains("first_name")) {
            if (addedColumn) {
                queryBuilder.append(", ");
            }
            queryBuilder.append("first_name");
            addedColumn = true;
        }

        if (userInput.contains("last_name")) {
            if (addedColumn) {
                queryBuilder.append(", ");
            }
            queryBuilder.append("last_name");
            addedColumn = true;
        }

        if (userInput.contains("email")) {
            if (addedColumn) {
                queryBuilder.append(", ");
            }
            queryBuilder.append("email");
            addedColumn = true;
        }

        if (userInput.contains("address")) {
            if (addedColumn) {
                queryBuilder.append(", ");
            }
            queryBuilder.append("address");
        }

        queryBuilder.append(" FROM customers");
        return queryBuilder.toString();
    }

    public void displayCustomers(ResultSet rs, String userInput) throws SQLException {
        while (rs.next()) {
            if (userInput.equals("all_attributes")) {
                // Display all attributes if user input is 'all_attributes'
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    System.out.print(rs.getString(i));
                    if (i < rs.getMetaData().getColumnCount()) {
                        System.out.print(", ");
                    }
                }
            } else {
                // Display selected attributes based on user input
                if (userInput.contains("customer_id")) {
                    System.out.print(rs.getInt("customer_id"));
                    if (userInput.contains("first_name") || userInput.contains("last_name") || userInput.contains("email") || userInput.contains("address")) {
                        System.out.print(", ");
                    }
                }

                if (userInput.contains("first_name")) {
                    System.out.print(rs.getString("first_name"));
                    if (userInput.contains("last_name") || userInput.contains("email") || userInput.contains("address")) {
                        System.out.print(", ");
                    }
                }

                if (userInput.contains("last_name")) {
                    System.out.print(rs.getString("last_name"));
                    if (userInput.contains("email") || userInput.contains("address")) {
                        System.out.print(", ");
                    }
                }

                if (userInput.contains("email")) {
                    System.out.print(rs.getString("email"));
                    if (userInput.contains("address")) {
                        System.out.print(", ");
                    }
                }

                if (userInput.contains("address")) {
                    System.out.print(rs.getString("address"));
                }
            }
            System.out.println(); // Move to the next line for the next record
        }
    }
}
