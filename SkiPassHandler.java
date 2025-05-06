import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class SkiPassHandler {

	public static void addPass(Connection dbconn) {
		String memID;
		int numUses;
		String passType;
		double price;
		String expDate;
		int curr = 0;
		String query1 = "SELECT COUNT(*) AS count FROM group14.Pass;";
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
			System.out.println("Error: Could not connect to database");
			return;
            }
		String passID = "P" + curr;
		String query2 = "Insert Into group14.Pass (passID, memberID, numUses, passType, price, exprDATE) VALUES (?, ?, ?, ?, ?, ?)";
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
			System.out.println("Error: Could not connect to database");
            }
	}

	public static void updatePass(Connection dbconn) {
		String passID;
		int curval = 0;
                int val;
		try (Scanner sc = new Scanner(System.in)) {
                    System.out.print("Enter Member ID of Pass: ");
                    passID = sc.nextLine();
                    String query = "Select numUses from group14.Pass Where passID = ?";
                    try (PreparedStatement query2 = dbconn.prepareStatement(query)) {
                        query2.setString(1, passID);
                        ResultSet rset = query2.executeQuery();
                        if (rset.next()) {
                            curval = rset.getInt("numUses");
                        }
                    } catch (SQLException ex) {
                        System.out.println("Error: Could not connect to database");
                    }
                    System.out.println("Current Remaining Uses for Pass " + passID + ": " + curval);
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
			String query3 = "Update group14.Pass Set numUses = numUses + ? Where passID = ?";
			try (PreparedStatement update = dbconn.prepareStatement(query3)) {
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

	public static void deletePass(Connection dbconn) {
		String passID;
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter Member ID of Pass: ");
		passID = sc.nextLine();
		int curval = 0;
		Date expDate = null;
		LocalDate today = LocalDate.now();
		Date today2 = (Date) Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
		try (PreparedStatement query = dbconn.prepareStatement("Select numUses, exprDate from group14.Pass Where passID = ?")) {
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
			try (PreparedStatement query2 = dbconn.prepareStatement("Delete From group14.Pass Where passID = ?")) {
					query2.setString(1, passID);
					query2.executeUpdate();
					System.out.println("Successfully deleted Ski Pass " + passID);
			} catch (SQLException ex) {
				System.out.println("SQL Error: " + ex.getMessage());
				}
		}
		else {
			System.out.println("Error: Only Expired or Useless passes can be deleted.");
		}
	}
}
