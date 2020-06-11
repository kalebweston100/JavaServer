package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.jdbc.MysqlDataSource;

/*
 * MySQLConnection is the object
 * that creates the Connection to 
 * the database and creates the database
 * if not already created. It also contains 
 * public static methods used by every DAO 
 * to format MySQL queries into usable data.
 * A MySQLConnection is passed into the 
 * constructor of every DAO, and the 
 * getConnection method is then called.
 * The DAOs can then have access to a 
 * Connection to the database, but 
 * not access to the MySQLConnection
 * object itself.
 */

public class MySQLConnection 
{
	public MySQLConnection()
	{
		createConnection();
		createDatabase();
	}
	
	private Connection connection = null;
	
	public Connection getConnection()
	{
		return connection;
	}
	
	//creates a connection to the database
	private void createConnection()
	{	
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUser("user");
		dataSource.setPassword("password");
		dataSource.setServerName("localhost");
		dataSource.setDatabaseName("chat");
		
		try
		{
			connection = dataSource.getConnection();
		}
		catch (Exception e)
		{
			System.out.println("Error establishing connection to database");
			e.printStackTrace();
		}
	}
	
	//creates the database if not already created
	private void createDatabase()
	{	
		try
		{
			String query = "";
	
			query = "CREATE TABLE User (userId INT NOT NULL AUTO_INCREMENT,"
				  + "username VARCHAR(20),"
				  + "password VARCHAR(20),"
				  + "PRIMARY KEY(userId))";
			Statement createUser = connection.createStatement();
			createUser.executeUpdate(query);
			
			query = "CREATE TABLE UserGroup (groupId INT NOT NULL AUTO_INCREMENT,"
					  + "groupName VARCHAR(20),"
					  + "isPrivate BOOLEAN,"
					  + "PRIMARY KEY (groupId))";	
			Statement createGroup = connection.createStatement();
			createGroup.executeUpdate(query);
				
			query = "ALTER TABLE UserGroup ALTER isPrivate SET DEFAULT false";
			Statement addDefault = connection.createStatement();
			addDefault.executeUpdate(query);
			
			query = "CREATE TABLE Message (messageId INT NOT NULL AUTO_INCREMENT,"
				  + "senderId INT,"
				  + "groupId INT,"
				  + "messageContent VARCHAR(100),"
				  + "PRIMARY KEY (messageId))";		
			Statement createMessage = connection.createStatement();
			createMessage.executeUpdate(query);
			
			query = "CREATE TABLE GroupMembers (rowId INT NOT NULL AUTO_INCREMENT,"
				  + "groupId INT,"
				  + "userId INT,"
				  + "PRIMARY KEY (rowId))";
			Statement createUserGroup = connection.createStatement();
			createUserGroup.executeUpdate(query);
			
			System.out.println("Database created successfully");
		}
		catch (SQLException sqle)
		{
			System.out.println("Database already exists");
		}
		catch (Exception e)
		{
			System.out.println("Error creating database");
			e.printStackTrace();
		}	
	}
	
	//formats a ResultSet from the database into a List<String[]>
	//for the DAOs to more easily use
	public static List<String[]> queryToList(ResultSet results)
	{
		List<String[]> data = new ArrayList<>();
		//int columnLength = 0;
		
		try
		{
			while (results.next())
			{
				ResultSetMetaData meta = results.getMetaData();
				int columnNum = meta.getColumnCount();
				String[] row = new String[columnNum];
				
				for (int i = 0; i < columnNum; i++)
				{
					row[i] = results.getString(i + 1);
				}
				
				data.add(row);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return data;
	}
	
	//formats a ResultSet into an int
	//from a query for a single id
	public static int queryToId(ResultSet results)
	{
		int queryId = 0;
		
		try
		{
			results.next();
			String result = results.getString(1);
			queryId = Integer.parseInt(result);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return queryId;
	}
}
