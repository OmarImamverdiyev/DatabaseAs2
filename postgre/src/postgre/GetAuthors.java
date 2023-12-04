package postgre;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class GetAuthors {
    public void getAuthors() {
        App app = new App();
        try (Connection conn = app.connect()) {
            if (conn != null) {
                Scanner scanner = new Scanner(System.in);

                boolean continueProcess = true;
                while (continueProcess) {
                    System.out.println("Enter column(s) to display (author_id, author_name, nationality, all_attributes, finish_process):");
                    String userInput = scanner.nextLine().toLowerCase().trim();

                    if (userInput.equals("finish_process")) {
                        continueProcess = false;
                        break;
                    }

                    String SQL = buildSQLQuery(userInput);
                    if (SQL != null) {
                        try (Statement stmt = conn.createStatement();
                             ResultSet rs = stmt.executeQuery(SQL)) {
                            // display author information based on user input
                            displayAuthors(rs, userInput);
                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                    } else {
                        System.out.println("Invalid input or no columns specified.");
                    }
                }
             //   scanner.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private String buildSQLQuery(String userInput) {
        if (userInput.isEmpty() || userInput.equals("all_attributes")) {
            return "SELECT * FROM authors";
        }

        StringBuilder queryBuilder = new StringBuilder("SELECT ");
        boolean addedColumn = false;

        if (userInput.contains("author_id")) {
            queryBuilder.append("author_id");
            addedColumn = true;
        }

        if (userInput.contains("author_name")) {
            if (addedColumn) {
                queryBuilder.append(", ");
            }
            queryBuilder.append("author_name");
            addedColumn = true;
        }

        if (userInput.contains("nationality")) {
            if (addedColumn) {
                queryBuilder.append(", ");
            }
            queryBuilder.append("nationality");
        }

        queryBuilder.append(" FROM authors");
        return queryBuilder.toString();
    }

    public void displayAuthors(ResultSet rs, String userInput) throws SQLException {
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
                if (userInput.contains("author_id")) {
                    System.out.print(rs.getInt("author_id"));
                    if (userInput.contains("author_name") || userInput.contains("nationality")) {
                        System.out.print(", ");
                    }
                }

                if (userInput.contains("author_name")) {
                    System.out.print(rs.getString("author_name"));
                    if (userInput.contains("nationality")) {
                        System.out.print(", ");
                    }
                }

                if (userInput.contains("nationality")) {
                    System.out.print(rs.getString("nationality"));
                }
            }
            System.out.println(); // Move to the next line for the next record
        }
    }
}
