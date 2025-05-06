import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Date;

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
    try {
      System.out.print("Welcome, to the membership portal!");

      System.out.println("\nChoose an action:");
      System.out.println("1. Register a member");
      System.out.println("2. Update existing member's information");
      System.out.println("3. Delete a memebership");
      System.out.print("Enter your choice: ");

      int choice = scanner.nextInt();
      scanner.nextLine(); // clear newline

      if (choice == 1) {
        System.out.println("Enter your name (first and last):");
        String name = scanner.nextLine();
        System.out.println("Enter your phone number (XXX-XXXX):");
        String phoneNumber = scanner.nextLine();
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        System.out.println("Enter your DOB (YYYY-MM-DD)");
        String dob = scanner.nextLine();
        System.out.println("Enter your emergency contact's name (first and last):");
        String emergencyName = scanner.nextLine();
        System.out.println("Enter your emergency contact's phone number (XXX-XXXX):");
        String emergencyPhone = scanner.nextLine();
        System.out.println("Enter your emergency contact's email:");
        String emergencyEmail = scanner.nextLine();

        try {
          LocalDate dateBirth = LocalDate.parse(dob);

          MemberHandler.addMember(dbconn, name, phoneNumber, email, dateBirth, 
                  emergencyName, emergencyPhone, emergencyEmail);
        } catch (IllegalArgumentException e) {
          System.out.println("Invalid date format. Please use YYYY-MM-DD.");
          members(); // Retry
        }

      } else if (choice == 2) {
        System.out.println("Enter the member ID of the membership you would like to update:");
        int memberID = scanner.nextInt();

        System.out.println("\nWhat would you like to update:");
        System.out.println("1. Phone Number");
        System.out.println("2. Email");
        System.out.println("3. Emergency Contact");
        System.out.print("Enter your choice:");

        choice = scanner.nextInt();
        scanner.nextLine(); // clear newline

        if (choice == 1) {
          System.out.println("Enter new phone number:");
          String newPhone = scanner.nextLine();
          
          MemberHandler.updateMember(dbconn, memberID, newPhone, null, 
                null, null, null);
        } else if(choice == 2) {
          System.out.println("Enter new email:");
          String newEmail = scanner.nextLine();
          
          MemberHandler.updateMember(dbconn, memberID, null, newEmail, 
                null, null, null);
        } else if(choice == 3) {
          System.out.println("Enter your emergency contact's name (first and last):");
          String newEmergencyName = scanner.nextLine();

          System.out.println("Enter your emergency contact's phone number:");
          String newEmergencyPhone = scanner.nextLine();

          System.out.println("Enter your emergency contact's email:");
          String newEmergencyEmail = scanner.nextLine();
          
          MemberHandler.updateMember(dbconn, memberID, null, null, 
                newEmergencyName, newEmergencyPhone, newEmergencyEmail);
        } else {
          System.out.println("Invalid choice.");
          members();
        }

      } else if (choice == 3) {
        System.out.println("Enter the memberID for the account you would like to delete:");
        String memberID = scanner.nextLine();

        MemberHandler.deleteMember(dbconn, memberID);
      } else {
        System.out.println("Invalid choice.");
        members();
      }
    } catch (InputMismatchException e) {
      System.out.println("Invalid input. Please enter a number.");
      scanner.nextLine();
    }
  }

  private void skiPasses() {
	  System.out.println("- Ski Pass Management -");
	  System.out.println("1) Add new Ski Pass.");
	  System.out.println("2) Update Usage Information on a Ski Pass.");
	  System.out.println("3) Remove a Ski Pass.");
	  
	  try (Scanner sc = new Scanner(System.in)) {
			System.out.print("Select an option or type 'Quit' to exit: ");
			while (true) {
				String choice = sc.nextLine();
				if (choice.equals("1")) {
					
					SkiPassHandler.addPass(dbconn);
					return;
				} else if (choice.equals("2")) {
					SkiPassHandler.updatePass(dbconn);
					return;
				} else if (choice.equals("3")) {
					SkiPassHandler.deletePass(dbconn);
					return;
				} 
				else if (choice.equals("Quit") || choice.equals("quit")) {
					return;
				} else
					System.out.println("Invalid Input, Try Again.");
			}
		}
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

  /*---------------------------------------------------------------------
  |  Method equipmentRentals
  |
  |  Purpose:  Manages equipment rental records. Allows the user to view
  |           all current rentals, add a new rental record, update the
  |           return status of a rental, or delete a rental entry if it
  |           was created in error and not used.
  |
  |  Pre-condition:
  |     - User must be connected to a valid database.
  |     - The Rental, Equipment, and Pass tables must exist and be populated properly.
  |
  |  Post-condition:
  |     - The Rental table may be modified depending on user input.
  |
  |  Parameters: None.
  |
  |  Returns: None.
  *-------------------------------------------------------------------*/
  private void equipmentRentals() {
    try {
      System.out.print("Would you like to view all rental records? (yes/no): ");
      String input = scanner.nextLine().trim().toLowerCase();

      if (input.equals("yes") || input.equals("y")) {
        RentalHandler.displayAllRentals(dbconn);
      }

      System.out.println("\nChoose an action:");
      System.out.println("1. Add new rental");
      System.out.println("2. Update return status");
      System.out.println("3. Delete rental");
      System.out.print("Enter your choice: ");

      int choice = scanner.nextInt();
      scanner.nextLine(); // Clear newline

      if (choice == 1) {
        System.out.print("Enter equipment ID: ");
        int equipmentID = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter pass ID: ");
        String passID = scanner.nextLine();
        System.out.print("Enter rental start date (YYYY-MM-DD): ");
        String rentalDate = scanner.nextLine();
        System.out.print("Enter rental start time (HH:MM:SS): ");
        String rentalTime = scanner.nextLine();
        rentalTime = rentalDate + " " + rentalTime;
        try {
          Timestamp rentalTimestamp = Timestamp.valueOf(rentalTime);
          RentalHandler.addRental(dbconn, equipmentID, passID, rentalTimestamp);
        } catch (IllegalArgumentException e) {
          System.out.println("Invalid date/time format. Please use YYYY-MM-DD HH:MM:SS.");
          equipmentRentals(); // Retry
        }

      } else if (choice == 2) {
        System.out.print("Enter equipment ID to update return status: ");
        int rentalID = scanner.nextInt();
        scanner.nextLine();

        RentalHandler.updateReturnStatus(dbconn, rentalID);

      } else if (choice == 3) {
        System.out.print("Enter rental ID to delete: ");
        int rentalID = scanner.nextInt();
        scanner.nextLine();

        RentalHandler.deleteRental(dbconn, rentalID);

      } else {
        System.out.println("Invalid choice.");
      }

    } catch (InputMismatchException e) {
      System.out.println("Invalid input. Please enter a number.");
      scanner.nextLine(); // Clear buffer
    }
  }

  /*---------------------------------------------------------------------
  |  Method lessons
  |
  |  Purpose:  Manages lesson purchases via interactive user input.
  |           Allows users to view, add, update, delete, or record
  |           usage of lesson purchase records stored in the database.
  |
  |  Pre-condition: Scanner and dbconn must be initialized and valid.
  |
  |  Post-condition: The LessonPurchase table may be modified based
  |           on the user's selected operation.
  |
  |  Parameters: None
  |
  |  Returns: None
  *-------------------------------------------------------------------*/
  private void lessons() {
    try {
      System.out.print("Would you like to view all lesson purchases? (yes/no): ");
      String input = scanner.nextLine().trim().toLowerCase();

      if (input.equals("yes") || input.equals("y")) {
        LessonPurchaseHandler.displayAllPurchases(dbconn);
      }

      System.out.println("\nChoose an action:");
      System.out.println("1. Add new lesson purchase");
      System.out.println("2. Update remaining sessions");
      System.out.println("3. Record a session usage");
      System.out.println("4. Delete a lesson purchase");
      System.out.print("Enter your choice: ");

      int choice = scanner.nextInt();
      scanner.nextLine(); // clear newline

      if (choice == 1) { // Add new lesson purchase
        System.out.print("Enter member ID: ");
        int memberID = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter lesson ID: ");
        int lessonID = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter number of sessions purchased: ");
        int sessions = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter total price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        LessonPurchaseHandler.addPurchase(dbconn, memberID, lessonID, sessions, price);

      } else if (choice == 2) { // Update remaining sessions
        System.out.print("Enter order ID to update: ");
        int orderID = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new remaining sessions: ");
        int remaining = scanner.nextInt();
        scanner.nextLine();

        LessonPurchaseHandler.updateRemainingSessions(dbconn, orderID, remaining);

      } else if (choice == 3) { // Record session usage
        System.out.print("Enter order ID to record session usage: ");
        int orderID = scanner.nextInt();
        scanner.nextLine();

        LessonPurchaseHandler.recordSessionUsage(dbconn, orderID);

      } else if (choice == 4) { // Delete lesson purchase
        System.out.print("Enter order ID to delete: ");
        int orderID = scanner.nextInt();
        scanner.nextLine();

        LessonPurchaseHandler.deletePurchase(dbconn, orderID);

      } else {
        System.out.println("Invalid choice.");
      }

    } catch (InputMismatchException e) {
      System.out.println("Invalid input. Please enter the correct data type.");
      scanner.nextLine(); // clear the buffer
    }
  }
}
