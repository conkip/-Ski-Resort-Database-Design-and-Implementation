import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

/*+----------------------------------------------------------------------
||  Class Queries
||
||         Author:  Group 14 – Connor, Luis, Mohammad, Nathan
||
||        Purpose:  This class provides methods for executing SQL queries
||                  against the ski resort Oracle database. It handles
||                  user-driven query selection and processes results
||                  for display through a command-line interface.
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
||   Constructors:
||       Queries(Connection dbconn, Scanner scanner)
||
||  Class Methods:  None.
||
||  Inst. Methods:
||       void performQueries()
||       void query1()
||       void query2()
||       void query3()
||       void query4()
||
++-----------------------------------------------------------------------*/
public class Queries {

  private Connection dbconn;
  private Statement stmt;
  private ResultSet rset;
  private Scanner scanner;

  /*---------------------------------------------------------------------
  |  Constructor Queries
  |
  |  Purpose:  Initializes the Queries object with a JDBC database
  |      connection and a Scanner object for user input. It attempts
  |      to create a SQL statement object for executing queries.
  |
  |  Pre-condition:
  |      - dbconn must be a valid, active JDBC Connection to the Oracle DB.
  |      - scanner must be an initialized Scanner object.
  |
  |  Post-condition:
  |      - The Queries object is ready to execute SQL SELECT queries.
  |      - A Statement object is created; any SQLException will be printed.
  |
  |  Parameters:
  |      dbconn -- (IN) a valid JDBC Connection to the Oracle database.
  |      scanner -- (IN) a Scanner object for reading user input.
  |
  |  Returns:  None.
  *-------------------------------------------------------------------*/
  public Queries(Connection dbconn, Scanner scanner) {
    this.dbconn = dbconn;
    this.scanner = scanner;
    try {
      stmt = dbconn.createStatement();
    } catch (SQLException e) {
      System.err.println("*** SQLException:  " + "Could not create statement.");
      System.err.println("\tMessage:   " + e.getMessage());
      System.err.println("\tSQLState:  " + e.getSQLState());
      System.err.println("\tErrorCode: " + e.getErrorCode());
    }
  }

  /*---------------------------------------------------------------------
  |  Method performQueries
  |
  |  Purpose:  Prompts the user to choose from a list of predefined
  |      SQL queries and calls the appropriate method to execute the
  |      query and display the results.
  |
  |  Pre-condition:  Scanner must be initialized and connected to input;
  |      the Oracle connection must be valid and active.
  |
  |  Post-condition:  The selected query is executed and its output is
  |      printed to the console. Invalid inputs are handled gracefully.
  |
  |  Parameters:  None.
  |
  |  Returns:  None.
  *-------------------------------------------------------------------*/
  public void performQueries() {
    // This method will be used to call the query methods
    // and display the results to the user.
    System.out.println("Which query would you like to perform?");
    System.out.println(
        "1) - For a given member, list all the ski lessons they have purchased, including\n"
            + "\tthe number of remaining sessions, instructor name, and scheduled time.");
    System.out.println(
        "2) - For a given ski pass, list all lift rides and equipment rentals associated\n"
            + "\twith it, along with timestamps and return status.");
    System.out.println(
        "3) - List all open trails suitable for intermediate-level skiers, along with\n"
            + "\ttheir category and connected lifts that are currently operational.");
    System.out.println(
        "4) - Display current properties and their monthly income. Choose to\n"
              + "\tdisplay all properties, profitable or unprofitable properties.");

    try {
      int choice = scanner.nextInt();
      scanner.nextLine(); // Consume the newline character

      if (choice == 1) {
        query1();
      } else if (choice == 2) {
        query2();
      } else if (choice == 3) {
        query3();
      } else if (choice == 4) {
        query4();
      } else {
        System.out.println("Invalid choice. Please try again.");
      }
    } catch (InputMismatchException e) {
      System.out.println("Invalid input. Please enter a number.");
      scanner.nextLine(); // Clear the invalid input
      performQueries(); // Call the method again to allow for retry
    }
  }

