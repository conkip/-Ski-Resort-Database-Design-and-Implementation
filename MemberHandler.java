import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.Random;

/*+----------------------------------------------------------------------
||  Class MemberHandler
||
||         Author:  Group 14 – Connor, Luis, Mohammad, Nathan
||
||        Purpose:  Provides static methods to manage member records
||                  in the ski resort system. Supports adding,
||                  updating, and deleting members.
||
||  Inherits From:  None
||
||     Interfaces:  None
||
||   Constructors:  None (static utility class)
||
||  Class Methods:
||       void addMember(Connection dbconn, String name, String phoneNumber, String email,
||                      String dateBirth, String emergencyName, String emergencyPhone, 
||                      String emergencyEmail) {
||       void updateRemainingSessions(Connection dbconn, int orderID, int newRemaining)
||       void deletePurchase(Connection dbconn, int orderID)
||       void recordSessionUsage(Connection dbconn, int orderID)
++-----------------------------------------------------------------------*/
public class MemberHandler {

  /*---------------------------------------------------------------------
  |  Method addMember
  |
  |  Purpose: Adds a new member with a randomly generated unique member ID.
  |
  |  Pre-condition:
  |     - `dbconn` must be valid and open.
  |     - all paramater attributes must be valid.
  |
  |  Post-condition:
  |     - A new record is inserted into the `Member` table.
  |
  |  Parameters:
  |     dbconn              -- Active JDBC connection.
  |     name                -- Name of the member.
  |     phoneNumber         -- Phone number of the member.
  |     email               -- Email of the member
  |     dateBirth           -- Date of birth of the member
  |     emergencyName       -- Emergency contact name.
  |     emergencyPhone      -- Emergency contact phone.
  |     emergencyEmail      -- Emergency contact email.
  |
  |
  |  Returns: None.
  *-------------------------------------------------------------------*/
  public static void addMember(
      Connection dbconn, String name, String phoneNumber, String email, LocalDate dateBirth,
      String emergencyName, String emergencyPhone, String emergencyEmail) {

    Random rand = new Random();
    int memberID = rand.nextInt(1000000); // Generate a random order ID
    boolean unique = false;

    // Ensure the memberID is unique
    while (!unique) {
      try (Statement stmt = dbconn.createStatement()) {
        String checkSQL = "SELECT memberID FROM group14.Memeber WHERE memberID = " + memberID;
        ResultSet rset = stmt.executeQuery(checkSQL);
        if (!rset.next()) {
          unique = true; // Unique memberID found
        } else {
          memberID = rand.nextInt(1000000); // Generate a new random member ID
        }
      } catch (SQLException e) {
        System.err.println("SQL Error: " + e.getMessage());
      }
    }

    // Insert the new lesson purchase into the database
    try (Statement stmt = dbconn.createStatement()) {
      String sql =
          "INSERT INTO group14.Member VALUES ("
              + memberID
              + ", "
              + name
              + ", "
              + phoneNumber
              + ", "
              + email
              + ", "
              + "TO_DATE('" + dateBirth + "', 'YYYY-MM-DD')"
              + ", "
              + emergencyName
              + ", "
              + emergencyPhone
              + ", "
              + emergencyEmail
              + ")";

      stmt.executeUpdate(sql);
      System.out.println("Member added successfully with Member ID: " + memberID);
    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }
  }

