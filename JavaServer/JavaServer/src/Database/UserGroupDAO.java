package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DataObjects.User;
import DataObjects.UserGroup;

/*
 * UserGroupDAO is the concrete 
 * implementation of the UserGroupActions
 * interface. Similarly to the other DAOs,
 * it implements all required methods and
 * contains any methods required for 
 * query formatting. 
 */

public class UserGroupDAO implements UserGroupActions
{
	private Connection connection = null;
	
	//takes a MySQLConnection in constructor
	//to get the Connection
	public UserGroupDAO(MySQLConnection mysqlc)
	{
		this.connection = mysqlc.getConnection();
	}
	
	//inserts a new UserGroup into the database
	@Override
	public boolean createGroup(UserGroup group)
	{
		boolean actionSuccess = false;
		String groupName = group.getGroupName();
		
		try
		{
			String query = "INSERT INTO UserGroup (groupName)"
						 + "VALUES ('" + groupName + "')";
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
	
	//inserts a new GroupMembers row into the database
	@Override
	public boolean joinGroup(int groupId, int userId)
	{
		boolean actionSuccess = false;
		
		try
		{
			String query = "INSERT INTO GroupMembers (groupId, userId) "
					     + "VALUES (" + groupId + ", " + userId + ")";
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
	
	//returns a query of all UserGroups formatted
	//into a List<UserGroup>
	@Override
	public List<UserGroup> getAllGroups(int userId)
	{
		List<String[]> results = queryAllGroups(userId);
		List<UserGroup> allGroups = constructFromDatabase(results);
		return allGroups;
	}
	
	//queries all UserGroups from the database
	private List<String[]> queryAllGroups(int userId)
	{
		List<String[]> groups = new ArrayList<>();
		
		try
		{
			String query = "SELECT * FROM UserGroup "
					     + "WHERE groupId NOT IN "
					     + "(SELECT groupId FROM GroupMembers "
					     + "WHERE userId=" + userId + " ) AND isPrivate=false";
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(query);
			groups = MySQLConnection.queryToList(results);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return groups;
	}
	
	//returns a query of all UserGroups joined by the current User
	//formatted into a List<UserGroup>
	@Override
	public List<UserGroup> getJoinedGroups(int userId)
	{
		List<String[]> results = queryJoinedGroups(userId);
		List<UserGroup> joinedGroups = constructFromDatabase(results);
		return joinedGroups;
	}
	
	//queries all UserGroups joined by the current User
	private List<String[]> queryJoinedGroups(int userId)
	{
		List<String[]> groups = new ArrayList<>();
		
		try
		{
			String query = "SELECT * FROM UserGroup "
					     + "WHERE groupId IN "
					     + "(SELECT groupId FROM GroupMembers "
					     + "WHERE userId=" + userId + " ) AND isPrivate=false";
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(query);
			groups = MySQLConnection.queryToList(results);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return groups;
	}
	
	//inserts a new private UserGroup into the database
	//returns the created UserGroup so that the User
	//can be sent to it directly after they create it
	@Override
	public UserGroup createPrivateMessageGroup(int currentUserId, User otherUser)
	{
		int newGroupId = 0;
		String newGroupName = "";
		
		try
		{
			String otherUserId = Integer.toString(otherUser.getUserId());
			String otherUsername = otherUser.getUsername();
			newGroupName = otherUsername;
			
			String query = "INSERT INTO UserGroup (groupName, isPrivate) " + 
						   "VALUES('" + otherUsername +"', 1)";
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			
			query = "SELECT LAST_INSERT_ID()";
			statement = connection.createStatement();
			ResultSet results = statement.executeQuery(query);
			newGroupId = MySQLConnection.queryToId(results);
			
			query = "INSERT INTO GroupMembers (groupId, userId) " + 
					"VALUES (" + newGroupId + ", " + currentUserId + ")";
			statement = connection.createStatement();
			statement.executeUpdate(query);
			
			query = "INSERT INTO GroupMembers (groupId, userId) " + 
					"VALUES (" + newGroupId + ", " + otherUserId + ")";
			statement = connection.createStatement();
			statement.executeUpdate(query);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		UserGroup newGroup = new UserGroup(newGroupId, newGroupName);
		
		return newGroup;
	} 
	
	//returns a query of all private UserGroups formatted
	//into a List<UserGroup>
	@Override
	public List<UserGroup> getPrivateMessageGroups(int userId)
	{
		List<String[]> results = queryPrivateMessageGroups(userId);
		List<UserGroup> groups = constructPrivateMessageGroups(results);
		return groups;
	}

	//queries all private UserGroups
	private List<String[]> queryPrivateMessageGroups(int userId)
	{
		List<String[]> privateMessageGroupData = new ArrayList<>();
		
		try
		{
			String query = "SELECT groupData.groupId, userData.username FROM " +
						   "(SELECT * FROM User) AS userData INNER JOIN " + 
						   "(SELECT * FROM GroupMembers WHERE groupId IN " +
						   "(SELECT groupId FROM UserGroup WHERE groupId IN " +
						   "(SELECT groupId FROM GroupMembers WHERE userId=" + userId + ") " +
						   "AND isPrivate=true) " +
						   "AND NOT userId=" + userId + ") AS groupData " +
						   "ON userData.userId=groupData.userId";
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(query);
			privateMessageGroupData = MySQLConnection.queryToList(results);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return privateMessageGroupData;
	}
	
	//constructs List<UserGroup> from raw query data
	//separate method for private UserGroup
	//because groupNames are User usernames instead
	//of a sent groupName
	private List<UserGroup> constructPrivateMessageGroups(List<String[]> privateMessageGroupData)
	{
		List<UserGroup> groups = new ArrayList<>();
		
		for (String[] groupData : privateMessageGroupData)
		{
			int groupId = Integer.parseInt(groupData[0]);
			String username = groupData[1];
			UserGroup group = new UserGroup(groupId, username);
			groups.add(group);
		}
		
		return groups;
	}
	
	//constructs a List<UserGroup> from raw query data
	private List<UserGroup> constructFromDatabase(List<String[]> results)
	{
		List<UserGroup> groups = new ArrayList<>();
		
		for (String[] result : results)
		{
			int groupId = Integer.parseInt(result[0]);
			String groupName = result[1];
			UserGroup group = new UserGroup(groupId, groupName);
			groups.add(group);
		}
		
		return groups;
	}
	
	//queries the userIds from members of a group
	public List<Integer> getGroupUserIds(int groupId)
	{
		List<Integer> userIds = new ArrayList<>();
		
		try
		{
			String query = "SELECT userId FROM groupMembers "
					     + "WHERE groupId=" + groupId;
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(query);
			List<String[]> idArrays = MySQLConnection.queryToList(results);
			
			for (String[] array : idArrays)
			{
				int userId = Integer.parseInt(array[0]);
				userIds.add(userId);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return userIds;
	}
}
