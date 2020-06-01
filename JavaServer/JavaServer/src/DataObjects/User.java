package DataObjects;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import JsonInterface.JsonControl;
import JsonInterface.JsonObject;

/*
 * User is the object that 
 * stores the data of the accounts
 * that are currently using the 
 * system. It is first used to send
 * the data to create an account, 
 * and then the login data to be validated
 * with the database. It is also used for 
 * the private messaging system when
 * displaying the logged in users on 
 * other client connections.
 */

public class User implements Data
{	
	private int userId = 0;
	private String username = "";
	private String password = "";
	
	public User()
	{
		
	}
	
	//constructor for User object sent from 
	//client to server
	public User(String username, String password)
	{
		this.username = username;
		this.password = password;
	}
	
	//constructor for User object sent from
	//server to client
	////////////////don't want password of user on a different client
	public User(int userId, String username)
	{
		this.userId = userId;
		this.username = username;
	}
	
	public int getUserId()
	{
		return userId;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	//see explanation of method in Data
	@Override
	public String accessObjectJson()
	{
		JsonObject jsonObject = new JsonObject();
		
		int userId = getUserId();
		String username = getUsername();
		String password = getPassword();
		
		if (userId != 0)
		{
			jsonObject.addInt("userId", userId);
			jsonObject.addItem("username", username);
		}
		else
		{
			jsonObject.addItem("username", username);
			jsonObject.addItem("password", password);
		}
		
		String json = jsonObject.accessJson();
		return json;
	}
	
	//decodes created user json for server to save in database
	public static User decodeFromClient(String object)
	{
		String[] data = JsonControl.decodeObject(object);
		String username = data[0];
		String password = data[1];
		User user = new User(username, password);
		return user;
	}
	
	//decodes a single User selected to be messaged
	public static User decodeMessageUser(String object)
	{
		String[] data = JsonControl.decodeObject(object);
		int userId = Integer.parseInt(data[0]);
		String username = data[1];
		User user = new User(userId, username);
		return user;
	}
	
	//decodes user json sent from server
	public static List<User> decodeFromServer(String json)
	{
		List<String[]> objects = JsonControl.decodeObjectSet(json);
		List<User> users = new ArrayList<>();
		
		for (String[] object : objects)
		{
			int userId = Integer.parseInt(object[0]);
			String username = object[1];
			User user = new User(userId, username);
			users.add(user);
		}
		
		return users;
	}

}
