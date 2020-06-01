package Client;

import java.util.ArrayList;
import java.util.List;
import DataObjects.ClientMessage;
import DataObjects.Data;
import DataObjects.Message;
import DataObjects.ServerMessage;
import DataObjects.User;
import DataObjects.UserGroup;

public abstract class AbstractUI
{
	//id of the UserGroup currently being used in the UI
	protected int selectedGroupId = 0;
	
	/*
	 * The currentGroups List is updated
	 * with all the groups a user has joined
	 * when the user is viewing the joined
	 * groups page. The selectedGroupId 
	 * variable is used to send the id of 
	 * the group the user selects to message. 
	 * selectedGroupId is also used for the 
	 * private message system, and similarly,
	 * it is used to send the id of the user 
	 * messaged. My variable names are confusing
	 * when it comes to the private message groups,
	 * so I will explain. The group system is used
	 * for all messaging, and once a User object has
	 * been selected from the allUsers page, the actual
	 * messaging goes through a UserGroup object 
	 * created with the name of the messaged user.
	 */
	
	protected List<UserGroup> currentGroups;
	
	/*
	 * The messagedUserGroups List stores all
	 * the UserGroup objects that function
	 * as messaging a single other user/client.
	 */
	
	protected List<UserGroup> messagedUserGroups;
	
	/*
	 * The allGroups List displays all the UserGroup objects
	 * that a user hasn't already joined.
	 */
	
	protected List<UserGroup> allGroups;
	
	/*
	 * The messages List is used to display
	 * all messages for the group chat messaging system,
	 * not the private messaging system.
	 */
	
	protected List<Message> messages;
	
	/*
	 * The userMessages List displays the 
	 * Message objects for the private 
	 * messaging system.
	 */
	
	protected List<Message> userMessages;
	
	//ArrayList to display all Users
	//that can then be selected to message
	protected List<User> allUsers;

	/*
	 * The requestCommands and requestDatas Lists
	 * are one of the most important parts of the 
	 * UI. They store the commands and data corresponding
	 * to the user actions so that the ClientProtocol
	 * can send them to the server and return the 
	 * response for the UI to render.
	 */
	
	protected List<String> requestCommands;
	protected List<Data> requestDatas;
	
	public AbstractUI()
	{
		currentGroups = new ArrayList<>();
		messagedUserGroups = new ArrayList<>();
		allGroups = new ArrayList<>();
		messages = new ArrayList<>();
		userMessages = new ArrayList<>();
		allUsers = new ArrayList<>();
		requestCommands = new ArrayList<>();
		requestDatas = new ArrayList<>();
	}
	
	//getters for ClientProtocol
	protected List<String> sendRequestCommands()
	{
		return requestCommands;
	}
	
	protected List<Data> sendRequestDatas()
	{
		return requestDatas;
	}
	
	protected void sendData(String requestCommand, Data requestData)
	{
		requestCommands.add(requestCommand);
		requestDatas.add(requestData);
	}
	
	//ensures that inheriting UI implements all methods
	//used by ClientProtocol to return data 
	//from server responses to the UI it is rendering
	protected abstract void validateCreateUser(ServerMessage serverMessage);
	protected abstract void validateLogin(ServerMessage serverMessage);
	protected abstract void validateCreateGroup(ServerMessage serverMessage);
	protected abstract void handleRefreshAllGroups(List<UserGroup> groups);
	protected abstract void validateJoinGroup(ServerMessage serverMessage);
	protected abstract void handleJoinedGroupRefresh(List<UserGroup> groups);
	protected abstract void handleMessageRefresh(List<Message> newMessages);
	protected abstract void validateSaveMessage(ServerMessage serverMessage);
	protected abstract void handleMessageNewUser(ServerMessage serverMessage);
	protected abstract void handleUserMessageRefresh(List<Message> newMessages);
	protected abstract void validateSaveUserMessage(ServerMessage serverMessage);
	protected abstract void handleAllUserRefresh(List<User> users);
	protected abstract void handleMessagedUserRefresh(List<UserGroup> groups);
	
	//sends data to create a User
	protected void sendUserData(String username, String password)
	{
		String command = "createUser";
		User user = new User(username, password);
		sendData(command, user);
	}
	
	//sends data to log in a User
	protected void sendLoginData(String username, String password)
	{
		String command = "validateUser";
		User user = new User(username, password);
		sendData(command, user);
	}
	
	//creates a UserGroup
	protected void createGroup(String name)
	{
		String command = "createGroup";
		UserGroup group = new UserGroup(name);
		sendData(command, group);
	}
	
	//displays all UserGroups
	protected void refreshAllGroups()
	{
		String command = "refreshAllGroups";
		ClientMessage data = new ClientMessage();
		sendData(command, data);
	}
	
	//creates a GroupMembers row when
	//a user selects a UserGroup to join
	protected void joinGroup(int groupId)
	{
		String command = "joinGroup";
		ClientMessage data = new ClientMessage(groupId);
		sendData(command, data);
	}
	
	//displays all UserGroups that a 
	//User has a GroupMembers row for
	protected void refreshYourGroups()
	{	
		String command = "refreshJoinedGroups";
		ClientMessage data = new ClientMessage();
		sendData(command, data);
	}
	
	//displays all Messages for a UserGroup
	protected void refreshMessages(int groupId)
	{
		//int groupId = selectedGroupId;
		String command = "refreshMessages";
		ClientMessage data = new ClientMessage(groupId);
		sendData(command, data);
	}
	
	//creates a Message
	protected void saveMessage(int groupId, String messageContent)
	{
		//String sentMessageContent = messageContent.getText();
		//int groupId = selectedGroupId;
		String command = "saveMessage";
		Message data = new Message(groupId, messageContent);
		sendData(command, data);
	}
	
	//creates a private UserGroup for another User
	protected void messageNewUser(int userId, String username)
	{
		String command = "messageNewUser";
		User data = new User(userId, username);
		sendData(command, data);
	}
	
	//displays Messages for a private UserGroup
	protected void refreshUserMessages(int groupId)
	{
		//int groupId = selectedGroupId;
		String command = "refreshUserMessages";
		ClientMessage data = new ClientMessage(groupId);
		sendData(command, data);
	}
	
	//saves a Message for a private UserGroup
	protected void saveUserMessage(int groupId, String messageContent)
	{
		//String sentMessageContent = messageInput.getText();
		//int groupId = selectedGroupId;
		String command = "saveUserMessage";
		Message data = new Message(groupId, messageContent);
		sendData(command, data);
	}
	
	//displays all Users
	protected void refreshAllUsers()
	{
		String command = "displayAllUsers";
		ClientMessage data = new ClientMessage();
		sendData(command, data);
	}
	
	//displays all private UserGroups
	//that the current User has a GroupMembers
	//row for
	protected void refreshMessagedUsers()
	{
		String command = "displayMessagedUsers";
		ClientMessage data = new ClientMessage();
		sendData(command, data);
	}
	
}
