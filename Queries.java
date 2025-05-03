import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Queries {
	public static void main(String[] args) {
		final String oracleURL = "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";
		System.out.println("1) - Members");
		System.out.println("2) - Ski Passes");
		System.out.println("3) - Equipment Inventory");
		System.out.println("4) - Equipment Rentals");
		System.out.println("5) - Lesson Purchases");
		System.out.print("Choose which dataset to manage: ");
		try (Scanner sc = new Scanner(System.in)) {
			System.out.print("Choose a query or \"Quit\" to exit: ");
			while (true) {
				String choice = sc.nextLine();
				if (choice.equals("1")) {
					query1(oracleURL);
					return;
				} else if (choice.equals("2")) {
					query2(oracleURL);
					return;
				} else if (choice.equals("3")) {
					query3(oracleURL);
					return;
				} else if (choice.equals("4")) {
					query4(oracleURL);
					return;
				} else if (choice.equals("5")) {
					query5(oracleURL);
					return;
				} else if (choice.equals("Quit")) {
					return;
				} else
					System.out.println("Invalid Input Try Again.");
			}
		}
	}

	
	private static void query1(String url) {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection dbconn = null;

		try {
			dbconn = DriverManager.getConnection(url, "", ""); // Your login
		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not open JDBC connection.");
			System.exit(-1);
		}
	}

	
	private static void query2(String url) {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection dbconn = null;

		try {
			dbconn = DriverManager.getConnection(url, "", ""); // Your login
		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not open JDBC connection.");
			System.exit(-1);
		}
	}

	
	private static void query3(String url) {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection dbconn = null;

		try {
			dbconn = DriverManager.getConnection(url, "", ""); // Your login
		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not open JDBC connection.");
			System.exit(-1);
		}
	}

	
	private static void query4(String url) {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection dbconn = null;

		try {
			dbconn = DriverManager.getConnection(url, "", "");  // Your login
		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not open JDBC connection.");
			System.exit(-1);
		}
	}

	
	private static void query5(String url) {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection dbconn = null;

		try {
			dbconn = DriverManager.getConnection(url, "", "");  // Your login
		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not open JDBC connection.");
			System.exit(-1);
		}
	}
}
