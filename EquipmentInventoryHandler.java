import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

/*+----------------------------------------------------------------------
||  Class EquipmentInventoryHandler
||
||         Author:  Group 14 – Connor, Luis, Mohammad, Nathan
||
||        Purpose:  This static utility class manages the ski resort's
||                  equipment inventory. It provides functions for:
||                    - displaying all active (non-archived) equipment,
||                    - adding new equipment entries,
||                    - updating existing entries,
||                    - and archiving old or retired equipment items.
||
||                  Equipment is uniquely identified by a randomly
||                  generated equipmentID. Instead of deletion,
||                  equipment is marked as archived to preserve history.
||
||  Inherits From:  None.
||
||     Interfaces:  None.
||
||   Constructors:  None (all methods are static).
||
||  Class Methods:
||       void displayAllEquipment(Connection dbconn)
||       void addEquipment(Connection dbconn, String type, String size)
||       void updateEquipment(Connection dbconn, int id, String type, String size)
||       void archiveEquipment(Connection dbconn, int id)
||
++-----------------------------------------------------------------------*/
public class EquipmentInventoryHandler {

  /*---------------------------------------------------------------------
  |  Method displaySummary
  |
  |  Purpose: Displays all non-archived (active) equipment records
  |           in a formatted table, showing their ID, type, and size.
  |
  |  Pre-condition:
  |     - Connection `dbconn` must be open and valid.
  |     - The `group14.Equipment` table must exist.
  |
  |  Post-condition:
  |     - Active equipment entries are printed to standard output.
  |     - Archived equipment entries are excluded.
  |
  |  Parameters:
  |     dbconn -- A valid JDBC connection to the database.
  |
  |  Returns: None.
  *-------------------------------------------------------------------*/

  public static void displaySummary(Connection dbconn) {
    String query =
        "SELECT equipmentID, type, size "
            + "FROM group14.Equipment "
            + "WHERE archived = 0 "
            + "ORDER BY equipmentID";

    try (Statement stmt = dbconn.createStatement();
        ResultSet rset = stmt.executeQuery(query)) {

      System.out.println("\n--- Equipment Inventory ---");
      System.out.printf("%-12s %-12s %-12s\n", "ID", "Type", "Size");

      while (rset.next()) {
        int id = rset.getInt("equipmentID");
        String type = rset.getString("type");
        String size = rset.getString("size");

        System.out.printf("%-12d %-12s %-12s\n", id, type, size);
      }
    } catch (SQLException e) {
      System.err.println("*** SQLException: " + e.getMessage());
      System.err.println("\tSQLState:  " + e.getSQLState());
      System.err.println("\tErrorCode: " + e.getErrorCode());
    }
  }

  /*---------------------------------------------------------------------
  |  Method addEquipment
  |
  |  Purpose: Inserts a new piece of equipment into the Equipment table
  |           with a unique randomly generated equipment ID. The item is
  |           initially marked as active (not archived).
  |
  |  Pre-condition:
  |     - `dbconn` must be a valid, open JDBC connection.
  |     - `type` and `size` must be non-null and non-empty strings.
  |
  |  Post-condition:
  |     - A new row is inserted into `group14.Equipment` with archived = 0.
  |
  |  Parameters:
  |     dbconn -- Active JDBC connection.
  |     type   -- Type/category of the equipment (e.g., skis, boots).
  |     size   -- Size descriptor (e.g., Small, 150cm, etc.).
  |
  |  Returns: None.
  *-------------------------------------------------------------------*/
  public static void addEquipment(Connection dbconn, String type, String size) {
    Random rand = new Random();
    int newEquipmentID = 0;
    boolean idIsUnique = false;

    try (Statement stmt = dbconn.createStatement()) {
      while (!idIsUnique) {
        newEquipmentID = 10000 + rand.nextInt(90000);
        String checkSQL =
            "SELECT COUNT(*) FROM group14.Equipment WHERE equipmentID = " + newEquipmentID;
        try (ResultSet rset = stmt.executeQuery(checkSQL)) {
          if (rset.next() && rset.getInt(1) == 0) {
            idIsUnique = true;
          }
        }
      }

      String insertSQL =
          "INSERT INTO group14.Equipment (equipmentID, type, size, archived) "
              + "VALUES ("
              + newEquipmentID
              + ", '"
              + type
              + "', '"
              + size
              + "', 0)";
      stmt.executeUpdate(insertSQL);
      System.out.println("Equipment added successfully with ID: " + newEquipmentID);
    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }
  }

