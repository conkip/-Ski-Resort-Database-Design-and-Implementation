/**
 * CSc 460 — Database Design Spring 2025 (McCann) http://www2.cs.arizona.edu/classes/cs460/spring25/
 * Program #4: Database Design and Implementation Due Dates: Team Members: April 22 nd, 2025, at the
 * beginning of class Draft E–R Diagram: April 29 th, 2025, at the beginning of class Final Product:
 * May 6 th, 2025, at the beginning of class Designed by Xinyu Guo and Jianwei (James) Shen
 * Overview: In this assignment, you will build a database–driven information management system from
 * ground up. We will give you an application domain to work on. Your goal is to design the
 * underlying database and define the application functionalities you will provide with the
 * database, and implement this application using Oracle within a text–based JDBC program.
 * Assignment: In this assignment you are to implement a two–tier client–server architecture. 1.
 * Database Back-End, which runs the Oracle DBMS on aloe.cs.arizona.edu. Your job is to design the
 * database relational schema, create tables and populate your tables with some initial data. We are
 * requiring that you create an E–R diagram, analyze the FDs of each table and apply table
 * normalization techniques to your schema to justify that your schema satisfies 3NF, and, if
 * possible, BCNF. 2. JDBC Front–End, which is the client’s user interface. You need to design a
 * text–based application that appropriately handles all the required functionalities. Your client
 * application will run on lectura. Application Domain: The problem description for the project is
 * as follows: As a ski resort striving to stand out in the local tourism market, we have recently
 * expanded our facilities to include a comprehensive entertainment and leisure area. This aims to
 * enhance the overall customer experience and attract more families and young visitors. While ski
 * passes and skiing activities remain our primary sources of revenue, we also wish to develop this
 * integrated area into a highlight of its own. To achieve this, we hope to build a system that
 * enables close integration between the skiing area and entertainment facilities, thereby improving
 * both customer experience and operational efficiency for staff. The ski resort owns different
 * types of properties, including multiple lodges that offer dining options, several gift shops, a
 * rental center, a visitor center where visitors can buy passes, a ski school, free parking lots,
 * and paid parking lots. The ski resort offers free shuttle service between properties for
 * visitors. The stakeholder may want to keep track of the daily income from different sources.
 * Before using any skiing facilities, customers must first register as members. For each member, we
 * collect a basic yet essential set of information, e.g., name, telephone number, email address,
 * date of birth, and emergency contact. Once registration is complete, members may purchase ski
 * passes, rent equipment, and begin their skiing journey either through self-service kiosks or at
 * the front desk. To manage access and skiing rights within the resort, we have designed a “Ski
 * Pass” module. The system should comprehensively track information such as the total number of
 * uses, remaining uses, time of purchase, expiration date, and associated member account. To note,
 * we offer different types of passes, e.g., 1-day, 2-day, 4-day, and season passes. (Continued...)
 * The ski resort provides equipment rental services to visitors who need them. Equipment rental is
 * tied to the ski pass: one use of the ski pass allows for one equipment rental. Equipment includes
 * ski boots, ski poles, skis (both snowboard and alpine), and protective gear. Ski boots range from
 * size 4 to 14 with half sizes, the length of ski poles ranges from 100cm to 140cm (integer), the
 * length of ski ranges from 115cm to 200cm (integer), and the length of snowboard ranges from 90cm
 * to 178cm (integer). The system must manage inventory and rental records. The ski resort has a lot
 * of trails for visitors to have fun. Each trail contains the start and end location, status for
 * open, the difficulty level (beginner, intermediate, expert, for simplicity), and category
 * (groomed, park, moguls, and glade skiing). We also want to offer different names for each trail
 * so that people can easily remember them. Feel free to come up with creative names. To reach the
 * trails on the mountain, visitors can take the lifts. Each lift has its unique name for people to
 * remember. To prevent possible accidents, the lift should indicate its ability level (beginner,
 * intermediate, expert, for simplicity) to better accommodate the visitors. Each lift has a regular
 * open and close time within a day for visitor to plan their runs in the resort. Besides, visitors
 * also want to know which trails each lift takes them to. Visitors also want to check their status
 * before they visit, since some of the lifts may open later in the season for a deeper snow cover.
 * Members scan their ski pass at the entrance of the lift. A valid entry counts as one ski pass
 * use. The system records all entry logs for people who want to check their statistics. The ski
 * school offers ski lessons for kids and adults. The resort employs PSIA-AASI certified instructors
 * (Level I, II, III). Instructors can manage private and group lessons. The system must store
 * lesson schedules, purchases, and usage records. The ski resort employs a lot of people to
 * maintain the service. Their job positions should cover all the services provided above. The ski
 * resort also wants to keep track of employees’ demographic information and employment start dates.
 * Besides, the stakeholder wants to know the gross income per month by calculating the difference
 * between the monthly income from all the properties and the monthly salary paid to every employee.
 * This description does not describe every detail. These are the essentials; we expect that your
 * team will create logical and conceptual designs that incorporate all of these features, at
 * minimum. You are free to add additional details that you feel are appropriate. For each table you
 * create, you need to populate a reasonable number of tuples in order to test your queries
 * adequately. Some data basics are provided in the application domain description; the rest are
 * left for you to determine, based on your needs. (What is ‘reasonable’ is difficult to define; a
 * few dozen tuples per relation certainly would be; just a handful per relation may not provide
 * sufficient variety.) We realize that you are not an expert in this domain, but you have dealt
 * with similar organizations in your life. Hopefully, you have enough experience that this problem
 * description makes sense. If you have questions, please ask, and the TAs will help you clear
 * things up. Required functionalities: Within the framework provided above, your system is expected
 * to perform ex- amples of the following operations: 1. Record insertion: Your application should
 * support inserting a new data record via a JDBC interface. 2. Record deletion: Your application
 * should support deleting an existing data record via a JDBC interface. 3. Record update: Your
 * application should support updating an existing data record via a JDBC interface. 4. Queries:
 * Your application should support querying your database via a JDBC interface for the problem
 * description given above. You are required to implement the three provided queries as well as at
 * least one query of your own design. Details are provided below. (Continued...) 2 Specifically,
 * the JDBC application’s interface should enable users to: 1. Add, update, or delete a member: The
 * member table contains essential personal details, including name, phone number, email address,
 * date of birth, and emergency contact. Each member must be assigned a unique member ID upon
 * registration. The system must support updating a member’s contact details at any time to ensure
 * accurate and up-to-date communication. This includes changes to phone number, email, or emergency
 * contact. If a member wishes to leave the resort system, such as no longer participating in skiing
 * activities, they may request account deletion. Prior to deletion, the system must verify that the
 * member does not hold any active ski passes, open rental records, or unused lesson sessions. If
 * any of these exist, the system should notify the user to either complete or cancel them. Once all
 * checks pass and deletion is confirmed, the system must remove all related records, including ski
 * pass data, lift usage logs, rental history, and lesson transactions associated with the member.
 * 2. Add, update, or delete a ski pass: The ski pass table stores key information for managing
 * skiing access, including the total number of uses, remaining uses, purchase time, expiration
 * date, price, and the ID of the purchasing member. Each ski pass is assigned a unique pass ID upon
 * purchase. When a member purchases a ski pass, the system must link the pass to the member’s ID
 * and record the full set of attributes at the time of purchase. The pass’s validity is determined
 * by both usage count and expiration date. The system must support updating pass usage—each valid
 * entry and exit at a lift deducts one use from the remaining count. If a member reports an issue
 * (e.g., incorrect usage deduction), administrators should be able to manually adjust the remaining
 * uses. Ski passes cannot be deleted arbitrarily. Deletion is only allowed if the pass has expired
 * and has no remaining uses, or in the case of a confirmed refund process. Deleted ski passes must
 * be archived for audit purposes, but will be excluded from active queries and member usage
 * summaries. 3. Add, update, or delete an equipment inventory record: The equipment inventory table
 * manages all rentable ski equipment, including boots, poles, snowboards, alpine skis, and
 * protective gear. Each equipment item is identified by a unique equipment ID, and is classified by
 * type and size. When new equipment is added to the inventory, the system must assign a unique
 * identifier and record the exact type and size. This ensures accurate tracking of available
 * equipment and proper matching with rental requests based on member needs. Updates may occur if
 * the equipment is reclassified, mislabeled, or reassigned a different size due to maintenance. The
 * system should allow authorized personnel to make such updates while maintaining change logs for
 * audit purposes. Deletion is allowed only if the equipment is permanently retired or lost. Before
 * deletion, the system must verify that the item is not currently rented out or reserved. Removed
 * equipment should be flagged and archived, rather than permanently erased, to maintain historical
 * integrity in rental and usage records. 4. Add, update, or delete an equipment rental record: The
 * equipment rental table tracks each rental transaction, including the rental ID, associated member
 * ID, linked ski pass ID, rental time, and return status. A new record is created when a member
 * rents equipment using a valid ski pass. The system must update the return status when the
 * equipment is returned. Rental records may only be deleted if created in error and the equipment
 * has not been used. All updates and deletions must be logged to ensure a reliable transaction
 * history. These records are critical for calculating ski pass usage frequency and monitoring
 * member activity within the resort. (Continued...) 3 5. Add, update, or delete a lesson purchase
 * record: This table logs lesson purchases, including order ID, member ID, lesson ID, total number
 * of sessions purchased, and remaining sessions. Updates reflect usage or adjustments to the
 * purchase. Deletion is only permitted if no sessions have been used. Here are the queries that
 * your application is to be able to answer: 1. For a given member, list all the ski lessons they
 * have purchased, including the number of remaining sessions, instructor name, and scheduled time.
 * 2. For a given ski pass, list all lift rides and equipment rentals associated with it, along with
 * timestamps and return status. 3. List all open trails suitable for intermediate-level skiers,
 * along with their category and connected lifts that are currently operational. 4. One additional
 * non-trivial query of your own design, with these restrictions:  The question must use more than
 * two relations.  It must be constructed using at least one piece of information gathered from the
 * user. Working in Groups: In industry, such a project is usually the work of multiple developers,
 * because it involves several different components. Good communication is a vital key to the
 * success of the project. This assignment provides an opportunity for just this sort of teamwork.
 * Therefore, we are accepting team sizes of between two and four members (inclusive). Working solo
 * is not permitted. Early on, you will need to agree on a reasonable workload distribution plan for
 * your team, with well–defined responsibilities, deliverables, and expected completion dates. Such
 * a plan will minimize conflicts and debugging effort in the actual implementation. Don’t have a
 * lot of experience working on group projects and would like some pointers? The following page has
 * a short list of useful suggestions:
 * https://www.saintleo.edu/about/stories/blog/7-tips-to-more-effectively-work-on-group-projects
 * Late days: Late days can be used on this assignment, but only on the third due date. How many a
 * team has to use is determined as follows: Team members total their remaining late days, and
 * divide by the num- ber of members in the team (integer division), producing the number of late
 * days the team has available, to a max of two days late. (Why? The TAs need to get grading done
 * soon after the due date, you need time to study for your final exams, and the department has a
 * rule about assignments needing to be due before the start of finals.) For example, a team whose
 * three members have 1, 1, and 3 late days remaining have ⌊ 1+1+3 3 ⌋ = 1 late day to use, if
 * needed. (Continued...) 4 Hand In: Here are the ‘deliverables’ for each of the assignment’s three
 * due dates: 1. Team Composition: By the first due date (see the top of the front page of this
 * handout), one member of your team must add a new row to the shared Google Sheet with the names
 * and email addresses of all team members. The link to the Google Sheet has already been posted to
 * Piazza. Failure to do so by the start of class on this date will cost your team the corresponding
 * points listed in the Grading Criteria section (below). 2. E–R Diagram: As stated in the
 * Assignment section, your team will need to create an E–R diagram that describes your database
 * design. Before the second due date, your team will need to prepare a draft of your E–R diagram
 * and a member of your team will need to submit it through turnin to the cs460p4 folder. The
 * purpose of this requirement is to allow the TAs to review your schema and make suggestions for
 * improvement. The sooner you create your design and discuss it with the TAs, the more time you
 * will have to refine your final E–R diagram. If TAs need further explanation of your E–R Diagram,
 * they’ll send out an email to make an appointment to have an additional meeting. 3. Final Product:
 * On or before the third due date, a member of your team must submit a .tar file of your
 * well–documented application program file(s) via turnin to the folder cs460p4. The tar file should
 * contain all of the following: (a) The source code for your application. (b) A PDF file called
 * “design.pdf” containing the following sections in this order: i. Conceptual database design: Your
 * final E–R diagram along with your design rationale and any necessary high–level text description
 * of the data model (e.g., constraints, or anything you were not able to show in the E–R diagram
 * but that is necessary to help people understand your database design). ii. Logical database
 * design: The conversion of your E–R schema into a relational database schema. Provide the schemas
 * of the tables resulting from this step. iii. Normalization analysis: For each of your entity sets
 * (tables), provide all of the FDs of the table and justify why your the table adheres to 3NF /
 * BCNF. iv. Query description: Describe your self–designed query. Specifically, what question is it
 * answer- ing, and what is the utility of including such a query in the system? (c) A ReadMe.txt
 * describing: i. Compilation and execution instructions, to enable the TAs to execute your
 * application and exercise the required functionalities. ii. The workload distribution among team
 * members (that is, which people were responsible for which parts of the project). In addition,
 * each team must schedule a time slot (∼15 – 20 minutes) to meet with a TA, demonstrate your
 * system, and perhaps answer some questions about it. Closer to the final due date, we will let you
 * know how to sign up. (Continued...) 5 Grading Criteria: Total: 100 points 1. Team Composition
 * (1st due date): 5 2. Complete E–R Diagram Draft (2nd due date): 20 3. Final Submission (3rd due
 * date): 75 (a) Coding / Implementation: 55  Documentation 15  Style and organization 10  Record
 * insertion: 5  Record deletion: 5  Record update: 10  Record query: 10 (b) Database design: 20
 *  Final E–R diagram: 10  Normalization analysis: 10 Grading Notes: 1. Unless we receive
 * verifiable complaints about inadequate contributions, each member of a team will receive the same
 * score on this assignment. 2. We won’t put much weight at all on the appearance of the text
 * application; concern yourselves with the application’s design instead. The main point of the
 * assignment is the DB design and how well it supports the use cases (the DML operations and
 * queries, in particular).
 */
import java.io.*;
import java.sql.*;
import java.util.*;

public class main {
  private static final boolean DEBUG = true;
  private static Queries queries;
  private static DML dml;
  private static Scanner scanner;

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
