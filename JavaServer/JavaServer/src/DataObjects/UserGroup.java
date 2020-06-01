package DataObjects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import JsonInterface.JsonControl;
import JsonInterface.JsonObject;

/*
 * UserGroup is the object that
 * stores data for the group
 * messaging system. This name
 * may be confusing because based
 * on how the system is designed,
 * all messaging is based on groups.
 * Even the private messaging uses
 * groups, just with a private group 
 * containing only two clients.
 */

public class UserGroup implements Data 
{
	private int groupId = 0;
	private String groupName = "";
	
	public UserGroup(String groupName)
	{
		this.groupName = groupName;
	}
	
	//constructed from json
	//so groupId is entered as a String
	public UserGroup(int groupId, String groupName)
	{
		this.groupId = groupId;
		this.groupName = groupName;
	}
	
	public int getGroupId()
	{
		return groupId;
	}
	
	public String getGroupName()
	{
		return groupName;
	}
	
	//see explanation of method in Data
	@Override
	public String accessObjectJson()
	{
		JsonObject jsonObject = new JsonObject();
		
		int groupId = getGroupId();
		
		if (groupId != 0)
		{
			jsonObject.addInt("groupId", groupId);
		}
		
		String groupName = getGroupName();
		jsonObject.addItem("groupName", groupName);
		String json = jsonObject.accessJson();
		return json;
	}
	
	//only one group will ever be sent to the server
	//in order to create a group
	public static UserGroup decodeFromClient(String json)
	{
		String[] data = JsonControl.decodeObject(json);
		String name = data[0];
		UserGroup group = new UserGroup(name);
		return group;
	}
	
	//the server can send multiple groups to the client
	//when they are requested to display on the 
	//view groups page
	public static List<UserGroup> decodeFromServer(String json)
	{
		List<String[]> objects = JsonControl.decodeObjectSet(json);
		List<UserGroup> groups = new ArrayList<>();
		
		for (String[] object : objects)
		{
			int groupId = Integer.parseInt(object[0]);
			String groupName = object[1];
			UserGroup group = new UserGroup(groupId, groupName);
			groups.add(group);
		}
		
		return groups;
	}
	
}
