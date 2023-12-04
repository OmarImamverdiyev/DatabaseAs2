package postgre;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	 public static void main(String[] args)  {
        
         App app = new App();
        
         GetBooks booksGetter = new GetBooks();
         GetAuthors authorsGetter = new GetAuthors();
         GetCustomers customersGetter = new GetCustomers();
         GetOrders ordersGetter = new GetOrders();
         BookCount bookCounter = new BookCount();
         
  
         
         

         int bookCount = bookCounter.countBooks();
        // System.out.println("Number of tuples in the Books report: " + bookCount);
         
        

      
             
         
//           booksGetter.getBooks();   // Tested postive + with infinite scan
//         authorsGetter.getAuthors();   // Tested postive
//        customersGetter.getCustomers(); // Tested postive
//        ordersGetter.getOrders(); // Tested postive
         
         
//       InsertBook bookInsert = new InsertBook();
//       bookInsert.insertNewBook();
//       
//       Scanner scanner = new Scanner(System.in);
//       InsertAuthorsORCustomers insertData = new InsertAuthorsORCustomers();
//
//       System.out.println("Choose table (Authors, Customers): ");
//       String tableChoice = scanner.nextLine().trim();
//
//       insertData.insertRecord(tableChoice);
         
         
 //        DeleteAuthors deleteAuthors = new DeleteAuthors();
//       deleteAuthors.deleteAuthor();
         
      
         
//         DeleteCustomers deleteCustomer = new DeleteCustomers();
//         deleteCustomer.deleteCustomer();
           
         //DeleteBooks deleteBook = new DeleteBooks();
         //deleteBook.deleteBook();
         
         
//       UpdateBooks updateBooks = new UpdateBooks();
//       updateBooks.updateBookDetails();
       
//       UpdateAuthors updateAuthors = new UpdateAuthors();
//       updateAuthors.updateAuthorDetails();
       
//       UpdateCustomers updateCustomers = new UpdateCustomers();
//       updateCustomers.updateCustomerDetails();
         
         
//       TransactionManagement transactionManager = new TransactionManagement();
//       transactionManager.placeOrder();
         
//       Connection conn = app.connect();
//     DatabaseMetadataInfo metadataInfo = new DatabaseMetadataInfo(conn);
////
////       System.out.println("Displaying Database Metadata:");
//      metadataInfo.displayTableInfo();
//       
//       // Close the connection when done
//       try {
//           conn.close();
//       } catch (Exception e) {
//           e.printStackTrace();
//       }
         
       
       // Below is GetTableInfo
//       Scanner scanner = new Scanner(System.in);
//                  navigateTables(scanner, booksGetter, authorsGetter, customersGetter, ordersGetter);
                  
               // Below is TransactionManagement
//                TransactionManagement transactionManager = new TransactionManagement();
//                transactionManager.initiateOrderTransactions();
                
                //Calling Metadaa
//                Connection conn = app.connect();
//                DatabaseMetadataInfo metadataInfo = new DatabaseMetadataInfo(conn);
//                metadataInfo.callMetadataView();
         
         // Calling for Deletion
//         deleteFromTables();
         
         // Calling for Insertion
//         performInsertions();
         
         // Calling for Book Update
        // performBookUpdates();
                  
         Scanner scanner = new Scanner(System.in);
         String userInput;

         do {
             System.out.println("What do you want to do with the database?");
             System.out.println("(GetTableInfo, Insert, Delete, Update, MakeTransaction, ViewMetaData, Exit)");
             
             userInput = scanner.nextLine().trim().toLowerCase();

             switch (userInput) {
                 case "gettableinfo":
                     navigateTables(scanner, booksGetter, authorsGetter, customersGetter, ordersGetter);
                     break;
                 case "insert":
                     performInsertions();
                     break;
                 case "delete":
                     deleteFromTables();
                     break;
                 case "update":
                     performBookUpdates();
                     break;
                 case "maketransaction":
                     TransactionManagement transactionManager = new TransactionManagement();
                     transactionManager.initiateOrderTransactions();
                     break;
                 case "viewmetadata":
                     Connection conn = app.connect();
                     DatabaseMetadataInfo metadataInfo = new DatabaseMetadataInfo(conn);
                     metadataInfo.callMetadataView();
                     break;
                 case "exit":
                     break;
                 default:
                     System.out.println("Invalid input. Please try again.");
                     break;
             }

         } while (!userInput.equals("exit"));

         scanner.close();
         System.out.println("Exited the database program.");
     }    

         
	 
         // about insertion below
