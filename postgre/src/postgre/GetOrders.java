package postgre;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class GetOrders {
    public void getOrders() {
        App app = new App();
        try (Connection conn = app.connect()) {
            if (conn != null) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter column(s) to display (order_id, customer_id, book_id, order_date, total_amount, all_attributes, finish_process):");
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
                            // display order information based on user input
                            displayOrders(rs, userInput);
                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                    } else {
                        System.out.println("Invalid input or no columns specified.");
                    }

                    System.out.println("Enter column(s) to display (order_id, customer_id, book_id, order_date, total_amount, all_attributes, finish_process):");
                    userInput = scanner.nextLine().toLowerCase().trim();
                }
               // scanner.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private String buildSQLQuery(String userInput) {
        if (userInput.isEmpty() || userInput.equals("all_attributes")) {
            return "SELECT * FROM orders";
        }

        StringBuilder queryBuilder = new StringBuilder("SELECT ");
        boolean addedColumn = false;

        if (userInput.contains("order_id")) {
            queryBuilder.append("order_id");
            addedColumn = true;
        }

        if (userInput.contains("customer_id")) {
            if (addedColumn) {
                queryBuilder.append(", ");
            }
            queryBuilder.append("customer_id");
            addedColumn = true;
        }

        if (userInput.contains("book_id")) {
            if (addedColumn) {
                queryBuilder.append(", ");
            }
            queryBuilder.append("book_id");
            addedColumn = true;
        }

        if (userInput.contains("order_date")) {
            if (addedColumn) {
                queryBuilder.append(", ");
            }
            queryBuilder.append("order_date");
            addedColumn = true;
        }

        if (userInput.contains("total_amount")) {
            if (addedColumn) {
                queryBuilder.append(", ");
            }
            queryBuilder.append("total_amount");
        }

        queryBuilder.append(" FROM orders");
        return queryBuilder.toString();
    }

    public void displayOrders(ResultSet rs, String userInput) throws SQLException {
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
                if (userInput.contains("order_id")) {
                    System.out.print(rs.getInt("order_id"));
                    if (userInput.contains("customer_id") || userInput.contains("book_id") || userInput.contains("order_date") || userInput.contains("total_amount")) {
                        System.out.print(", ");
                    }
                }

                if (userInput.contains("customer_id")) {
                    System.out.print(rs.getInt("customer_id"));
                    if (userInput.contains("book_id") || userInput.contains("order_date") || userInput.contains("total_amount")) {
                        System.out.print(", ");
                    }
                }

                if (userInput.contains("book_id")) {
                    System.out.print(rs.getInt("book_id"));
                    if (userInput.contains("order_date") || userInput.contains("total_amount")) {
                        System.out.print(", ");
                    }
                }

                if (userInput.contains("order_date")) {
                    System.out.print(rs.getDate("order_date"));
                    if (userInput.contains("total_amount")) {
                        System.out.print(", ");
                    }
                }

                if (userInput.contains("total_amount")) {
                    System.out.print(rs.getDouble("total_amount"));
                }
            }
            System.out.println(); // Move to the next line for the next record
        }
    }
}
