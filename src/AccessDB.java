import java.sql.*;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;

public class AccessDB {
	
	//Connection conn = null;
	//private Statement statement = null;
	
	public static Connection getConnection() throws Exception{
		try {
			Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/hw4?useSSL=false","root","abc123");
			//System.out.println("Connected");
			return conn;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return null;
	}
	
	//Deletes a part from the 'parts' table.  Also, deletes from 'subpart_of' table using cascading delete.
	public void deletePart(Part part) throws Exception 
	{
			Connection conn = getConnection();
			Statement statement = conn.createStatement();

			String sql = "DELETE FROM parts " +
					"WHERE pid = " + part.getId();
			
			try {
				int numRows = statement.executeUpdate(sql);
				if (numRows == 0) {
					System.out.println("error");
				}
				else {
					System.out.println(numRows);	
				}			
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	
	//Inserts a part into database
	public void insertintoParts(Part part) throws Exception 
	{
		Connection conn = getConnection();
		Statement statement = conn.createStatement();
		ArrayList<Integer> mainpart = part.getmainpart();
		String sql = "INSERT INTO parts " +
				"(pid, name, price) VALUES " +
				"(" + part.getId() + ", '" + part.getName() + "', " + part.getPrice() + ")";
		
		try
		{
			statement.executeUpdate(sql);
			for (int i = 0; i < mainpart.size(); i++){
				insertintoSubpart_of(part.getId(), mainpart.get(i));
			}
			System.out.println("done");
		}
		catch (Exception ex)
		{
			System.out.println("error");
		}
	}
	
	//Inserts a mainpart/subpart relationship into the database
	private void insertintoSubpart_of(Integer mainpart, Integer subpart) throws Exception
	{
		Connection conn = getConnection();
		Statement statement = conn.createStatement();

		String sql = "INSERT INTO subpart_of " +
				"(eid, mid) VALUES " +
				"(" + mainpart + ", " + subpart +")";

		statement.executeUpdate(sql);
	}
	
	//Returns the average price of all parts
	public Integer averagePrice() throws Exception {
		Connection conn = getConnection();
		Statement statement = conn.createStatement();

		String sql = "SELECT AVG(price) " +
				"FROM parts ";
		
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(sql);
			if(rs.next()){
				return rs.getInt(1);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("error");
		}
		return -1;
	}
	
	//Gets the names of subparts that have multiple direct main-parts and return their names
	public ArrayList<String> getNamesOfMultipleSubParts() throws Exception {
		Connection conn = getConnection();
		Statement statement = conn.createStatement();

		String sql = "SELECT p.name " +
				"FROM parts AS p NATURAL JOIN (" +
					"SELECT sp.pid " +
					"FROM subpart_of AS sp " +
					"GROUP BY sp.pid " +
					"HAVING count(*) > 1) AS w";

		ArrayList<String> names = new ArrayList<String>();
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(sql);
			while (rs.next()){
				names.add(rs.getString(1));
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("error");
		}
		return names;
	}

	//Gets the names of subparts that have multiple indirect main-parts and return their names
	public ArrayList<String> getNamesOfMultipleIndirectSubParts() throws Exception {
		Connection conn = getConnection();
		Statement statement = conn.createStatement();

		String sql = "SELECT DISTINCT (name) " +
				"FROM (SELECT p.name AS name " +
				"FROM parts AS p " +
				"NATURAL JOIN (SELECT sp.pid " +
				"FROM subpart_of AS sp " +
				"GROUP BY sp.pid " +
				"HAVING count(*) > 1) AS w " + 
				"UNION " +
				"SELECT parts.name " +
				"FROM parts " +
				"NATURAL JOIN (SELECT sp1.pid " +
				"FROM subpart_of AS sp1 " +
				"JOIN subpart_of AS sp2 ON sp1.mid = sp2.pid) AS w2) AS q";

		ArrayList<String> names = new ArrayList<String>();
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(sql);
			while (rs.next()){
				names.add(rs.getString(1));
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("error");
		}
		return names;
	}
	
	//Returns the average price of a list of Part ids
	public Integer averagePriceOfPartsFromIDs(ArrayList<Integer> ids) throws Exception {
		Connection conn = getConnection();
		Statement statement = conn.createStatement();

		String idList = "";
		for (Integer id : ids) {
			idList = idList.concat(id.toString() + ", ");
		}
		if (idList.length() > 0){
			idList = idList.substring(0, idList.length() - 2);
		}
		else {
			return 0;
		}
		String sql = "SELECT AVG(price) " +
				"FROM parts " +
				"WHERE pid IN (" + idList + ")";
	
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(sql);

			if(rs.next()){
				return rs.getInt(1);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	
		return 0;
	}
	
	
	//gets PIDs of all subparts that is a subpart of a particular part
	public ArrayList<Integer> getSubPartIDSOfSubParts(Integer id) throws Exception {
		Connection conn = getConnection();
		Statement statement = conn.createStatement();
		
		String sql = "SELECT pid " +
				"FROM subpart_of " +
				"WHERE mid = " + id;
		
		
		ArrayList<Integer> subpartIDs = new ArrayList<Integer>();
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(sql);

			while(rs.next()){
				subpartIDs.add(rs.getInt(1));
			}
			
			ArrayList<Integer> spmp = new ArrayList<Integer>();
			for (Integer x : subpartIDs){
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp = this.getSubPartIDSOfSubParts(x);
				for (Integer y : temp) {
					if (!spmp.contains(y)){
						spmp.add(y);
					}
				}
			}
			subpartIDs.addAll(spmp);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}	
		
		return subpartIDs;
	}
	
	//Get part names from a list of part IDs
	public ArrayList<String> getPartNamesFromIDS(ArrayList<Integer> ids) throws Exception {
		Connection conn = getConnection();
		Statement statement = conn.createStatement();
		
		String idList = "";
		ResultSet rs = null;
		ArrayList<String> names = new ArrayList<String>();
		
		for (Integer id : ids) {
			idList = idList.concat(id.toString() + ", ");
		}
		if (idList.length() > 0){
			idList = idList.substring(0, idList.length() - 2);
		}
		else {
			return names;
		}
		String sql = "SELECT name " +
				"FROM parts " +
				"WHERE pid IN (" + idList + ")";

		try {
			rs = statement.executeQuery(sql);

			while(rs.next()){
				names.add(rs.getString(1));
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	
		return names;
	}
	
	
}