//         Scanner scanner = new Scanner(System.in);
//         InsertAuthorsORCustomers insertHandler = new InsertAuthorsORCustomers();
//         InsertBook insertBookHandler = new InsertBook();
//
//         boolean continueProcess = true;
//         while (continueProcess) {
//             System.out.println("Choose an action: (Insert, Finish_Process)");
//             String actionChoice = scanner.nextLine().trim();
//
//             switch (actionChoice.toLowerCase()) {
//                 case "insert":
//                     System.out.println("Choose table to insert (Authors, Books, Customers):");
//                     String tableChoice = scanner.nextLine().trim();
//
//                     switch (tableChoice.toLowerCase()) {
//                         case "authors":
//                             insertHandler.insertRecord("Authors");
//                             break;
//                         case "books":
//                             insertBookHandler.insertNewBook();
//                             break;
//                         case "customers":
//                             insertHandler.insertRecord("Customers");
//                             break;
//                         default:
//                             System.out.println("Invalid table choice.");
//                             break;
//                     }
//                     break;
//                 case "finish_process":
//                     continueProcess = false;
//                     break;
//                 default:
//                     System.out.println("Invalid action choice.");
//                     break;
//             }
//         }
//         scanner.close();
         
         // Below are for Deletions:
//         DeleteAuthors deleteAuthors = new DeleteAuthors();
//         DeleteBooks deleteBooks = new DeleteBooks();
//         DeleteCustomers deleteCustomers = new DeleteCustomers();
//         
//         Scanner scanner = new Scanner(System.in);
//
//         boolean continueProcess = true;
//         while (continueProcess) {
//             System.out.println("Choose a table to delete from: (Authors, Books, Customers, Finish)");
//             String tableChoice = scanner.nextLine().trim();
//
//             switch (tableChoice.toLowerCase()) {
//                 case "authors":
//                	 deleteAuthors.deleteAuthor();
//                     break;
//                 case "books":
//                	 deleteBooks.deleteBook();
//                     break;
//                 case "customers":
//                	 deleteCustomers.deleteCustomer();
//                     break;
//                 case "finish":
//                     continueProcess = false;
//                     break;
//                 default:
//                     System.out.println("Invalid choice. Please try again.");
//                     break;
//             }
//
//             if (continueProcess) {
//                 System.out.println("Do you want to delete from another table? (Yes/No)");
//                 String response = scanner.nextLine().trim().toLowerCase();
//                 if (!response.equals("yes")) {
//                     continueProcess = false;
//                 }
//             }
//         }
//
//         scanner.close();
         
         // Below is Updating
//         Scanner scanner = new Scanner(System.in);
//
//         while (true) {
//             System.out.println("Do you want to update the Book table? (Yes/No)");
//             String choice = scanner.nextLine().toLowerCase().trim();
//
//             if (choice.equals("yes")) {
//                 UpdateBooks updateBooks = new UpdateBooks();
//                 updateBooks.updateBookDetails();
//             } else if (choice.equals("no")) {
//                 System.out.println("Finished updating Book table.");
//                 break;
//             } else {
//                 System.out.println("Invalid choice. Please enter Yes or No.");
//             }
//         }
//
//         scanner.close();
         
         // Below is MetadataInfo
         
//         Scanner scanner = new Scanner(System.in);
//
//         // Ask the user if they want to view Database Metadata
//         System.out.println("Do you want to view Database Metadata? (Yes or No)");
//         String response = scanner.nextLine().trim().toLowerCase();
//
//         if (response.equals("yes")) {
//             Connection conn = app.connect();
//             DatabaseMetadataInfo metadataInfo = new DatabaseMetadataInfo(conn);
//             metadataInfo.displayTableInfo(scanner);
//         }
//        
//        	// Close the scanner after usage
//             scanner.close();
         
         
         // Below is TransactionManagement
