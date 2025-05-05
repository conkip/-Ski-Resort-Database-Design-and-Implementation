import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DML {
  private Connection dbconn;
  private Statement stmt;
  private ResultSet rset;
  private Scanner scanner;

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

  private void equipmentInventory() {
    // Implement the logic for Equipment Inventory table
  }

  private void equipmentRentals() {
    // Implement the logic for Equipment Rentals table
  }

  private void lessons() {
    // Implement the logic for Lessons table
  }
}