  /*---------------------------------------------------------------------
  |  Method updateMember
  |
  |  Purpose: Updates the specified contact information for a member
  |
  |  Pre-condition:
  |     - `dbconn` must be valid and open.
  |     - The member ID must exist.
  |
  |  Post-condition:
  |     - the contact information is updated in the Member table.
  |
  |  Parameters:
  |     dbconn          -- Active JDBC connection.
  |     memberID        -- member ID to update
  |     phoneNumber     -- phone number to update, null otherwise
  |     email           -- email to update, null otherwise
  |     emergencyName   -- emergency contact name to update, null otherwise
  |     emergencyPhone  -- emergency contact phone number to update, null otherwise
  |     emergencyEmail  -- emergency contact email to update, null otherwise
  |
  |  Returns: None.
  *-------------------------------------------------------------------*/
  public static void updateMember(Connection dbconn, int memberID, String phoneNumber, String email,
                    String emergencyName, String emergencyPhone, String emergencyEmail) {
    try (Statement stmt = dbconn.createStatement()) {
      // Check if the order ID exists
      String sql = "";
      if(phoneNumber != null) {
        sql = 
            "UPDATE group14.Member SET phoneNumber = "
                + phoneNumber
                + " WHERE memberID = "
                + memberID;

      } else if (email != null) {
        sql = 
            "UPDATE group14.Member SET email = "
                + email
                + " WHERE memberID = "
                + memberID;

      } else { //if its the emergency contact being changed
        sql = 
            "UPDATE group14.Member SET emergencyName = "
                + emergencyName
                + ", emergencyPhone = "
                + emergencyPhone
                + ", emergencyEmail = "
                + emergencyEmail
                + " WHERE memberID = "
                + memberID;
      }

      // Execute the update
      int updated = stmt.executeUpdate(sql);
      if (updated > 0) {
        // Print confirmation message
        System.out.println("Member #" + memberID + "updated successfully.");
      } else {
        System.out.println("Member ID not found.");
      }
    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }
  }

  /*---------------------------------------------------------------------
  |  Method deleteMember
  |
  |  Purpose: Deletes a member if they do not hold any active ski passes, 
  |     open rental records, or unused lesson sessions.  If any of these exist,
  |     it will notify the user to either complete or cancel them.  
  |     Once all checks pass and deletion is confirmed, it will remove all 
  |     related records, including ski pass data, lift usage logs, rental history,
  |     and lesson transactions associated with the member.
  |
  |  Pre-condition:
  |     - `dbconn` must be valid and open.
  |     - The member ID must exist.
  |     - The member must not have any active ski passes, open rental records,
  |       or unused lesson sessions
  |
  |  Post-condition:
  |     - The member is deleted from the Member table, as well as all related
  |       records, including ski pass data, lift usage logs, rental history,
  |       and lesson transactions associated with the member.
  |
  |  Parameters:
  |     dbconn  -- Active JDBC connection.
  |     memberID -- ID of the member.
  |
  |  Returns: None.
  *-------------------------------------------------------------------*/
  public static void deleteMember(Connection dbconn, int memberID) {
    try (Statement stmt = dbconn.createStatement()) {
      // Check if the order ID exists and get the remaining sessions
      String checkSQL =
          "SELECT * FROM group14.Member WHERE memberID = " + memberID;
      ResultSet rset = stmt.executeQuery(checkSQL);
      if (!rset.next()) {
        System.out.println("Member ID not found.");
      }

      // Check for active passes
      checkSQL =
          "SELECT exprDATE FROM"
      ResultSet rset = stmt.executeQuery("SELECT expiration_date FROM your_table");

      LocalDate today = LocalDate.now();

      while (rset.next()) {
          // Get expiration_date from ResultSet as java.sql.Date and convert to LocalDate
          Date sqlDate = rset.getDate("expiration_date");
          if (sqlDate != null) {
              LocalDate expirationDate = sqlDate.toLocalDate();

              // Compare to today's date
              if (expirationDate.isBefore(today)) {
                  System.out.println("Expired on: " + expirationDate);
              } else {
                  System.out.println("Still valid until: " + expirationDate);
              }
          }
      }




      checkSQL =
          "SELECT * FROM group14.Pass WHERE memberID = " + memberID ;
      // Get the number of remaining sessions
      // Check if remaining sessions are equal to purchased sessions
      // If so, delete the record
      int remaining = rset.getInt("remainingSessions");
      if (remaining == rset.getInt("sessionsPurchased")) {
        String deleteSQL = "DELETE FROM group14.LessonPurchase WHERE orderID = " + orderID;
        stmt.executeUpdate(deleteSQL);
        System.out.println("Lesson purchase deleted successfully.");
      } else {
        // Print message if sessions have been used
        System.out.println("Cannot delete. Some sessions have already been used.");
      }
    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }
  }
}
