import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Scanner;
import java.time.*;

public class SkiPassHandler {
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static void addPass(Connection dbconn) {
		int memID;
		int numUses;
		String passType;
		float price;
		String expDate;
		String curr = null;
		String query1 = "SELECT COUNT(*) AS count FROM group14.Pass;";
		
		try (Scanner sc = new Scanner(System.in)) {
			System.out.println("Enter Member ID of new Pass: ");
			memID = (int) sc.nextLine();
			System.out.println("Enter the type of pass: ");
			passType = sc.nextLine();
		}
		
		if (passType.equals("Season")) {
			price = 499.99;
			numUses = 100;
			LocalDate today = LocalDate.now();
	        expDate = today.plusDays(60).format(formatter);
	        
		}
		else if (passType.equals("Weekend")) {
			price = 79.99;
			numUses = 30;
			LocalDate today = LocalDate.now();
	        expDate = today.plusDays(4).format(formatter);
		}
		else if (passType.equals("Day")) {
			price = 49.99;
			numUses = 10;
			LocalDate today = LocalDate.now();
	        expDate = today.plusDays(1).format(formatter);
		}
		else {System.out.println("Incorrect Pass Type. Try Again.") return};
		
		try (Statement stmt = dbconn.createStatement()) {
			ResultSet rset = stmt.executeQuery(query1);
			while (rset.next()) {
		        curr = rset.getInt("count") + 1;
		      }
		}
		
		String passID = "P" + curr;
		
		String query2 = "INSERT INTO group14.Pass VALUES ("
	              + passID
	              + ", "
	              + memID
	              + ", "
	              + numUses
	              + ", "
	              + passType
	              + ", "
	              + price
	              + ", "
	              + expDate
	              + ");";
	}

	public static void updatePass(Connection dbconn) {

	}

	public static void deletePass(Connection dbconn) {

	}
}
