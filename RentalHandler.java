import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Random;

/*+----------------------------------------------------------------------
||  Class RentalHandler
||
||         Author:  Group 14 – Connor, Luis, Mohammad, Nathan
||
||        Purpose:  This static utility class manages the ski resort's
||                  equipment rental records. It provides functions for:
||                    - displaying all rental records,
||                    - adding a new rental transaction,
||                    - updating return status,
||                    - and deleting incorrect records.
||
||                  Rental records link members, ski passes, and equipment,
||                  and are essential for activity tracking and pass usage.
||
||  Inherits From:  None.
||
||     Interfaces:  None.
||
||   Constructors:  None (all methods are static).
||
||  Class Methods:
||       void displayAllRentals(Connection dbconn)
||       void addRental(Connection dbconn, int equipmentID, String passID, Timestamp rentalTime)
||       void updateReturnStatus(Connection dbconn, int rentalID)
||       void deleteRental(Connection dbconn, int rentalID)
||
++-----------------------------------------------------------------------*/
public class RentalHandler {

  public static void displayAllRentals(Connection dbconn) {
    // SQL query to select all rental records
    String query =
        "SELECT rentalID, equipmentID, passID, rentalTime, returnStatus "
            + "FROM nathanlamont.Rental ORDER BY rentalID";
    try (Statement stmt = dbconn.createStatement();
        ResultSet rset = stmt.executeQuery(query)) {

      // Print the header for the rental records
      System.out.println("\n--- Equipment Rentals ---");
      System.out.printf(
          "%-10s %-13s %-10s %-22s %-15s\n",
          "Rental ID", "Equipment ID", "Pass ID", "Rental Time", "Returned");

      // Print each rental record
      while (rset.next()) {
        int rentalID = rset.getInt("rentalID");
        int equipmentID = rset.getInt("equipmentID");
        String passID = rset.getString("passID");
        Timestamp rentalTime = rset.getTimestamp("rentalTime");
        int returned = rset.getInt("returnStatus");

        System.out.printf(
            "%-10d %-13d %-10s %-22s %-15s\n",
            rentalID, equipmentID, passID, rentalTime.toString(), (returned == 1 ? "Yes" : "No"));
      }
    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }
  }

  /*---------------------------------------------------------------------
  |  Method addRental
  |
  |  Purpose: Adds a new equipment rental record to the Rental table
  |           using the provided equipment ID, pass ID, and rental timestamp.
  |           The return status is initially set to 0 (not returned).
  |
  |  Pre-condition:
  |     - `dbconn` must be a valid and open JDBC connection.
  |     - `equipmentID` and `passID` must reference valid existing entries.
  |     - `rentalTime` must be a valid timestamp.
  |
  |  Post-condition:
  |     - A new row is inserted into the Rental table.
  |
  |  Parameters:
  |     dbconn     -- Active JDBC connection.
  |     equipmentID -- ID of the rented equipment.
  |     passID     -- Pass ID used to rent the equipment.
  |     rentalTime -- Timestamp when the rental occurred.
  |
  |  Returns: None.
  *-------------------------------------------------------------------*/
  public static void addRental(
      Connection dbconn, int equipmentID, String passID, Timestamp rentalTime) {
    try (Statement stmt = dbconn.createStatement()) {
      // Check if the equipment is already rented out
      String checkSQL =
          "SELECT COUNT(*) FROM nathanlamont.Rental WHERE equipmentID = "
              + equipmentID
              + " AND returnStatus = 0";
      ResultSet rset = stmt.executeQuery(checkSQL);
      if (rset.next() && rset.getInt(1) > 0) {
        System.out.println("Cannot add rental. Equipment is currently rented out.");
        return;
      }

      // check if the pass ID exists and is not expired
      String checkPassSQL =
          "SELECT COUNT(*) FROM nathanlamont.Pass WHERE passID = '"
              + passID
              + "' AND exprDate > SYSDATE";
      ResultSet checkPassRset = stmt.executeQuery(checkPassSQL);
      if (checkPassRset.next() && checkPassRset.getInt(1) == 0) {
        System.out.println("Cannot add rental. Pass ID does not exist or is expired.");
        return;
      }

      // Create a rental ID
      Random rand = new Random();
      int rentalID = rand.nextInt(1000000); // Generate a random rental ID
      boolean rentalIDExists = true;
      while (rentalIDExists) {
        String checkRentalIDSQL =
            "SELECT COUNT(*) FROM nathanlamont.Rental WHERE rentalID = " + rentalID;
        ResultSet checkRset = stmt.executeQuery(checkRentalIDSQL);
        if (checkRset.next() && checkRset.getInt(1) == 0) {
          rentalIDExists = false; // Rental ID is unique
        } else {
          rentalID = rand.nextInt(1000000); // Generate a new random rental ID
        }
      }

      // Insert the new rental record
      String sql =
          "INSERT INTO nathanlamont.Rental (rentalID, equipmentID, passID, rentalTime, returnStatus) "
              + "VALUES ("
              + rentalID
              + ", "
              + equipmentID
              + ", '"
              + passID
              + "', TO_TIMESTAMP('"
              + rentalTime
              + "', 'YYYY-MM-DD HH24:MI:SS.FF'), 0)";
      stmt.executeUpdate(sql);
      System.out.println("Rental record added successfully with ID: " + rentalID);
    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }
  }

