For HW4, I have used Eclipse IDE to write a Java program which interfaces with MySQL Workbench. 
I used the following JDBC to connect a Java program to a MySQL database and to test the program :
"Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/hw4?useSSL=false","root","abc123");"
Please change the database accordingly.

When we run the program, it creates a DB with the following schema :
1. parts(pid: integer, name: string, priceinteger)
2. subpart_of(pid: integer, mid: integer)
The database uses foreign key “references” constraints, as well as “on delete cascade“ and “on
update cascade” constraints to maintain referential integrity. 
After the program completes, the created database is dropped. (Please change the database name in the dropDatabase() method in the Main.java file )

The program will read a file named “transfile”. (Please change the path of transfile in readTransfile() method in the Main.java file )
A sample transfile is attached in the submission. 

Attached in the submission are the following four Java files:

1. Main.java :  
		Contains the main function which calls createTable();readTransfile();dropDatabase();
	        Also, it has getConnection() method which will create the connection and return the connection object.
	      	createTable() sets up the DB in the MySQL database.
		dropDatabase() deletes the databse as mentioned in the HW description.
		readTransfile() processes the transfile line by line and pass the values separated by spaces in it to the SQL queries in AcessDB.java file

2. Part.java :	
		Contains the getter and setter methods for accessing attributes of parts object. 
		And also a toString() method that overrides java.lang.Object.toString and converts to string.

3. AccessDB.java :
		Contains all the methods used to execute the commands according to the transaction code's value. Has the SQL queries.

4. UseCaseCode.java :
		Contains the Use case switch statement to execute the commands read from the transfile.

Assumptions made :
		There is a function in the AccessDB class that supports only direct mainparts. However,
		this is not called in the submitted implementation.

		Also, for transaction code 2 (to include a new part), a new part is added to the
		database. If it fails, the program does not continue adding tuples into the subpart_of relation. 
		If it succeeds and there are subpart_of tuples to be added, the tuples are inserted one at a time. 
		If any of these fail, the rest will continue to be inserted. 