  /*---------------------------------------------------------------------
  |  Method updateEquipment
  |
  |  Purpose: Updates the type and size of an existing, non-archived
  |           equipment entry identified by its ID.
  |
  |  Pre-condition:
  |     - `dbconn` must be a valid JDBC connection.
  |     - The equipment ID must refer to an existing, non-archived item.
  |
  |  Post-condition:
  |     - The corresponding entry is updated if it is found and active.
  |
  |  Parameters:
  |     dbconn -- Active JDBC connection.
  |     id     -- ID of the equipment to update.
  |     type   -- New type of the equipment.
  |     size   -- New size of the equipment.
  |
  |  Returns: None.
  *-------------------------------------------------------------------*/
  public static void updateEquipment(Connection dbconn, int id, String type, String size) {
    try (Statement stmt = dbconn.createStatement()) {
      String sql =
          "UPDATE group14.Equipment SET type = '"
              + type
              + "', size = '"
              + size
              + "' "
              + "WHERE equipmentID = "
              + id
              + " AND archived = 0";
      int updated = stmt.executeUpdate(sql);
      if (updated > 0) {
        // Log the update to Updates table
        String logSQL =
            "INSERT INTO group14.Updates (updateType, tableChanged, changeID, dateTime) VALUES ("
                + "'update', 'Equipment', '"
                + id
                + "', SYSDATE)";
        stmt.executeUpdate(logSQL);

        // print the update
        System.out.println("Equipment updated successfully.");
      } else {
        System.out.println("No active equipment found with the given ID.");
      }
    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }
  }

  /*---------------------------------------------------------------------
  |  Method archiveEquipment
  |
  |  Purpose: Flags an equipment record as archived (archived = 1)
  |           if it is not currently rented out (i.e., no active rental).
  |
  |  Pre-condition:
  |     - `dbconn` must be valid and open.
  |     - Equipment must not be actively rented (checked via Rental table).
  |
  |  Post-condition:
  |     - The specified equipment entry is marked as archived if eligible.
  |
  |  Parameters:
  |     dbconn -- Active JDBC connection.
  |     id     -- Equipment ID to archive.
  |
  |  Returns: None.
  *-------------------------------------------------------------------*/

  public static void archiveEquipment(Connection dbconn, int id) {
    try (Statement stmt = dbconn.createStatement()) {
      String checkSql =
          "SELECT COUNT(*) FROM group14.Rental WHERE equipmentID = " + id + " AND returnStatus = 0";
      try (ResultSet rset = stmt.executeQuery(checkSql)) {
        if (rset.next() && rset.getInt(1) > 0) {
          System.out.println("Cannot archive. Equipment is currently rented out.");
          return;
        }
      }

      String sql = "UPDATE group14.Equipment SET archived = 1 WHERE equipmentID = " + id;
      int updated = stmt.executeUpdate(sql);
      if (updated > 0) {
        // Log the archival (treated as update)
        String logSQL =
            "INSERT INTO group14.Updates (updateType, tableChanged, changeID, dateTime) VALUES ("
                + "'update', 'Equipment', '"
                + id
                + "', SYSDATE)";
        stmt.executeUpdate(logSQL);

        // print the update
        System.out.println("Equipment archived successfully.");
      } else {
        System.out.println("No equipment found with the given ID.");
      }
    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }
  }
}
