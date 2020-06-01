package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import DataObjects.User;
import DataObjects.UserGroup;

/*
 * UserDAO is the concrete implementation
 * of UserActions. It implements all the 
 * methods required for User database
 * actions, and similarly to MessageDAO,
 * it also has methods required for 
 * turning database queries into usable data.
 */

public class UserDAO implements UserActions
{	
	private Connection connection = null;
	
	public UserDAO(MySQLConnection mysqlc)
	{
		this.connection = mysqlc.getConnection();
	}
	
	//inserts a new User into the database
	@Override
	public boolean createUser(User user)
	{
		boolean actionSuccess = false;
		
		String username = user.getUsername();
		String password = user.getPassword();
		
		try
		{
			String query = "INSERT INTO User (username, password)"
						 + "VALUES ('" + username + "', '" + password + "')";
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			
			actionSuccess = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return actionSuccess;
	}
	
	//checks the username and password of a User
	//constructed from data entered into the login
	//of the UI. If a User with the username and password
	//exists in the database, returns a User with the userId
	//and username for the system to user while they are logged in
	@Override
	public User checkCredentials(User user)
	{	
		String username = user.getUsername();
		String password = user.getPassword();
		
		User resultUser = null;
		
		try
		{
			String query = "SELECT * FROM User WHERE "
						 + "username='" + username + "' AND "
						 + "password='" + password + "'";
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(query);
			
			
			if (results.isBeforeFirst())
			{
				List<String[]> userData = MySQLConnection.queryToList(results);
				
				String resultUserId = userData.get(0)[0];
				int userId = Integer.parseInt(resultUserId);
				String resultUsername = userData.get(0)[1];
				
				resultUser = new User(userId, resultUsername);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return resultUser;
	}
	
	//returns a query of all Users formatted into a List<User>
	@Override
	public List<User> getAllUsers(int userId)
	{
		List<String[]> results = queryAllUsers(userId);
		List<User> users = constructFromDatabase(results);
		return users;
	}
	
	//queries all Users in the database
	private List<String[]> queryAllUsers(int userId)
	{
		List<String[]> data = new ArrayList<>();
		
		try
		{
			
			String query = "SELECT * FROM User WHERE userId NOT IN " +
						   "(SELECT userId FROM GroupMembers WHERE groupId IN " + 
						   "(SELECT groupId FROM UserGroup WHERE groupId IN " +
						   "(SELECT groupId FROM GroupMembers WHERE userId=" + userId + ") " +
						   "AND isPrivate=true)) AND NOT userId=" + userId;
			
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(query);
			data = MySQLConnection.queryToList(results);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return data;
	}
	
	//constructs a List<User> from a raw query
	private List<User> constructFromDatabase(List<String[]> results)
	{
		List<User> users = new ArrayList<>();
		
		for (String[] object : results)
		{
			int userId = Integer.parseInt(object[0]);
			String username = object[1];
			User user = new User(userId, username);
			users.add(user);
		}
		
		return users;
	}
}
