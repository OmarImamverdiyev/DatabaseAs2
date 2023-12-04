package postgre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UpdateAuthors {
    private Scanner scanner;

    public UpdateAuthors() {
        this.scanner = new Scanner(System.in);
    }

    public void updateAuthorDetails() {
        App app = new App();

        GetAuthors getAuthors = new GetAuthors();
        getAuthors.getAuthors();

        System.out.println("Choose the author_id you want to update:");
        int authorId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Which attribute(s) of this author do you want to change? (author_name, nationality)");
        String attributeInput = scanner.nextLine().toLowerCase().trim();
        String[] attributes = attributeInput.split(", ");

        Map<String, String> attributeValues = new HashMap<>();

        for (String attribute : attributes) {
            System.out.println("Enter the new value for " + attribute + ":");
            String newValue = scanner.nextLine().trim();
            attributeValues.put(attribute, newValue);
        }

        updateAuthor(app, authorId, attributeValues);

        // Close the scanner when done
        scanner.close();
    }

    private void updateAuthor(App app, int authorId, Map<String, String> attributeValues) {
        // Constructing the SQL query based on the chosen attribute
        String SQL = "UPDATE authors SET ";

        for (String attribute : attributeValues.keySet()) {
            SQL += attribute + " = ?, ";
        }

        SQL = SQL.substring(0, SQL.length() - 2); // Remove the last comma and space
        SQL += " WHERE author_id = ?";

        try (Connection conn = app.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            int parameterIndex = 1;
            for (String attribute : attributeValues.keySet()) {
                pstmt.setString(parameterIndex++, attributeValues.get(attribute));
            }
            pstmt.setInt(parameterIndex, authorId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Author details updated successfully.");
            } else {
                System.out.println("No author found with the given author_id or no update performed.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