//         TransactionManagement transactionManager = new TransactionManagement();
//         transactionManager.initiateOrderTransactions();
         
         //Calling Metadaa
//         Connection conn = app.connect();
//         DatabaseMetadataInfo metadataInfo = new DatabaseMetadataInfo(conn);
//         metadataInfo.callMetadataView();
         
         
       
	 
        public static void navigateTables(Scanner scanner, GetBooks booksGetter, GetAuthors authorsGetter,
                GetCustomers customersGetter, GetOrders ordersGetter) {
while (true) {
System.out.println("From which table do you want to find information?");
System.out.println("1. Books");
System.out.println("2. Authors");
System.out.println("3. Customers");
System.out.println("4. Orders");
System.out.println("5. Exit");

int choice = scanner.nextInt();
scanner.nextLine(); // Consume newline

switch (choice) {
case 1:
booksGetter.getBooks();
break;
case 2:
authorsGetter.getAuthors();
break;
case 3:
customersGetter.getCustomers();
break;
case 4:
ordersGetter.getOrders();
break;
case 5:
return;
default:
System.out.println("Invalid choice. Please select a valid option.");
}

System.out.println("Do you want to find information from another table? (yes/no)");
String continueChoice = scanner.nextLine().trim().toLowerCase();

if (!continueChoice.equals("yes")) {
return;
}
}



     
}
        
        public static void deleteFromTables() {
            DeleteAuthors deleteAuthors = new DeleteAuthors();
            DeleteBooks deleteBooks = new DeleteBooks();
            DeleteCustomers deleteCustomers = new DeleteCustomers();

            Scanner scanner = new Scanner(System.in);

            boolean continueProcess = true;
            while (continueProcess) {
                System.out.println("Choose a table to delete from: (Authors, Books, Customers, Finish)");
                String tableChoice = scanner.nextLine().trim();

                switch (tableChoice.toLowerCase()) {
                    case "authors":
                        deleteAuthors.deleteAuthor();
                        break;
                    case "books":
                        deleteBooks.deleteBook();
                        break;
                    case "customers":
                        deleteCustomers.deleteCustomer();
                        break;
                    case "finish":
                        continueProcess = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }

                if (continueProcess) {
                    System.out.println("Do you want to delete from another table? (Yes/No)");
                    String response = scanner.nextLine().trim().toLowerCase();
                    if (!response.equals("yes")) {
                        continueProcess = false;
                    }
                }
            }

            //scanner.close();
        }
        
        public static void performInsertions() {
            Scanner scanner = new Scanner(System.in);
            InsertAuthorsORCustomers insertHandler = new InsertAuthorsORCustomers();
            InsertBook insertBookHandler = new InsertBook();

            boolean continueProcess = true;
            while (continueProcess) {
                System.out.println("Choose an action: (Insert, Finish_Process)");
                String actionChoice = scanner.nextLine().trim();

                switch (actionChoice.toLowerCase()) {
                    case "insert":
                        System.out.println("Choose table to insert (Authors, Books, Customers):");
                        String tableChoice = scanner.nextLine().trim();

                        switch (tableChoice.toLowerCase()) {
                            case "authors":
                                insertHandler.insertRecord("Authors");
                                break;
                            case "books":
                                insertBookHandler.insertNewBook();
                                break;
                            case "customers":
                                insertHandler.insertRecord("Customers");
                                break;
                            default:
                                System.out.println("Invalid table choice.");
                                break;
                        }
                        break;
                    case "finish_process":
                        continueProcess = false;
                        break;
                    default:
                        System.out.println("Invalid action choice.");
                        break;
                }
            }
           // scanner.close();
        }
        
        public static void performBookUpdates()  {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Do you want to update the Book table? (Yes/No)");
                String choice = scanner.nextLine().toLowerCase().trim();

                if (choice.equals("yes")) {
                    UpdateBooks updateBooks = new UpdateBooks();
                    updateBooks.updateBookDetails();
                } else if (choice.equals("no")) {
                    System.out.println("Finished updating Book table.");
                    break;
                } else {
                    System.out.println("Invalid choice. Please enter Yes or No.");
                }
            }

            //scanner.close();
        }

}
