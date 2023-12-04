package postgre;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class GetBooks {
    public void getBooks() {
        App app = new App();
        try (Connection conn = app.connect()) {
            if (conn != null) {
                Scanner scanner = new Scanner(System.in);

                boolean continueProcess = true;
                while (continueProcess) {
                    System.out.println("Enter column(s) to display (book_id, title, author_id, genre, price, stock_quantity, all_attributes, finish_process):");
                    String userInput = scanner.nextLine().toLowerCase().trim();

                    if (userInput.equals("finish_process")) {
                        continueProcess = false;
                        break;
                    }

                    String SQL = buildSQLQuery(userInput);
                    if (SQL != null) {
                        try (Statement stmt = conn.createStatement();
                             ResultSet rs = stmt.executeQuery(SQL)) {
                            
                            displayBooks(rs, userInput);
                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                    } else {
                        System.out.println("Invalid input or no columns specified.");
                    }
                }
                //scanner.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private String buildSQLQuery(String userInput) {
        if (userInput.isEmpty()) {
            return null;
        }

        if (userInput.equals("all_attributes")) {
            return "SELECT * FROM books";
        }

        StringBuilder queryBuilder = new StringBuilder("SELECT ");
        boolean addedColumn = false;

        if (userInput.contains("title")) {
            queryBuilder.append("title");
            addedColumn = true;
        }

        if (userInput.contains("book_id")) {
            if (addedColumn) {
                queryBuilder.append(", ");
            }
            queryBuilder.append("book_id");
            addedColumn = true;
        }

        if (userInput.contains("author_id")) {
            if (addedColumn) {
                queryBuilder.append(", ");
            }
            queryBuilder.append("author_id");
            addedColumn = true;
        }

        if (userInput.contains("genre")) {
            if (addedColumn) {
                queryBuilder.append(", ");
            }
            queryBuilder.append("genre");
            addedColumn = true;
        }

        if (userInput.contains("price")) {
            if (addedColumn) {
                queryBuilder.append(", ");
            }
            queryBuilder.append("price");
            addedColumn = true;
        }

        if (userInput.contains("stock_quantity")) {
            if (addedColumn) {
                queryBuilder.append(", ");
            }
            queryBuilder.append("stock_quantity");
        }

        queryBuilder.append(" FROM books");
        return queryBuilder.toString();
    }

    public void displayBooks(ResultSet rs, String userInput) throws SQLException {
        while (rs.next()) {
            if (userInput.equals("all_attributes")) {
                
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    System.out.print(rs.getString(i));
                    if (i < rs.getMetaData().getColumnCount()) {
                        System.out.print(", ");
                    }
                }
            } else {
               
            	
            	 if (userInput.contains("book_id")) {
                     System.out.print(rs.getInt("book_id"));
                     if (userInput.contains("title") || userInput.contains("author_id") || userInput.contains("genre") || userInput.contains("price") || userInput.contains("stock_quantity")) {
                         System.out.print(", ");
                     }
                 }
            	
            	
                if (userInput.contains("title")) {
                    System.out.print(rs.getString("title"));
                    if (userInput.contains("author_id") || userInput.contains("genre") || userInput.contains("price") || userInput.contains("stock_quantity")) {
                        System.out.print(", ");
                    }
                }

                if (userInput.contains("author_id")) {
                    System.out.print(rs.getInt("author_id"));
                    if (userInput.contains("genre") || userInput.contains("price") || userInput.contains("stock_quantity")) {
                        System.out.print(", ");
                    }
                }

                if (userInput.contains("genre")) {
                    System.out.print(rs.getString("genre"));
                    if (userInput.contains("price") || userInput.contains("stock_quantity")) {
                        System.out.print(", ");
                    }
                }

                if (userInput.contains("price")) {
                    System.out.print(rs.getDouble("price"));
                    if (userInput.contains("stock_quantity")) {
                        System.out.print(", ");
                    }
                }

                if (userInput.contains("stock_quantity")) {
                    System.out.print(rs.getInt("stock_quantity"));
                }
            }
            System.out.println(); 
        }

    
}
}