  /*---------------------------------------------------------------------
  |  Method query1
  |
  |  Purpose:  For an inputed memberID, this method performs the sql query to list their
  |            ID, name, and all the ski lessons they have purchased, including
  |            the number of remaining sessions, instructor name, and scheduled time.
  |
  |  Pre-condition:  The LessonPurchase, LessonOffering, Employee, and Member tables
  |      must exist and be populated with valid data.
  |
  |  Post-condition:  A list of open, intermediate trails is printed
  |      to the console, each showing its name, category, and associated
  |      operational lifts.
  |
  |  Parameters:  None.
  |
  |  Returns:  None.
  *-------------------------------------------------------------------*/
  private void query1() {
    System.out.print("Please provide the memberID that");
    System.out.println("you would like to see the lesson purchases for.\n");

    String memberID = scanner.nextLine();

    String sql =
        "SELECT lp.remainingSessions, e.name AS instructor_name, lo.schedule AS scheduled_time \n"
            + "FROM nathanlamont.LessonPurchase lp \n"
            + "JOIN nathanlamont.lessonOffering lo ON lo.lessonID = lp.lessonID \n"
            + "JOIN nathanlamont.Employee e ON lo.instructorID = e.employeeID \n"
            + "JOIN nathanlamont.Member m ON m.memberID = lp.memberID \n"
            + "WHERE m.memberID = '" + memberID + "'";

    try {
      // Execute the query
      rset = stmt.executeQuery(sql);

      // Process the result set
      System.out.println("Lesson Purchases for member #" + memberID + ":");

      // Check if the result set is empty
      int count = 1;
      while (rset.next()) {
        int remainingSessions = rset.getInt("remainingSessions");
        String instName = rset.getString("instructor_name");
        String scheduledTime = rset.getString("scheduled_time");

        String output =
            "Lesson #"
                + count
                + ": Remaining Sessions: "
                + remainingSessions
                + ", Instructor Name: "
                + instName
                + ", Scheduled Time: "
                + scheduledTime;
        System.out.println(output);
        count++;
      }
      if(count == 1) {
        // no lessons found
        System.out.println("No lesson purchases");
      }
    } catch (SQLException e) { // Handle SQL exceptions
      System.err.println("*** SQLException:  " + e.getMessage());
      System.err.println("\tSQLState:  " + e.getSQLState());
      System.err.println("\tErrorCode: " + e.getErrorCode());
    }
  }

  /*---------------------------------------------------------------------
  |  Method query2
  |
  |  Purpose:  For a specified Pass ID, this method returns all of the Equipment rentals
  |			   Associated with it, as well as the Lift uses associated as well.
  |
  |  Pre-condition:  The Pass, Lift, LiftLog, Rental, and Equipment tables
  |      must exist and be populated with valid data.
  |
  |  Post-condition:  This method prints the PassID, LiftID, Trail, and DateTime of the
  |					  specified pass, as well as the PassID, Type, RentalTime, and RentalStatus of associated rentals
  |
  |  Parameters:  None.
  |
  |  Returns:  None.
  *-------------------------------------------------------------------*/
  private void query2() {
    String pass = null;
    try {
      System.out.print("Enter Pass ID to view information: ");
      System.out.println();
      pass = scanner.nextLine();
    } catch (Exception e) {
      System.err.println("Error reading input: " + e.getMessage());
      return;
    }

    String query1 =
        "Select passID as Pass, liftID, liftName as Trail, dateTime\n"
            + "From nathanlamont.Pass \n"
            + "JOIN nathanlamont.LiftLog USING (passID)\n"
            + "JOIN nathanlamont.Lift USING (liftID) \n"
            + "WHERE passID = '" + pass + "'";


    String query2 =
        "Select passID as Pass, equipmentType, rentalTime, returnStatus\n"
            + "From nathanlamont.Pass \n"
            + "JOIN nathanlamont.Rental USING (passID)\n"
            + "JOIN nathanlamont.Equipment USING (equipmentID) \n"
            + "WHERE passID = '" + pass + "'";


    try {
      rset = stmt.executeQuery(query1);

      System.out.println(pass + " Lift History:");
      System.out.println("---------------");

      while (rset.next()) {
        String idno = rset.getString("Pass");
        String liftid = rset.getString("liftID");
        String trail = rset.getString("Trail");
        Date time = rset.getDate("dateTime");
        System.out.println(
            "Pass: " + idno + ", Lift ID: " + liftid + ", Name: " + trail + ", Time: " + time);
      }

      rset = stmt.executeQuery(query2);

      System.out.println(pass + " Rental History:");
      System.out.println("---------------");

      while (rset.next()) {
        String idno = rset.getString("Pass");
        String eqtype = rset.getString("equipmentType");
        Date rentalTime = rset.getDate("rentalTime");
        int status = rset.getInt("returnStatus");
        System.out.println(
            "Pass: "
                + idno
                + ", Type: "
                + eqtype
                + ", Rental Time: "
                + rentalTime
                + ", Rental Status: "
                + status);
      }
    } catch (SQLException e) { // Handle SQL exceptions
      System.err.println("*** SQLException, could not execute query:  " + e.getMessage());
      System.err.println("\tSQLState:  " + e.getSQLState());
      System.err.println("\tErrorCode: " + e.getErrorCode());
    }
  }

