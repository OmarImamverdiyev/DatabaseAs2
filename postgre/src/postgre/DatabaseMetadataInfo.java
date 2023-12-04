package postgre;

import java.sql.*;
import java.util.Scanner;

public class DatabaseMetadataInfo {
    private Connection connection;

    public DatabaseMetadataInfo(Connection connection) {
        this.connection = connection;
    }

    public void displayTableInfo(Scanner scanner) {
        boolean continueProcess = true;

        while (continueProcess) {
            try {
                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

                System.out.println("Tables in the Database:");
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    System.out.println(tableName);
                }

                System.out.println("Enter the table name to see its details (or 'exit' to finish):");
                String chosenTable = scanner.nextLine().trim();

                if (chosenTable.equalsIgnoreCase("exit")) {
                    continueProcess = false;
                    break;
                }

                if (tableExists(chosenTable, metaData)) {
                    displayColumnDetails(chosenTable, metaData);
                    displayPrimaryKeys(chosenTable, metaData);
                    displayForeignKeys(chosenTable, metaData);
                } else {
                    System.out.println("Table not found or no details available.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean tableExists(String tableName, DatabaseMetaData metaData) throws SQLException {
        ResultSet tables = metaData.getTables(null, null, tableName, null);
        return tables.next();
    }

    private void displayColumnDetails(String tableName, DatabaseMetaData metaData) throws SQLException {
        ResultSet columns = metaData.getColumns(null, null, tableName, null);
        System.out.println("\nColumns in table " + tableName + ":");
        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            String columnType = columns.getString("TYPE_NAME");
            int columnSize = columns.getInt("COLUMN_SIZE");
            boolean isNullable = columns.getInt("NULLABLE") == DatabaseMetaData.columnNullable;
            System.out.println("Column Name: " + columnName + ", Type: " + columnType +
                    ", Size: " + columnSize + ", Nullable: " + isNullable);
        }
    }

    private void displayPrimaryKeys(String tableName, DatabaseMetaData metaData) throws SQLException {
        ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
        System.out.println("\nPrimary Keys in table " + tableName + ":");
        while (primaryKeys.next()) {
            String primaryKeyColumnName = primaryKeys.getString("COLUMN_NAME");
            System.out.println("Primary Key Column: " + primaryKeyColumnName);
        }
    }

    private void displayForeignKeys(String tableName, DatabaseMetaData metaData) throws SQLException {
        ResultSet foreignKeys = metaData.getImportedKeys(null, null, tableName);
        if (!foreignKeys.next()) {
            System.out.println("\nTable " + tableName + " does not have foreign keys.");
            return;
        }

        System.out.println("\nForeign Keys in table " + tableName + ":");
        do {
            String foreignKeyName = foreignKeys.getString("FKCOLUMN_NAME");
            String primaryKeyTableName = foreignKeys.getString("PKTABLE_NAME");
            System.out.println("Foreign Key Column: " + foreignKeyName +
                    ", Referenced Table: " + primaryKeyTableName);
        } while (foreignKeys.next());
    }
    
    public void callMetadataView() {
        Scanner scanner = new Scanner(System.in);
        App app = new App();

        // Ask the user if they want to view Database Metadata
        System.out.println("Do you want to view Database Metadata? (Yes or No)");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes")) {
            Connection conn = app.connect();
            DatabaseMetadataInfo metadataInfo = new DatabaseMetadataInfo(conn);
            metadataInfo.displayTableInfo(scanner);
        }
        
        // Close the scanner after usage
      //  scanner.close();
    }

}