  /*---------------------------------------------------------------------
  |  Method updateReturnStatus
  |
  |  Purpose: Updates the return status of a rental entry to 1 (returned).
  |
  |  Pre-condition:
  |     - `dbconn` must be a valid and open JDBC connection.
  |     - `equipmentID` must refer to an existing equipment record.
  |
  |  Post-condition:
  |     - The return status for the specified rental is set to 1.
  |
  |  Parameters:
  |     dbconn      -- Active JDBC connection.
  |     EquipmentID -- Equipment ID to be updated.
  |
  |  Returns: None.
  *-------------------------------------------------------------------*/
  public static void updateReturnStatus(Connection dbconn, int equipmentID) {
    try (Statement stmt = dbconn.createStatement()) {
      // Check if the rental ID exists and is not already returned
      String sql =
          "UPDATE nathanlamont.Rental SET returnStatus = 1 "
              + "WHERE equipmentID = "
              + equipmentID
              + " AND returnStatus = 0";
      int updated = stmt.executeUpdate(sql);
      // Check if the update was successful
      if (updated > 0) {

        // Log update
        String logSQL =
            "INSERT INTO nathanlamont.Updates (updateType, tableChanged, changeID, dateTime) VALUES ("
                + "'update', 'Rental', '"
                + equipmentID
                + "', SYSDATE)";
        stmt.executeUpdate(logSQL);

        System.out.println("Item is returned successfully.");
      } else {
        System.out.println("Equipment ID not found or already returned.");
      }
    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }
  }

  /*---------------------------------------------------------------------
  |  Method deleteRental
  |
  |  Purpose: Deletes a rental record from the Rental table, but only if
  |           the return status is still 0 (not returned). This allows
  |           deletion of erroneous entries while preserving history of use.
  |
  |  Pre-condition:
  |     - `dbconn` must be valid and open.
  |     - The rental record must exist and have returnStatus = 0.
  |
  |  Post-condition:
  |     - The rental entry is deleted if not yet returned.
  |
  |  Parameters:
  |     dbconn   -- Active JDBC connection.
  |     rentalID -- ID of the rental to delete.
  |
  |  Returns: None.
  *-------------------------------------------------------------------*/
  public static void deleteRental(Connection dbconn, int rentalID) {
    try (Statement stmt = dbconn.createStatement()) {
      // Only delete if the returnStatus = 0
      String checkSQL = "SELECT returnStatus FROM nathanlamont.Rental WHERE rentalID = " + rentalID;
      ResultSet rset = stmt.executeQuery(checkSQL);

      // Check if the rental ID exists and is not already returned
      if (rset.next() && rset.getInt("returnStatus") == 0) {
        String sql = "DELETE FROM nathanlamont.Rental WHERE rentalID = " + rentalID;
        stmt.executeUpdate(sql);

        // Log deletion
        String logSQL =
            "INSERT INTO nathanlamont.Updates (updateType, tableChanged, changeID, dateTime) VALUES ("
                + "'delete', 'Rental', '"
                + rentalID
                + "', SYSDATE)";
        stmt.executeUpdate(logSQL);

        System.out.println("Rental record deleted successfully.");
      } else {
        // Rental ID does not exist or is already returned
        System.out.println("Cannot delete. Rental is already returned or does not exist.");
      }
    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }
  }
}