  /*---------------------------------------------------------------------
  |  Method query3
  |
  |  Purpose:  This method retrieves and displays all trails that are
  |      currently open and suitable for intermediate-level skiers,
  |      along with their category and a comma-separated list of
  |      operational lifts that are connected to each trail.
  |
  |  Pre-condition:  The Trails, TrailLift, and Lifts tables must exist
  |      and be populated with valid data.
  |
  |  Post-condition:  A list of open, intermediate trails is printed
  |      to the console, each showing its name, category, and associated
  |      operational lifts.
  |
  |  Parameters:  None.
  |
  |  Returns:  None.
  *-------------------------------------------------------------------*/
  private void query3() {
    String sql =
        "SELECT t.name AS trail_name, t.category, "
            + "LISTAGG(l.liftName, ', ') WITHIN GROUP (ORDER BY l.liftName) AS lifts "
            + "FROM nathanlamont.Trail t "
            + "JOIN nathanlamont.TrailLift tl ON t.name = tl.trailName "
            + "JOIN nathanlamont.Lift l ON tl.liftID = l.liftID "
            + "WHERE t.difficulty = 'Intermediate' AND t.status = 1 AND l.status = 1 "
            + "GROUP BY t.name, t.category";

    try {
      // Execute the query
      rset = stmt.executeQuery(sql);

      // Process the result set
      System.out.println("Open Intermediate Trails and their connected lifts:");

      // Check if the result set is empty
      while (rset.next()) {
        String trailName = rset.getString("trail_name");
        String category = rset.getString("category");
        String lifts = rset.getString("lifts");
        System.out.println("Trail: " + trailName + ", Category: " + category + ", Lifts: " + lifts);
      }
    } catch (SQLException e) { // Handle SQL exceptions
      System.err.println("*** SQLException:  " + e.getMessage());
      System.err.println("\tSQLState:  " + e.getSQLState());
      System.err.println("\tErrorCode: " + e.getErrorCode());
    }
  }

