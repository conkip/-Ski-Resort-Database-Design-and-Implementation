import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Queries {
	public static void main(String[] args) {
		final String oracleURL = "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";
		System.out.println("1) - For a given member, list all the ski lessons they have purchased, including\n	the number of remaining sessions, instructor name, and scheduled time.");
		System.out.println("2) - For a given ski pass, list all lift rides and equipment rentals associated\n	with it, along with timestamps and return status.");
		System.out.println("3) - List all open trails suitable for intermediate-level skiers, along with\n	their category and connected lifts that are currently operational.");
		System.out.println("4) - ");
		System.out.println("5) - Add/Delete/Update a dataset.");
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
