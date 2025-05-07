import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/*+----------------------------------------------------------------------
||  Class MemberHandler
||
||         Author:  Group 14 – Connor, Luis, Mohammad, Nathan
||
||        Purpose:  Provides static methods to manage ski passes
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
||       void addPass(Connection dbconn)
||
||       void updatePass(Connection dbconn)
||
||       void deletePass(Connection dbconn)
++-----------------------------------------------------------------------*/
public class SkiPassHandler {


  /*---------------------------------------------------------------------
  |  Method addPass
  |
  |  Purpose: Adds a new ski pass and assigns a new pass ID based on how many there
  |           currently are.
  |
  |  Pre-condition:
  |     - `dbconn` must be valid and open.
  |
  |  Post-condition:
  |     - A new record is inserted into the `Pass` table.
  |
  |  Parameters:
  |     dbconn              -- Active JDBC connection.
  |
  |
  |  Returns: None.
  *-------------------------------------------------------------------*/
	public static void addPass(Connection dbconn) {
		String memID;
		int numUses;
		String passType;
		double price;
		String expDate;
		int curr = 0;
		String query1 = "SELECT COUNT(*) AS count FROM nathanlamont.Pass";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate today = LocalDate.now();
		
		try (Scanner sc1 = new Scanner(System.in)) {
			System.out.println("Enter Member ID of new Pass: ");
			memID = sc1.nextLine();
			System.out.println("Enter the type of pass: ");
			passType = sc1.nextLine();
			sc1.close();
		}
		switch (passType) {
			case "Season" ->                     {
					price = 499.99;
					numUses = 100;
					expDate = today.plusDays(60).format(formatter);
				}
			case "Weekend" ->                     {
					price = 79.99;
					numUses = 30;
					expDate = today.plusDays(4).format(formatter);
				}
			case "Day" ->                     {
					price = 49.99;
					numUses = 10;
					expDate = today.plusDays(1).format(formatter);
				}
			default -> {
				System.out.println("Incorrect Pass Type. Try Again.");
				return;
			}
		}
		
		try (Statement stmt = dbconn.createStatement()) {
			ResultSet rset = stmt.executeQuery(query1);
			while (rset.next()) {
		        curr = rset.getInt("count") + 1;
		      }
		} catch (SQLException ex) {
			System.out.println("Error: Could not retrieve size.");
			return;
            }
		String passID = "P" + curr;
		String query2 = "Insert Into nathanlamont.Pass (passID, memberID, numUses, passType, price, exprDATE) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement query = dbconn.prepareStatement(query2)) {
                query.setString(1, passID);
                query.setString(2, memID);
                query.setInt(3, numUses);
                query.setString(4, passType);
                query.setDouble(5, price);
                query.setDate(6, Date.valueOf(expDate));
                query.executeUpdate();
				System.out.println("Successfully entered new Ski Pass for user " + passID);
		} catch (SQLException ex) {
			System.out.println("Error: Could not complete query.");
            }
	}


  /*---------------------------------------------------------------------
  |  Method updatePass
  |
  |  Purpose: Changes the value of the remaining uses of a ski pass by a specified amount.
  |
  |  Pre-condition:
  |     - `dbconn` must be valid and open.
  |
  |  Post-condition:
  |     - A record's numUses attributed is updated in the 'Pass' table.
  |
  |  Parameters:
  |     dbconn              -- Active JDBC connection.
  |
  |  Returns: None.
  *-------------------------------------------------------------------*/
	public static void updatePass(Connection dbconn) {
		String passID;
		int curval = 0;
        int val;
		try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter PassID: ");
            passID = sc.nextLine();
            String query = "Select numUses from nathanlamont.Pass Where passID = ?";
            try (PreparedStatement query2 = dbconn.prepareStatement(query)) {
                query2.setString(1, passID);
                ResultSet rset = query2.executeQuery();
					if (rset.next()) {
						curval = rset.getInt("numUses");
					}
				} catch (SQLException ex) {
					System.out.println("Error: Could not connect to database");
				}
				System.out.println("\nCurrent Remaining Uses for Pass " + passID + ": " + curval);
				System.out.print("\nIncrease Remaining Uses by: ");
			val = 0;
			try {
				val = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Error: Invalid number.");
				return;
			}
		}
		if (val + curval >= 0) {
			String query3 = "Update nathanlamont.Pass Set numUses = numUses + ? Where passID = ?";
			try (PreparedStatement update = dbconn.prepareStatement(query3)) {
				update(dbconn, "update", passID);
                update.setInt(1, val);
                update.setString(2, passID);
				update.executeUpdate();
				System.out.println("Successfully changed remaining uses by " + val);
			} catch (SQLException ex) {
				System.out.println("Error: Could not connect to database");
            }
		}
		else {
			System.out.println("Error: Invalid value. Try again.");
		}
	}

  /*---------------------------------------------------------------------
  |  Method deletePass
  |
  |  Purpose: Removes a ski pass from the 'Pass' table if it has expired or has no more uses.
  |
  |  Pre-condition:
  |     - `dbconn` must be valid and open.
  |
  |  Post-condition:
  |     - A record is deleted from the Pass table.
  |
  |  Parameters:
  |     dbconn              -- Active JDBC connection.
  |
  |
  |  Returns: None.
  *-------------------------------------------------------------------*/
	public static void deletePass(Connection dbconn) {
		String passID;
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter PassID: ");
		passID = sc.nextLine();
		int curval = 0;
		Date expDate = null;
		LocalDate today = LocalDate.now();
		java.sql.Date today2 = java.sql.Date.valueOf(today);
		try (PreparedStatement query = dbconn.prepareStatement("Select numUses, exprDate from nathanlamont.Pass Where passID = ?")) {
                query.setString(1, passID);
                ResultSet rset = query.executeQuery();
				if (rset.next()) {
					curval = rset.getInt("numUses");
					expDate = rset.getDate("exprDate");
				}
		} catch (SQLException ex) {
			System.out.println("SQL Error: " + ex.getMessage());
            }
		boolean expired = expDate.compareTo(today2) < 0;
		if (expired || curval == 0) {
			try (PreparedStatement query2 = dbconn.prepareStatement("Delete From nathanlamont.Rental Where passID = ?")) {
					query2.setString(1, passID);
					query2.executeUpdate();
			} catch (SQLException ex) {
				System.out.println("SQL Error: " + ex.getMessage());
				}
			try (PreparedStatement query2 = dbconn.prepareStatement("Delete From nathanlamont.LiftLog Where passID = ?")) {
					query2.setString(1, passID);
					query2.executeUpdate();
			} catch (SQLException ex) {
				System.out.println("SQL Error: " + ex.getMessage());
				}
			try (PreparedStatement query2 = dbconn.prepareStatement("Delete From nathanlamont.Pass Where passID = ?")) {
					update(dbconn, "delete", passID);
					query2.setString(1, passID);
					query2.executeUpdate();
					System.out.println("Successfully deleted Ski Pass " + passID);
			} catch (SQLException ex) {
				System.out.println("SQL Error: " + ex.getLocalizedMessage());
				}
		}
		else {
			System.out.println("Error: Only Expired or Useless passes can be deleted.");
		}
	}

  /*---------------------------------------------------------------------
  |  Method update
  |
  |  Purpose: Logs any updates or deletions to the 'Updates' table.
  |
  |  Pre-condition:
  |     - `dbconn` must be valid and open.
  |		- Parameters must be valid.
  |
  |  Post-condition:
  |     - A record is entered into the 'Updates' table.
  |
  |  Parameters:
  |     dbconn              -- Active JDBC connection.
  |		edit				-- The type of edit that is being done on the 'Pass' table.
  |		pk					-- The primary key of the pass being updated or deleted.
  |
  |  Returns: None.
  *-------------------------------------------------------------------*/
	public static void update(Connection dbconn, String edit, String pk) {
		LocalDate today = LocalDate.now();
		try (PreparedStatement query = dbconn.prepareStatement("Insert Into nathanlamont.Updates (updateType, tableChanged, changeID, dateTime) Values (?, ?, ?, ?)")) {
                query.setString(1, edit);
				query.setString(2, "Pass");
				query.setString(3, pk);
				query.setDate(4, java.sql.Date.valueOf(today));
                query.executeUpdate();
		} catch (SQLException ex) {
			System.out.println("SQL Update Error: " + ex.getMessage());
            }
	}
}
