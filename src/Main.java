import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import com.mysql.jdbc.Connection;

public class Main {
	
	public static void main(String[] args) throws Exception {
		//getConnection();
		createTable();
		readTransfile();	
		dropDatabase();
		}
		
		public static Connection getConnection() throws Exception{
			try {
				Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/hw4?useSSL=false","root","abc123");
				System.out.println("Connected");
				return conn;
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
			return null;
		}

		
		public static void createTable() throws Exception{
			try {
				Connection conn = getConnection();
				Statement stmt = conn.createStatement();
				String sql1 = "CREATE TABLE IF NOT EXISTS parts " +
							"(pid int, " + 
							"name varchar(255), " +
							"price int, " + 
							"PRIMARY KEY (pid) )";
				stmt.executeUpdate(sql1);
				String sql2 = "CREATE TABLE IF NOT EXISTS subpart_of " +
						"(pid int, " + 
						"mid int, " +
						"FOREIGN KEY (pid) references parts(pid) ON DELETE CASCADE ON UPDATE CASCADE, " + 
						"FOREIGN KEY (mid) references parts(pid) ON DELETE CASCADE ON UPDATE CASCADE )";
			stmt.executeUpdate(sql2);
				
			}
			catch(SQLException  se)
			{ 
				se.printStackTrace();
			}
			finally 
			{
				System.out.println("Function complete");
			}
		}
		
		public static void dropDatabase() throws Exception
		{
			Connection conn = getConnection();
			Statement stmt = conn.createStatement();
			try {
				stmt.executeUpdate("DROP DATABASE hw4");
			}
			catch (Exception ex) {
				System.out.println(ex.toString());
			}

			try {
				conn.close();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
			
		public static  void readTransfile() {
			
			BufferedReader reader;
			final String FILENAME = "C:\\Users\\Kruneet\\Desktop\\Transfile.txt";
			File file = new File(FILENAME);
			
			try {
				reader = new BufferedReader(new FileReader(file));
				String line = reader.readLine();
				while (line != null) {
					execute(line);
					// read next line
					line = reader.readLine();
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private static void execute(String line)
		{
			//create the object to access Database
			AccessDB db = null;
			
			// To ignore the commented lines
			if (line.charAt(0) == '*') return;
			
			try {
				UseCaseCode command = new UseCaseCode(line);
				command.execute(db);
			}
			catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		

	}
	


