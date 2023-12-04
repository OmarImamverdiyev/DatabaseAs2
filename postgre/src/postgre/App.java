package postgre;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class App {
  
    private final String url = "jdbc:postgresql://localhost/postgres";
  private final String user = "root";
  private final String password = "root";
  
   public Connection connect() {
          Connection conn = null;
          try {
        	  conn = DriverManager.getConnection(url, user, password);
              System.out.println("Connected to the PostgreSQL server successfully.");
          } catch (SQLException e) {
              System.out.println(e.getMessage());
          }

          return conn;
      }

   
     
      
      
}



