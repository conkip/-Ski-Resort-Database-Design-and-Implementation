// Group 14- Connor, Luis, Mohommad, Nathan
// Creates an sql file to create all of the necessary tables
// and fills them up in Oracle.


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CreateTables {
    public static void main(String[] args) {
        // NOTE: these tables might need more identifiers like foreign key relations or constraints

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("createTables.sql"));

                // ****** PROPERTY TABLE *******

            bw.write("CREATE TABLE group14.Property (\n"
                    + "propertyid INTEGER,"
                    + "name VARCHAR2(30),\n"
                    + "income DECIMAL(12, 2),\n"
                    + "adress VARCHAR2(30),\n" 
                    + "property VARCHAR2(15),\n"
                    + "primary key (Property ID)\n);\n\n" //could also just sort by adress and remove property ID
            );

            bw.write("INSERT INTO group14.Property\n"
                    + " VALUES (12345, Copper Lodge, 100000, 1234 West Ski Street, ski lodge);\n");

            bw.write("\nCOMMIT;\nGRANT SELECT ON group14.Property TO PUBLIC;");


                // ****** SHOP TABLE *******

            bw.write("CREATE TABLE group14.Shop (\n"
                    + "shopid INTEGER,"
                    + "name VARCHAR2(30),\n"
                    + "type VARCHAR2(15),\n"
                    + "buildingid DECIMAL(12, 2)\n"
                    + "income DECIMAL(12, 2),\n"
                    + "primary key (shopid)\n);\n\n"
            );

            bw.write("INSERT INTO group14.Shop\n"
                    + " VALUES (56789, KFC, resturant, 12345, 10000);\n");

            bw.write("\nCOMMIT;\nGRANT SELECT ON group14.Shop TO PUBLIC;");
            
            	// ****** MEMBER TABLE *******
            
            bw.write("CREATE TABLE group14.Member (\n"
                    + "memberID INTEGER,\n"
            		+ "name VARCHAR2(25),\n"
                    + "phoneNumber INTEGER,\n"
                    + "email VARCHAR2(25),\n"
                    + "birthdate DATE,\n"
                    + "emergencyName VARCHAR2(25),\n"
                    + "emergencyPhone INTEGER,\n"
                    + "emergencyEmail VARCHAR2(25),\n"
                    + "primary key (memberID)\n);\n\n"
            );

            bw.write("INSERT INTO group14.Pass\n"
                    + " VALUES (500, 'John Smith', 20, 4-day, 200, 5-10-2025);\n");
            
            	// ****** SKI PASS TABLE *******
            
            bw.write("CREATE TABLE group14.Pass (\n"
            		+ "passID INTEGER,"
                    + "memberID INTEGER,"
                    + "numUses INTEGER,\n"
                    + "passType VARCHAR2(6),\n"
                    + "price INTEGER,\n"
                    + "exprDate DATE,\n"
                    + "primary key (passID)\n);\n\n"
            );

            bw.write("INSERT INTO group14.Pass\n"
                    + " VALUES (1000, 500, 20, 4-day, 200, 5-10-2025);\n");

            bw.write("\nCOMMIT;\nGRANT SELECT ON group14.Pass TO PUBLIC;");

            //do the same with the rest of the relations

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
