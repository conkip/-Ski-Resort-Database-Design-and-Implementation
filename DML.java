import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

/*+----------------------------------------------------------------------
||  Class DML
||
||         Author:  Group 14 – Connor, Luis, Mohammad, Nathan
||
||        Purpose:  This class provides methods to perform Data Manipulation
||                  Language (DML) operations such as insert, update, and
||                  delete on ski resort database tables. It enables
||                  interactive modifications to the Oracle database via
||                  a terminal-based menu system.
||
||  Inherits From:  None.
||
||     Interfaces:  None.
||
|+-----------------------------------------------------------------------
||
||      Constants:  None.
||
|+-----------------------------------------------------------------------
||
||   Constructors:
||       DML(Connection dbconn, Scanner scanner)
||
||  Class Methods:  None.
||
||  Inst. Methods:
||       void performDML()
||       void members()
||       void skiPasses()
||       void skiLessons()
||       void equipmentInventory()
||       void equipmentRentals()
||       void lessons()
||
++-----------------------------------------------------------------------*/
public class DML {
  private Connection dbconn;
  private Statement stmt;
  private ResultSet rset;
  private Scanner scanner;

  /*---------------------------------------------------------------------
  |  Constructor DML
  |
  |  Purpose:  Initializes the DML object with a JDBC database connection
  |      and a Scanner object for user input. It sets up a SQL statement
  |      to perform data manipulation operations (insert, update, delete).
  |
  |  Pre-condition:
  |      - dbconn must be a valid, open JDBC Connection.
  |      - scanner must be an initialized Scanner object.
  |
  |  Post-condition:
  |      - A Statement object is created for executing DML SQL commands.
  |      - If an error occurs during statement creation, an error message
  |        is printed and the program continues.
  |
  |  Parameters:
  |      dbconn -- (IN) a valid JDBC Connection to the Oracle database.
  |      scanner -- (IN) a Scanner object for reading user input.
  |
  |  Returns:  None.
  *-------------------------------------------------------------------*/
  public DML(Connection dbconn, Scanner scanner) {
    this.dbconn = dbconn;
    this.scanner = scanner;
    try {
      stmt = dbconn.createStatement();
    } catch (SQLException e) {
      System.err.println("*** SQLException:  " + "Could not create statement.");
      System.err.println("\tMessage:   " + e.getMessage());
      System.err.println("\tSQLState:  " + e.getSQLState());
      System.err.println("\tErrorCode: " + e.getErrorCode());
    }
  }

  /*---------------------------------------------------------------------
  |  Method performDML
  |
  |  Purpose:  Prompts the user to select a table to perform a DML
  |      operation on (e.g., insert, update, delete). Delegates the
  |      selected operation to a corresponding handler method.
  |
  |  Pre-condition:  Scanner must be initialized; database connection
  |      must be valid and tables must exist in the schema.
  |
  |  Post-condition:  The chosen DML handler method is called. Invalid
  |      inputs are handled gracefully.
  |
  |  Parameters:  None.
  |
  |  Returns:  None.
  *-------------------------------------------------------------------*/
  public void performDML() {
    // This method will be used to call the DML methods
    // and display the results to the user.
    System.out.println("Which table would you like to work with?");
    System.out.println(
        "1) - Members\n"
            + "2) - Ski Passes\n"
            + "3) - Ski Lessons\n"
            + "4) - Equipment Inventory\n"
            + "5) - Equipment Rentals\n"
            + "6) - Lessons\n");

    try {
      int choice = scanner.nextInt();
      scanner.nextLine(); // Consume the newline character

      if (choice == 1) {
        // Call method for Members
        members();
      } else if (choice == 2) {
        // Call method for Ski Passes
        skiPasses();
      } else if (choice == 3) {
        // Call method for Ski Lessons
        skiLessons();
      } else if (choice == 4) {
        // Call method for Equipment Inventory
        equipmentInventory();
      } else if (choice == 5) {
        // Call method for Equipment Rentals
        equipmentRentals();
      } else if (choice == 6) {
        // Call method for Lessons
        lessons();
      } else {
        System.out.println("Invalid choice. Please try again.");
      }
    } catch (InputMismatchException e) {
      System.out.println("Invalid input. Please enter a number.");
      scanner.nextLine(); // Clear the invalid input
      performDML(); // Call the method again to allow for retry
    }
  }

  // Add your DML methods here
  private void members() {
    // Implement the logic for Members table
  }

  private void skiPasses() {
    // Implement the logic for Ski Passes table
  }

  private void skiLessons() {
    // Implement the logic for Ski Lessons table
  }

  /*---------------------------------------------------------------------
  |  Method equipmentInventory
  |
  |  Purpose:  Manages the Equipment inventory from a user interface
  |      perspective. Prompts the user for whether they want to see a summary
  |      of active equipment, then asks them to choose an action (add, update,
  |      or archive). Delegates to EquipmentInventoryHandler for functionality.
  |
  |  Pre-condition:  Equipment table must exist. User must be connected and input must be valid.
  |
  |  Post-condition:  Depending on the user's choice, the equipment table may be
  |      modified (new equipment added, existing updated, or archived).
  |
  |  Parameters:  None
  |
  |  Returns:  None
  *-------------------------------------------------------------------*/
  private void equipmentInventory() {
    try {
      System.out.print("Would you like to view a summary of all active equipment? (yes/no): ");
      String input = scanner.nextLine().trim().toLowerCase();

      if (input.equals("yes") || input.equals("y")) {
        EquipmentInventoryHandler.displaySummary(dbconn);
      }

      System.out.println("\nChoose an action:");
      System.out.println("1. Add new equipment");
      System.out.println("2. Update existing equipment");
      System.out.println("3. Delete/Archive equipment");
      System.out.print("Enter your choice: ");

      int choice = scanner.nextInt();
      scanner.nextLine(); // clear newline

      if (choice == 1) {
        System.out.print("Enter equipment type: ");
        String type = scanner.nextLine();
        System.out.print("Enter equipment size: ");
        String size = scanner.nextLine();
        EquipmentInventoryHandler.addEquipment(dbconn, type, size);

      } else if (choice == 2) {
        System.out.print("Enter equipment ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new equipment type: ");
        String type = scanner.nextLine();
        System.out.print("Enter new equipment size: ");
        String size = scanner.nextLine();
        EquipmentInventoryHandler.updateEquipment(dbconn, id, type, size);

      } else if (choice == 3) {
        System.out.print("Enter equipment ID to archive: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        EquipmentInventoryHandler.archiveEquipment(dbconn, id);

      } else {
        System.out.println("Invalid choice.");
      }

    } catch (InputMismatchException e) {
      System.out.println("Invalid input. Please enter a number.");
      scanner.nextLine();
    }
  }

  private void equipmentRentals() {
    // Implement the logic for Equipment Rentals table
  }

  private void lessons() {
    // Implement the logic for Lessons table
  }
}