  /*---------------------------------------------------------------------
  |  Method query4
  |
  |  Purpose:  This method displays properties that are profitable, unprofitable or
  |            all properties via user input. It uses the property,shop and employee relations.
  |
  |  Pre-condition:  The property, shop and employee tables must exist and contain relevant data
  |
  |  Post-condition:  A list of properties and their income is displayed to meet user speciifcations
  |
  |  Parameters:  None.
  |
  |  Returns:  None.
  *-------------------------------------------------------------------*/
  private void query4() {
    HashMap<String, Float> map = new HashMap<>();
    
    System.out.println("Select desired output: ");
    System.out.println("(1) All properties monthly income ");
    System.out.println("(2) Select specific property");
    
    int q4Choice = -1;
    try {
      q4Choice = scanner.nextInt();
      scanner.nextLine();
    } catch (InputMismatchException e) {
      System.out.println("Invalid input... ");
      scanner.nextLine();
      return;
    }

    String propertySql =
        "SELECT p.propertyID, p.name, SUM(s.income) AS monthly_income "
            + "FROM nathanlamont.Shop s "
            + "JOIN nathanlamont.Property p ON s.buildingID = p.propertyID "
            + "GROUP BY p.propertyID, p.name "
            + "ORDER BY p.propertyID";

    String employeeSql =
        "SELECT p.propertyID, p.name, SUM(e.monthlySalary) AS monthly_cost "
            + "FROM nathanlamont.Employee e "
            + "JOIN nathanlamont.Property p ON e.propertyID = p.propertyID "
            + "GROUP BY p.propertyID, p.name "
            + "ORDER BY p.propertyID";

    try {
      rset = stmt.executeQuery(propertySql);
      while (rset.next()) {
        map.put(rset.getString("name"), rset.getFloat("monthly_income"));
      }
      rset = stmt.executeQuery(employeeSql);
      while (rset.next()) {
        map.put(
            rset.getString("name"),
            map.get(rset.getString("name")) - rset.getFloat("monthly_cost"));
      }
    } catch (SQLException e) {
      System.err.println("*** SQLException:  " + e.getMessage());
      System.err.println("\tSQLState:  " + e.getSQLState());
      System.err.println("\tErrorCode: " + e.getErrorCode());
    }

    if (q4Choice == 1) {
      map.forEach(
          (propertyName, netIncome) -> {
            System.out.println("Property: " + propertyName + ", Net Income: $" + netIncome);
          });
    } else if (q4Choice == 2) {
      System.out.println("Enter property ID to select: ");
      int propertyID = scanner.nextInt();
      scanner.nextLine(); // clean newline

      propertySql =
        "SELECT p.propertyID, p.name, SUM(s.income) AS monthly_income "
            + "FROM nathanlamont.Shop s "
            + "JOIN nathanlamont.Property p ON s.buildingID = p.propertyID "
            + "Where p.propertyID = " + propertyID + " "
            + "GROUP BY p.propertyID, p.name "
            + " ORDER BY p.propertyID";

      employeeSql =
        "SELECT p.propertyID, p.name, SUM(e.monthlySalary) AS monthly_cost "
            + "FROM nathanlamont.Employee e "
            + "JOIN nathanlamont.Property p ON e.propertyID = p.propertyID "
            + "Where p.propertyID = " + propertyID + " "
            + "GROUP BY p.propertyID, p.name "
            + "ORDER BY p.propertyID";
            
      try {
        rset = stmt.executeQuery(propertySql);
        float income = 0;
        float cost = 0;
        String propName = "";
        boolean failed = false;
        if(rset.next()) {
          income = rset.getFloat("monthly_income");
          propName = rset.getString("name");
        } else {
          failed = true;
          System.out.println("No Shop income found for property ID: "+propertyID);
        }

        rset = stmt.executeQuery(employeeSql);
        if(rset.next()) {
          cost = rset.getFloat("monthly_cost");
        } else {
          failed = true;
          System.out.println("No Employee Cost found for property ID: "+propertyID);
        }

        if(!failed) {
          float total = income - cost;
          System.out.println("Name: "+propName+" ID: "+propertyID+" income: "+ total);
        }

      } catch (SQLException e) {
        System.err.println("*** SQLException:  " + e.getMessage());
        System.err.println("\tSQLState:  " + e.getSQLState());
        System.err.println("\tErrorCode: " + e.getErrorCode());
      }

    } else {
      System.out.println(q4Choice + " Is not a valid choice. Please try again.");
    }
  }
}
