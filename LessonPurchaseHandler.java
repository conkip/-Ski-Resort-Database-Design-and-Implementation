import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

/*+----------------------------------------------------------------------
||  Class LessonPurchaseHandler
||
||         Author:  Group 14 – Connor, Luis, Mohammad, Nathan
||
||        Purpose:  Provides static methods to manage lesson purchase
||                  records in the ski resort system. Supports adding,
||                  updating (sessions remaining), displaying, and
||                  deleting lesson purchases.
||
||  Inherits From:  None
||
||     Interfaces:  None
||
||   Constructors:  None (static utility class)
||
||  Class Methods:
||       void displayAllPurchases(Connection dbconn)
||       void addPurchase(Connection dbconn, int orderID, int memberID, int lessonID,
||                        int sessionsPurchased, double price)
||       void updateRemainingSessions(Connection dbconn, int orderID, int newRemaining)
||       void deletePurchase(Connection dbconn, int orderID)
||       void recordSessionUsage(Connection dbconn, int orderID)
++-----------------------------------------------------------------------*/
public class LessonPurchaseHandler {

  /*---------------------------------------------------------------------
  |  Method displayAllPurchases
  |
  |  Purpose: Displays all lesson purchase records from the database
  |           including order ID, member ID, lesson ID, session counts,
  |           and price in a formatted table.
  |
  |  Pre-condition:
  |     - `dbconn` must be a valid and open JDBC connection.
  |
  |  Post-condition:
  |     - Outputs all rows from `LessonPurchase` to standard output.
  |
  |  Parameters:
  |     dbconn -- Active JDBC connection.
  |
  |  Returns: None.
  *-------------------------------------------------------------------*/

  public static void displayAllPurchases(Connection dbconn) {
    // SQL query to select all lesson purchases
    String query = "SELECT * FROM nathanlamont.LessonPurchase ORDER BY orderID";

    // Execute the query and display results
    try (Statement stmt = dbconn.createStatement();
        ResultSet rset = stmt.executeQuery(query)) {
      System.out.println("\n--- Lesson Purchases ---");
      System.out.printf(
          "%-10s %-10s %-10s %-20s %-20s %-10s\n",
          "OrderID", "MemberID", "LessonID", "Sessions Purchased", "Remaining Sessions", "Price");

      // Loop through the result set and print each record
      while (rset.next()) {
        int orderID = rset.getInt("orderID");
        int memberID = rset.getInt("memberID");
        int lessonID = rset.getInt("lessonID");
        int sessionsPurchased = rset.getInt("sessionsPurchased");
        int remainingSessions = rset.getInt("remainingSessions");
        double price = rset.getDouble("price");

        // Print the record in a formatted manner
        System.out.printf(
            "%-10d %-10d %-10d %-20d %-20d $%-10.2f\n",
            orderID, memberID, lessonID, sessionsPurchased, remainingSessions, price);
      }
    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }
  }

  /*---------------------------------------------------------------------
  |  Method addPurchase
  |
  |  Purpose: Adds a new lesson purchase entry with a randomly generated
  |           unique order ID. The number of remaining sessions is set
  |           equal to the number of sessions purchased.
  |
  |  Pre-condition:
  |     - `dbconn` must be valid and open.
  |     - Provided `memberID`, `lessonID`, `sessionsPurchased`, and `price` must be valid.
  |
  |  Post-condition:
  |     - A new record is inserted into the `LessonPurchase` table.
  |
  |  Parameters:
  |     dbconn            -- Active JDBC connection.
  |     memberID          -- ID of the purchasing member.
  |     lessonID          -- ID of the lesson purchased.
  |     sessionsPurchased -- Total number of sessions bought.
  |     price             -- Total price for the sessions.
  |
  |  Returns: None.
  *-------------------------------------------------------------------*/

  public static void addPurchase(
      Connection dbconn, int memberID, int lessonID, int sessionsPurchased, double price) {

    Random rand = new Random();
    int orderID = rand.nextInt(1000000); // Generate a random order ID
    boolean unique = false;

    // Ensure the orderID is unique
    while (!unique) {
      try (Statement stmt = dbconn.createStatement()) {
        String checkSQL = "SELECT orderID FROM nathanlamont.LessonPurchase WHERE orderID = " + orderID;
        ResultSet rset = stmt.executeQuery(checkSQL);
        if (!rset.next()) {
          unique = true; // Unique orderID found
        } else {
          orderID = rand.nextInt(1000000); // Generate a new random order ID
        }
      } catch (SQLException e) {
        System.err.println("SQL Error: " + e.getMessage());
      }
    }

    // Insert the new lesson purchase into the database
    try (Statement stmt = dbconn.createStatement()) {
      String sql =
          "INSERT INTO nathanlamont.LessonPurchase (orderID, memberID, lessonID, "
              + "sessionsPurchased, remainingSessions, price) VALUES ("
              + orderID
              + ", "
              + memberID
              + ", "
              + lessonID
              + ", "
              + sessionsPurchased
              + ", "
              + sessionsPurchased
              + ", "
              + price
              + ")";
      stmt.executeUpdate(sql);
      System.out.println("Lesson purchase added successfully with Order ID: " + orderID);
    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }
  }

