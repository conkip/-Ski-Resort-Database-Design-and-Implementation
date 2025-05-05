/*=============================================================================
|   Assignment:  Program #4: Database Design and Implementation
|
|      Authors:  Group 14- Connor, Luis, Mohommad, Nathan
|     Language:  Java16
|   To Compile:  use 'javac Prog.Code" to compile and then 'java Code' to run
|
|        Class:  CSC 460- Database Design
|   Instructor:  Dr. McCann
|          TAS:  Xinyu and Jianwei
|     Due Date:  May __, 2025, at the beginning of class
|
+-----------------------------------------------------------------------------
|
|  Description:  Creates a database for a Ski Resort and allows the user to do 4 diferent queries on it.
|
|        Input:  None
|
|       Output:  Responses to user queries through the terminal.
|
|    Algorithm:  None.
|
|   Required Features Not Included:  None.
|
|   Known Bugs:  None.
|
|   Note:   N/A
|
*===========================================================================*/

import java.sql.*; // for access to the SQL interaction methods
import java.util.Scanner;  // for getting stdin on terminal

public class Code
{
    /*---------------------------------------------------------------------
    |  Method doDatabaseStuff ()
    |
    |  Purpose:  Connect to the database, perform the specified query, and then close the connection.
    |
    |  Pre-condition:  The queries are propperly formatted and tables are created in sql.
    |
    |  Post-condition: The responses to the queries have been propperly printed and formatted.
    |
    |  Parameters:
    |       qNum -- number of the query (1-4)
    |
    |       q1, q2, q3, q4 -- String(s) of sql query(s).
    |
    |       newYear -- String of the year to change the sql table to.
    |
    |  Returns: None.
    *-------------------------------------------------------------------*/
    private static void doDatabaseStuff(String qNum, String q1, String q2, String q3, String q4) {
        final String oracleURL =   // Magic lectura -> aloe access spell
                        "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";

        String username = "connorkippes",    // Oracle DBMS username
            password = "a4444";    // Oracle DBMS password


            // load the (Oracle) JDBC driver by initializing its base
            // class, 'oracle.jdbc.OracleDriver'.

        try {

                Class.forName("oracle.jdbc.OracleDriver");

        } catch (ClassNotFoundException e) {

                System.err.println("*** ClassNotFoundException:  "
                    + "Error loading Oracle JDBC driver.  \n"
                    + "\tPerhaps the driver is not on the Classpath?");
                System.exit(-1);

        }


        // make and return a database connection to the user's oracle database

        Connection dbconn = null;

        try {
                dbconn = DriverManager.getConnection
                            (oracleURL,username,password);

        } catch (SQLException e) {

                System.err.println("*** SQLException:  "
                    + "Could not open JDBC connection.");
                System.err.println("\tMessage:   " + e.getMessage());
                System.err.println("\tSQLState:  " + e.getSQLState());
                System.err.println("\tErrorCode: " + e.getErrorCode());
                System.exit(-1);

        }


            // Send the query to the DBMS, and get and display the results

        Statement s1 = null, s2 = null, s3 = null, s4 = null;
        ResultSet a1 = null, a2 = null, a3 = null, a4 = null;

        try {

            s1 = dbconn.createStatement();
            a1 = s1.executeQuery(q1);

            if (a1 == null) {
                System.out.println("this is not good");
                System.exit(0);
            }

            System.out.println("\nThe results of query " + qNum + " are:\n");

            // access the metadata to get the attribute names
            // to use as headers and use for accessing the attrs

            ResultSetMetaData answermetadata = a1.getMetaData();
            String meta2 = ""; //saving the second value

            for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
                String metadata = answermetadata.getColumnName(i);
                if(i == 2) {
                    meta2 = metadata;
                }
                System.out.printf("%-25s", metadata);
                if(i == 1){
                    System.out.print(" | ");
                }
            }
            System.out.println();
            System.out.println("--------------------------------------------------");

            // have to make seperate statements so it doesnt overwrite the previous connections
            if(!qNum.equals("1")) {
                s2 = dbconn.createStatement();
                a2 = s2.executeQuery(q2);
                s3 = dbconn.createStatement();
                a3 = s3.executeQuery(q3);
                s4 = dbconn.createStatement();
                a4 = s4.executeQuery(q4);
            }

            // next() to advance cursor through the tuples (rows) and print attributes (cols)

            if(qNum.equals("1")){
                int i = 0;
                while (a1.next() && i < 10) { // only print top 10 states
                    i++;
                    System.out.printf("%-25s | %-25d %n", a1.getString("state"), 
                            a1.getInt(meta2));
                }
            }
            else{ //if its 2, 3 or 4 bc they are the same
                a1.next();
                System.out.printf("%-25d | %-25d %n", a1.getInt("year"), 
                        a1.getInt(meta2));
                a2.next();
                System.out.printf("%-25d | %-25d %n", a2.getInt("year"), 
                        a2.getInt(meta2));
                a3.next();
                System.out.printf("%-25d | %-25d %n", a3.getInt("year"), 
                        a3.getInt(meta2));
                a4.next();
                System.out.printf("%-25d | %-25d %n", a4.getInt("year"), 
                        a4.getInt(meta2));
            }

            System.out.println();

            
            // close the connection to the DBMS.

            s1.close();
            if(!qNum.equals("1")) {
                s2.close();
                s3.close();
                s4.close();
            }
            dbconn.close();

        } catch (SQLException e) {

                System.err.println("*** SQLException:  "
                    + "Could not fetch query results.");
                System.err.println("\tMessage:   " + e.getMessage());
                System.err.println("\tSQLState:  " + e.getSQLState());
                System.err.println("\tErrorCode: " + e.getErrorCode());
                System.exit(-1);

        }
    }

    // we can just replace these queries
    public static void main (String [] args)
    {
        System.out.println("\nYou may perform 4 querys on the data."
                            + "Perform a query by typing the number of the\n"
                            + "query followed by any required parameters" 
                            + "seperated by spaces in all lower case.\n\n"

                            + "Query 1: Get something.\n"
                            + "         Ex- 1 parameter1\n\n"

                            + "Query 2: \n"
                            + "         Ex-\n\n"

                            + "Query 3: \n"
                            + "         Ex-\n\n"

                            + "Query 4: \n"
                            + "         Ex-\n\n");
                            // this query is interesting because buses stay pretty consistant so we
                            // can see the popular eras of motorcycles like in the 2000s and now its
                            // dwindling down, but there are still random spikes in states

        Scanner scanner = new Scanner(System.in); // set up scanner to read user input

        while(true){
            System.out.println("Enter a query here (type q to quit):");
            String nextLine = scanner.nextLine();

            String[] line = nextLine.split(" ");
            String q = "";

            if(line[0].equals("q")){
                break;
            }
            else if(line[0].equals("1")) {
                q = "";

            }
            else if (line[0].equals("2")) {
                q = 
                "SELECT year, COUNT(state) AS states_under_million_reg \n" +
                "FROM \n" +
                "(\n" +
                "SELECT year, state, (auto + bus + truck + motorcycle) AS total \n" +
                "FROM connorkippes.MVRD1990 \n" +
                "WHERE (auto + bus + truck + motorcycle) < 1000000 \n" +
                ") t1 \n" +
                "GROUP BY year";

                //for example
            }
            else if (line[0].equals("3")) {
                q = "";
            }
            else if (line[0].equals("4")) {
                q = "";
            }
            else {
                System.out.println("You entered the query incorrectly.");
                continue;
            }
            
            System.out.println("\nQuery:\n\n" + q1);
            
            doDatabaseStuff(line[0], q);
        }
        scanner.close();
    }
} // main()