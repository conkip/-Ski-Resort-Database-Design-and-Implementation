/*=============================================================================
|   Assignment:  Program #4:  Database Design and Implementation
|      Authors:  Group 14- Connor, Luis, Mohommad, Nathan
|       Grader:  Xinyu and Jianwei
|
|       Course:  CSc 460 — Database Design
|   Instructor:  Dr. L. McCann
|     Due Date:  May 6th, 2025
|
|  Compile/Run:  Compile: javac main.java Queries.java DML.java
|                Run    : java Prog3 <username> <password>
+-----------------------------------------------------------------------------
|  Description:  This program implements the front-end of a two-tier client-server
|      architecture for a ski resort management system using JDBC and Oracle SQL.
|      It allows users to interact with a backend Oracle database through a
|      text-based interface, supporting a variety of operations including:
|          - Inserting, updating, and deleting records for members, passes,
|            rentals, lessons, and equipment
|          - Running pre-defined SQL queries for retrieving lesson usage, lift and
|            rental activity, open trails, and more
|
|     Language:  Java 17
| Ex. Packages:  java.sql.*, java.util.*, oracle.jdbc.OracleDriver
|
|   Known Bugs:  No known bugs at this time.
*===========================================================================*/

import java.io.*;
import java.sql.*;
import java.util.*;

/*+----------------------------------------------------------------------
||  Class main
||
||         Authors:  Group 14 – Connor, Luis, Mohammad, Nathan
||
||        Purpose:  This class serves as the entry point for the Ski Resort
||                  Management System. It establishes a JDBC connection
||                  to the Oracle database, initializes the DML and Query
||                  modules, and provides a text-based menu for user
||                  interaction.
||
||  Inherits From:  None.
||
||     Interfaces:  None.
||
|+-----------------------------------------------------------------------
||
||      Constants:
||         DEBUG -- Flag to enable/disable debug mode output.
||
|+-----------------------------------------------------------------------
||
||   Constructors:  None (uses static main method).
||
||  Class Methods:  None.
||
||  Inst. Methods:  None.
||
++-----------------------------------------------------------------------*/
public class main {
  private static final boolean DEBUG = true;
  private static Queries queries;
  private static DML dml;
  private static Scanner scanner;

  /*---------------------------------------------------------------------
  |  Method main
  |
  |  Purpose:  Initializes the application, connects to the Oracle database
  |      using provided credentials (or defaults), sets up the user input
  |      scanner and core modules (`Queries`, `DML`), and repeatedly
  |      presents a command-line menu for performing database operations
  |      or queries until the user chooses to exit.
  |
  |  Pre-condition:  Oracle JDBC driver must be available in the classpath;
  |      the Oracle database server (aloe.cs.arizona.edu) must be online;
  |      schema and tables must exist and be accessible to the user.
  |
  |  Post-condition:  Connection is established, menu options are displayed,
  |      and upon exit, resources (DB connection and scanner) are closed.
  |
  |  Parameters:
  |      args -- (String[]) Optional: user-provided Oracle username and password
  |
  |  Returns:  None (void). Runs until terminated by user selection.
  *-------------------------------------------------------------------*/
  public static void main(String args[]) {
    final String oracleURL = "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";

    String username = null, password = null;

    // Check if username and password are provided as command line arguments
    if (args.length == 2) { // get username/password from cmd line args
      username = args[0];
      password = args[1];
    } else {
      username = "mhrezaei"; // Default username
      password = "a3121"; // Default password
    }

    // Load the (Oracle) JDBC driver by initializing its base class, 'oracle.jdbc.OracleDriver'.
    try {
      Class.forName("oracle.jdbc.OracleDriver");
    } catch (ClassNotFoundException e) {
      System.err.println(
          "*** ClassNotFoundException:  "
              + "Error loading Oracle JDBC driver.  \n"
              + "\tPerhaps the driver is not on the Classpath?");
      System.exit(-1);
    }

    // Make and return a database connection to the user's Oracle database
    Connection dbconn = null;
    try {
      dbconn = DriverManager.getConnection(oracleURL, username, password);
    } catch (SQLException e) {
      System.err.println("*** SQLException:  " + "Could not open JDBC connection.");
      System.err.println("\tMessage:   " + e.getMessage());
      System.err.println("\tSQLState:  " + e.getSQLState());
      System.err.println("\tErrorCode: " + e.getErrorCode());
      System.exit(-1);
    }

    // If we get here, we have a connection to the database.
    if (DEBUG) {
      System.out.println("Connected to database: " + dbconn);
    }

    // We need an instance of the Queries and DML classes
    this.scanner = new Scanner(System.in);
    this.queries = new Queries(dbconn, scanner);
    this.dml = new DML(dbconn, scanner);

    // ==========================================================
    // Main program loop
    System.out.println("\n===============================");
    System.out.println("Welcome to Group 14's Ski Resort Database!");

    while (true) {
      // Display the menu
      System.out.println("\nPlease select an option:");
      System.out.println("1. Operations on the database");
      System.out.println("2. Queries");
      System.out.println("3. Exit");
      System.out.print("Enter your choice: ");
      int choice = scanner.nextInt();

      if (choice == 1) {
        dml.performDML();
      } else if (choice == 2) {
        queries.performQueries();
      } else if (choice == 3) {
        System.out.println("Exiting the program.");
        break;
      } else {
        System.out.println("Invalid choice. Please try again.");
      }
    }

    // Close the database connection
    try {
      dbconn.close();
    } catch (SQLException e) {
      System.err.println("*** SQLException:  Could not close connection.");
      System.err.println("\tMessage:   " + e.getMessage());
    }
    // Close the scanner
    scanner.close();
  }
}
