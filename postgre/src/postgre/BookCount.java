package postgre;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BookCount {
    public int countBooks() {
        App app = new App();
        int count = 0;

        try (Connection conn = app.connect();
             Statement stmt = conn.createStatement()) {

            String SQL = "SELECT COUNT(*) AS book_count FROM books";
            ResultSet rs = stmt.executeQuery(SQL);
            
            // Retrieve the count from the result set
            if (rs.next()) {
                count = rs.getInt("book_count");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return count;
    }
}
