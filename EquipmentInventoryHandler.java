import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*+----------------------------------------------------------------------
||  Class EquipmentInventoryHandler
||
||         Author:  Group 14 – Connor, Luis, Mohammad, Nathan
||
||        Purpose:  This class provides static methods to manage equipment
||                  inventory records. This includes displaying all entries,
||                  adding new equipment, updating existing entries, and
||                  archiving old equipment.
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
||   Constructors:  None (static utility class)
||
||  Class Methods:
||       void displayAllEquipment(Connection dbconn)
||       void addEquipment(Connection dbconn, String type, String size, int qty)
||       void updateEquipment(Connection dbconn, int id, String type, String size, int qty)
||       void archiveEquipment(Connection dbconn, int id)
||
++-----------------------------------------------------------------------*/
public class EquipmentInventoryHandler {

  /*---------------------------------------------------------------------
  |  Method displaySummary
  |
  |  Purpose: Displays a summary of all active (non-archived) equipment
  |      in the Equipment table. The summary includes equipment ID, type,
  |      size, and available quantity.
  |
  |  Pre-condition:
  |      - Connection `dbconn` must be valid and open.
  |      - Equipment table must exist and follow schema with an `archived`
  |        column (1 = archived, 0 = active).
  |
  |  Post-condition:
  |      - Equipment summary is printed to the console.
  |      - Archived equipment is excluded from the results.
  |
  |  Parameters:
  |      dbconn -- (IN) active JDBC connection used to execute SQL queries.
  |
  |  Returns: None
  *-------------------------------------------------------------------*/
  public static void displaySummary(Connection dbconn) {
    String query =
        "SELECT equipmentID, type, size, availableQty "
            + "FROM group14.Equipment "
            + "WHERE archived = 0 "
            + "ORDER BY equipmentID";

    // Execute the query and process the results
    try (Statement stmt = dbconn.createStatement();
        ResultSet rset = stmt.executeQuery(query)) {

      // Print the header
      System.out.println("\n--- Equipment Inventory ---");
      System.out.printf("%-12s %-12s %-12s %-15s\n", "ID", "Type", "Size", "Available Qty");

      while (rset.next()) {
        // Retrieve data from the result set
        int id = rset.getInt("equipmentID");
        String type = rset.getString("type");
        String size = rset.getString("size");
        int qty = rset.getInt("availableQty");

        // Print each equipment entry
        System.out.printf("%-12d %-12s %-12s %-15d\n", id, type, size, qty);
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
  |  Purpose: Adds a new equipment record to the Equipment table using
  |      the provided type, size, and quantity. The new item is marked
  |      as active (archived = 0) and a unique ID is auto-generated.
  |
  |  Pre-condition:
  |      - Connection `dbconn` must be valid and open.
  |      - Parameters `type`, `size` must be valid strings.
  |      - Parameter `qty` must be a non-negative integer.
  |
  |  Post-condition:
  |      - A new row is inserted into the Equipment table.
  |
  |  Parameters:
  |      dbconn -- (IN) active JDBC connection.
  |      type   -- (IN) type/category of the equipment.
  |      size   -- (IN) size of the equipment.
  |      qty    -- (IN) initial quantity available.
  |
  |  Returns: None
  *-------------------------------------------------------------------*/

  public static void addEquipment(Connection dbconn, String type, String size, int qty) {
    try (Statement stmt = dbconn.createStatement()) {
      String sql =
          "INSERT INTO group14.Equipment (equipmentID, type, size, availableQty, archived) "
              + "VALUES (group14.equipment_seq.NEXTVAL, '"
              + type
              + "', '"
              + size
              + "', "
              + qty
              + ", 0)";
      stmt.executeUpdate(sql);
      System.out.println("Equipment added successfully.");
    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }
  }

  /*---------------------------------------------------------------------
  |  Method updateEquipment
  |
  |  Purpose: Updates an existing equipment entry's type, size, and quantity,
  |      provided that the item is currently active (not archived).
  |
  |  Pre-condition:
  |      - Connection `dbconn` must be valid and open.
  |      - `id` must refer to a valid, active equipment item.
  |      - `type` and `size` are valid strings, `qty` is non-negative.
  |
  |  Post-condition:
  |      - If valid, the equipment record is updated in-place.
  |
  |  Parameters:
  |      dbconn -- (IN) active JDBC connection.
  |      id     -- (IN) ID of the equipment to update.
  |      type   -- (IN) new type/category.
  |      size   -- (IN) new size.
  |      qty    -- (IN) new available quantity.
  |
  |  Returns: None
  *-------------------------------------------------------------------*/

  public static void updateEquipment(Connection dbconn, int id, String type, String size, int qty) {
    try (Statement stmt = dbconn.createStatement()) {
      String sql =
          "UPDATE group14.Equipment SET type = '"
              + type
              + "', size = '"
              + size
              + "', availableQty = "
              + qty
              + " WHERE equipmentID = "
              + id
              + " AND archived = 0";
      int updated = stmt.executeUpdate(sql);
      if (updated > 0) {
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
  |  Purpose: Archives an equipment item (sets archived = 1), provided that
  |      it is not currently rented out. This preserves history without deletion.
  |
  |  Pre-condition:
  |      - `dbconn` must be a valid JDBC connection.
  |      - Equipment with given `id` must exist and not be currently rented
  |        (i.e., has no active Rental entry with returnStatus = 0).
  |
  |  Post-condition:
  |      - The equipment is marked as archived (if allowed).
  |
  |  Parameters:
  |      dbconn -- (IN) active JDBC connection.
  |      id     -- (IN) equipment ID to archive.
  |
  |  Returns: None
  *-------------------------------------------------------------------*/

  public static void archiveEquipment(Connection dbconn, int id) {
    try (Statement stmt = dbconn.createStatement()) {
      // Check if equipment is currently rented
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
        System.out.println("Equipment archived successfully.");
      } else {
        System.out.println("No equipment found with the given ID.");
      }
    } catch (SQLException e) {
      System.err.println("SQL Error: " + e.getMessage());
    }
  }
}
