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
||                      String emergencyEmail)
||
||       void updateMember(Connection dbconn, int memberID, String phoneNumber, String email,
||                      String emergencyName, String emergencyPhone, String emergencyEmail)
||
||       void deleteMember(Connection dbconn, int memberID)
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
        String checkSQL = "SELECT memberID FROM nathanlamont.Member WHERE memberID = " + memberID;
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
          "INSERT INTO nathanlamont.Member VALUES ("
              + memberID
              + ", '"
              + name
              + "', '"
              + phoneNumber
              + "', '"
              + email
              + "', "
              + "TO_DATE('" + dateBirth + "', 'YYYY-MM-DD')"
              + ", '"
              + emergencyName
              + "', '"
              + emergencyPhone
              + "', '"
              + emergencyEmail
              + "')";

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
            "UPDATE nathanlamont.Member SET phoneNumber = '"
                + phoneNumber + "'"
                + " WHERE memberID = "
                + memberID;

      } else if (email != null) {
        sql = 
            "UPDATE nathanlamont.Member SET email = '"
                + email + "'"
                + " WHERE memberID = "
                + memberID;

      } else { //if its the emergency contact being changed
        sql = 
            "UPDATE nathanlamont.Member SET emergencyName = '"
                + emergencyName + "'"
                + ", emergencyPhone = '"
                + emergencyPhone + "'"
                + ", emergencyEmail = '"
                + emergencyEmail + "'"
                + " WHERE memberID = "
                + memberID;
      }

      // Execute the update
      int updated = stmt.executeUpdate(sql);
      if (updated > 0) {
        // Print confirmation message
        System.out.println("Member #" + memberID + "updated successfully.");
        
        // Log update
        String logSQL =
            "INSERT INTO nathanlamont.Updates (updateType, tableChanged, changeID, dateTime) VALUES ("
                + "'update', 'Member', " + memberID + ", SYSDATE)";
        stmt.executeUpdate(logSQL);

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
    String checkSQL = "";
    try (Statement stmt = dbconn.createStatement()) {
      // Check if the order ID exists and get the remaining sessions
      checkSQL =
          "SELECT * FROM nathanlamont.Member WHERE memberID = " + memberID;
      ResultSet rset = stmt.executeQuery(checkSQL);
      if (!rset.next()) {
        System.out.println("Member ID not found.");
        return;
      }
    }
    catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }
      
    try (Statement stmt = dbconn.createStatement()) {
      // Check for active passes (compare expiration date to current date)
      checkSQL =
          "SELECT exprDate FROM nathanlamont.Pass WHERE memberID = " + memberID;
      ResultSet rset = stmt.executeQuery(checkSQL);

      LocalDate curDate = LocalDate.now();

      while (rset.next()) {
          Date sqlDate = rset.getDate("exprDate");
          LocalDate exprDate = sqlDate.toLocalDate();

          if (!exprDate.isBefore(curDate)) {
            String message = "Member #" + memberID + " still has an active pass."
                          + " Please wait for the pass to expire or cancel it"
                          + " before deleting the membership.";
              System.out.println(message);
              return;
          }
      }
    }
    catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }

    try(Statement stmt = dbconn.createStatement()){

      // Check for open rental records
      checkSQL =
          "SELECT r.returnStatus \n"
            + "FROM nathanlamont.Pass p \n"
            + "JOIN nathanlamont.Rental r ON p.passID = r.passID \n"
            + "WHERE p.memberID = " + memberID;

      ResultSet rset = stmt.executeQuery(checkSQL);

      while (rset.next()) {
          int returnStatus = rset.getInt("returnStatus");
          if(returnStatus == 0) {
            String message = "Member #" + memberID + " has open rental records."
                          + " Please return all equipment before deleting the membership.";
            System.out.println(message);
            return;
          }
      }
    }
    catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }

    try(Statement stmt = dbconn.createStatement()){
      // Check for unused lesson sessions
      checkSQL = 
            "SELECT remainingSessions \n"
              + "FROM nathanlamont.LessonPurchase \n"
              + "WHERE memberID = " + memberID;
      
      ResultSet rset = stmt.executeQuery(checkSQL);

      while (rset.next()) {
          int remainingSessions = rset.getInt("remainingSessions");
          if(remainingSessions > 0) {
            String message = "Member #" + memberID + " has remaining lesson sessions left."
                          + " Please finish or cancel all lessons before deleting the membership.";
            System.out.println(message);
            return;
          }
      }
    }
    catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }

    String deleteSQL ="";

    try(Statement stmt = dbconn.createStatement()) {

      // delete ski pass data, lift logs, and rental history

      // delete all stuff connected to the passes
      checkSQL =
          "SELECT passID \n"
            + "FROM nathanlamont.Pass p \n"
            + "WHERE p.memberID = " + memberID;

      ResultSet rset = stmt.executeQuery(checkSQL);

      while (rset.next()) {
          String passID = rset.getString("passID");
          
          try(Statement stmt2 = dbconn.createStatement()){
            // delete rental history
            deleteSQL = "DELETE FROM nathanlamont.Rental WHERE passID = '" + passID + "'";
            stmt2.executeUpdate(deleteSQL);
          } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
          }

          try(Statement stmt3 = dbconn.createStatement()) {
            // delete lift logs
            deleteSQL = "DELETE FROM nathanlamont.LiftLog WHERE passID = '" + passID + "'";
            stmt3.executeUpdate(deleteSQL);
          } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
          }
      }
    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }

    try(Statement stmt = dbconn.createStatement()) {
      // delete the pass itslef
      deleteSQL = "DELETE FROM nathanlamont.Pass WHERE memberID = " + memberID;
      stmt.executeUpdate(deleteSQL);
    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }

    try(Statement stmt = dbconn.createStatement()) {
      // delete lesson transactions
      checkSQL =
          "SELECT orderID \n"
            + "FROM nathanlamont.LessonPurchase lp\n"
            + "WHERE lp.memberID = " + memberID;

      ResultSet rset = stmt.executeQuery(checkSQL);

      while (rset.next()) {
          int orderID = rset.getInt("orderID");
          
          try(Statement stmt2 = dbconn.createStatement()){
            // delete lesson logs
            deleteSQL = "DELETE FROM nathanlamont.LessonLog WHERE orderID = " + orderID;
            stmt2.executeUpdate(deleteSQL);
          } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
          }
      }
    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }

    try (Statement stmt = dbconn.createStatement()) {
      // delete the lesson purchase
      deleteSQL = "DELETE FROM nathanlamont.LessonPurchase WHERE memberID = " + memberID;
      stmt.executeUpdate(deleteSQL);
    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }

    try(Statement stmt = dbconn.createStatement()){
      //finally delete the member
      deleteSQL = "DELETE FROM nathanlamont.Member WHERE memberID = " + memberID;
      stmt.executeUpdate(deleteSQL);
    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }

      System.out.println("Member and all related records deleted successfully.");

    try(Statement stmt = dbconn.createStatement()) {
      // Log update
      String logSQL =
          "INSERT INTO nathanlamont.Updates (updateType, tableChanged, changeID, dateTime) VALUES ("
              + "'delete', 'Member', " + memberID + ", SYSDATE)";
      stmt.executeUpdate(logSQL);

    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }
  }
}