  /*---------------------------------------------------------------------
  |  Method recordSessionUsage
  |
  |  Purpose: Reduces the remaining sessions of a lesson purchase by one,
  |           representing that one session has been used.
  |
  |  Pre-condition:
  |     - `dbconn` must be a valid, open JDBC connection.
  |     - `orderID` must exist in the LessonPurchase table.
  |     - remainingSessions must be greater than 0.
  |
  |  Post-condition:
  |     - remainingSessions is decremented by 1 if valid.
  |
  |  Parameters:
  |     dbconn -- Active JDBC connection.
  |     orderID -- The order ID of the lesson purchase to update.
  |
  |  Returns: None.
  *-------------------------------------------------------------------*/
  public static void recordSessionUsage(Connection dbconn, int orderID) {
    try (Statement stmt = dbconn.createStatement()) {
      // Check if the order ID exists and get the remaining sessions
      String checkSQL =
          "SELECT remainingSessions FROM nathanlamont.LessonPurchase WHERE orderID = " + orderID;
      ResultSet rset = stmt.executeQuery(checkSQL);

      // If the order ID exists, update the remaining sessions
      if (rset.next()) {
        int remaining = rset.getInt("remainingSessions");

        // Check if there are remaining sessions to use
        // If remaining sessions are greater than 0, decrement by 1
        if (remaining > 0) {
          String updateSQL =
              "UPDATE nathanlamont.LessonPurchase SET remainingSessions = "
                  + (remaining - 1)
                  + " WHERE orderID = "
                  + orderID;
          stmt.executeUpdate(updateSQL);
          // Print confirmation message
          System.out.println("Session usage recorded. Remaining sessions: " + (remaining - 1));
        } else {
          System.out.println("No remaining sessions left to use.");
        }
      } else {
        System.out.println("Order ID not found.");
      }

    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }
  }

  /*---------------------------------------------------------------------
  |  Method updateRemainingSessions
  |
  |  Purpose: Updates the number of remaining sessions for a specific
  |           lesson purchase.
  |
  |  Pre-condition:
  |     - `dbconn` must be valid and open.
  |     - The order ID must exist.
  |
  |  Post-condition:
  |     - remainingSessions is updated to the new value.
  |
  |  Parameters:
  |     dbconn       -- Active JDBC connection.
  |     orderID      -- ID of the lesson purchase to update.
  |     newRemaining -- New value for remaining sessions.
  |
  |  Returns: None.
  *-------------------------------------------------------------------*/

  public static void updateRemainingSessions(Connection dbconn, int orderID, int newRemaining) {
    try (Statement stmt = dbconn.createStatement()) {
      // Check if the order ID exists
      String sql =
          "UPDATE nathanlamont.LessonPurchase SET remainingSessions = "
              + newRemaining
              + " WHERE orderID = "
              + orderID;
      // Execute the update
      int updated = stmt.executeUpdate(sql);
      if (updated > 0) {
        // Log update
        String logSQL =
            "INSERT INTO nathanlamont.Updates (updateType, tableChanged, changeID, dateTime) VALUES ("
                + "'update', 'LessonPurchase', '"
                + orderID
                + "', SYSDATE)";
        stmt.executeUpdate(logSQL);
        // Print confirmation message
        System.out.println("Remaining sessions updated successfully.");
      } else {
        System.out.println("Order ID not found.");
      }
    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }
  }

  /*---------------------------------------------------------------------
  |  Method deletePurchase
  |
  |  Purpose: Deletes a lesson purchase record from the database, only if
  |           none of the sessions have been used (i.e., remaining = purchased).
  |
  |  Pre-condition:
  |     - `dbconn` must be valid and open.
  |     - The order ID must exist.
  |
  |  Post-condition:
  |     - Record is deleted only if all sessions are unused.
  |
  |  Parameters:
  |     dbconn  -- Active JDBC connection.
  |     orderID -- ID of the lesson purchase to delete.
  |
  |  Returns: None.
  *-------------------------------------------------------------------*/

  public static void deletePurchase(Connection dbconn, int orderID) {
    try (Statement stmt = dbconn.createStatement()) {
      // Check if the order ID exists and get the remaining sessions
      String checkSQL =
          "SELECT remainingSessions, sessionsPurchased FROM nathanlamont.LessonPurchase WHERE orderID = " + orderID;
      ResultSet rset = stmt.executeQuery(checkSQL);
      if (rset.next()) {
        // Get the number of remaining sessions
        // Check if remaining sessions are equal to purchased sessions
        // If so, delete the record

        int remaining = rset.getInt("remainingSessions");

        if (remaining == rset.getInt("sessionsPurchased")) {
          // Delete the lesson purchase record
          String deleteSQL = "DELETE FROM nathanlamont.LessonPurchase WHERE orderID = " + orderID;
          stmt.executeUpdate(deleteSQL);

          // Log delete
          String logSQL =
              "INSERT INTO nathanlamont.Updates (updateType, tableChanged, changeID, dateTime) VALUES ("
                  + "'delete', 'LessonPurchase', '"
                  + orderID
                  + "', SYSDATE)";
          stmt.executeUpdate(logSQL);

          // Print confirmation message
          System.out.println("Lesson purchase deleted successfully.");
        } else {
          // Print message if sessions have been used
          System.out.println("Cannot delete. Some sessions have already been used.");
        }
      } else {
        System.out.println("Order ID not found.");
      }
    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }
  }
}
