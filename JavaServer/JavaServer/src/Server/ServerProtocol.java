package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import DataObjects.ClientMessage;
import DataObjects.Data;
import DataObjects.Message;
import DataObjects.ServerMessage;
import DataObjects.User;
import DataObjects.UserGroup;
import Database.MessageDAO;
import Database.MySQLConnection;
import Database.UserDAO;
import Database.UserGroupDAO;
import JsonInterface.JsonControl;

/*
 * As with the other files, I will
 * continue to add more detailed comments
 * in the future.
 */

 /*
  * ServerProtocol is the class that
  * controls the connection to the client.
  * It has the input and output streams to 
  * the client socket, and the DAOs for 
  * each object that requires database
  * actions. The design is quite similar
  * to the ClientProtocol class. In the main loop,
  * it first checks if there is data to be read, and 
  * if there is it reads a line as the command.
  * It checks the value of the command, and first if
  * equals "updateRequest" it retrieves an update from its
  * Lists of stored updates and returns it to the client.
  * If the command is something different, then it reads
  * a second line as data and sends both lines to the
  * handUserAction method.
  */

public class ServerProtocol 
{	
	private BufferedReader in = null;
	private PrintWriter out = null;
	private Thread thread = null;
	public int userId = 0;
	public int currentGroupId = 0;
	
	MessageDAO messageDAO = null;
	UserDAO userDAO = null;
	UserGroupDAO userGroupDAO = null;
	
	public ServerProtocol(BufferedReader in, PrintWriter out, Thread thread, MySQLConnection mysqlc)
	{
		this.in = in;
		this.out = out;
		this.thread = thread;
		
		messageDAO = new MessageDAO(mysqlc);
		userDAO = new UserDAO(mysqlc);
		userGroupDAO = new UserGroupDAO(mysqlc);
	}

	boolean clientConnected = true;
	//command and data from server to client
	private List<String> serverCommands = new ArrayList<>();
	private List<String> serverDatas = new ArrayList<>();
	
	//main method for controlling the connection with the client
	//see above comments for more detail
	public void controlConnection()
	{	
		while (clientConnected)
		{
			try
			{	
				Thread.sleep(500);
				
				if (in.ready())
				{
					String command = in.readLine();
					
					if (command.equals("updateRequest"))
					{
						if (serverCommands.size() > 0)
						{
							out.println(serverCommands.get(0));
							serverCommands.remove(0);
							
							out.println(serverDatas.get(0));
							serverDatas.remove(0);
						}
						else 
						{
							out.println("noUpdates");
						}
					}
					else
					{
						String data = in.readLine();
						
						System.out.println("server command " + command);
						System.out.println("server data " + data);
						
						handleUserAction(command, data);
					}
				}
			}
			catch (Exception e)
			{
				clientConnected = false;
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * The handleUserAction method takes a command and data
	 * String, which are both read directly from the input
	 * stream in the main loop. It then compares the value 
	 * of the command to all the possible actions required.
	 * Depending on which action the command corresponds to,
	 * the method will send the data to a different method.
	 * Once that method has completed its action, it will
	 * return response data which handleUserAction then
	 * encodes and returns to the client with the original
	 * received action command.
	 */
	
	public void handleUserAction(String command, String data)
	{	
		if (command.equals("refreshAllGroups"))
		{
			List<UserGroup> groups = refreshAllGroups(data);
			out.println("refreshAllGroups");
			out.println(JsonControl.encodeObjectSet(groups));
		}
		else if (command.equals("refreshJoinedGroups"))
		{
			List<UserGroup> groups = refreshJoinedGroups(data);
			out.println("refreshJoinedGroups");
			out.println(JsonControl.encodeObjectSet(groups));
		}
		else if (command.equals("createUser"))
		{
			ServerMessage responseData = createUser(data);
			out.println("createUser");
			out.println(responseData.accessObjectJson());
		}
		else if (command.equals("validateUser"))
		{
			ServerMessage responseData = validateUser(data);
			out.println("validateUser");
			out.println(responseData.accessObjectJson());	
		}
		else if (command.equals("createGroup"))
		{
			ServerMessage responseData = createGroup(data);
			out.println("createGroup");
			out.println(responseData.accessObjectJson());
		}
		else if (command.equals("joinGroup"))
		{
			ServerMessage responseData = joinGroup(data);
			out.println("serverResponse");
			out.println(responseData.accessObjectJson());
		}
		else if (command.equals("saveMessage"))
		{
			ServerMessage serverMessage = saveMessage(data);
			out.println("saveMessage");
			out.println(serverMessage.accessObjectJson());
		}
		else if (command.equals("saveUserMessage"))
		{
			ServerMessage serverMessage = saveUserMessage(data);
			out.println("saveUserMessage");
			out.println(serverMessage.accessObjectJson());
		}
		else if (command.equals("refreshMessages"))
		{
			List<Message> messages = refreshMessages(data);
			out.println("refreshMessages");
			out.println(JsonControl.encodeObjectSet(messages));
		}
		else if (command.equals("refreshUserMessages"))
		{
			List<Message> messages = refreshMessages(data);
			out.println("refreshUserMessages");
			out.println(JsonControl.encodeObjectSet(messages));
		}
		else if (command.equals("displayAllUsers"))
		{
			List<User> users = refreshAllUsers(data);
			out.println("displayAllUsers");
			out.println(JsonControl.encodeObjectSet(users));
		}
		else if (command.equals("displayMessagedUsers"))
		{
			List<UserGroup> groups = refreshPrivateMessageGroups(data);
			out.println("displayMessagedUsers");
			out.println(JsonControl.encodeObjectSet(groups));
		}
		else if (command.equals("messageNewUser"))
		{
			ServerMessage message = messageNewUser(data);
			out.println("messageNewUser");
			out.println(message.accessObjectJson());
		}
	}
	
	/*
	 * The following methods are all called from 
	 * handleUserAction depending on the value
	 * of the received client command. They take
	 * a String of data in JSON form and decode it 
	 * using the correct data object's JSON decoding
	 * method. After the data has been decoded,
	 * any necessary database actions are performed
	 * through the data layer DAOs, and the response
	 * data is returned to handleUserAction.
	 */
	
	//creates a new User
	private ServerMessage createUser(String json)
	{
		User user = User.decodeFromClient(json);
		boolean actionSuccess = userDAO.createUser(user);
		ServerMessage responseMessage = null;
		
		if (actionSuccess)
		{
			responseMessage = new ServerMessage("userCreated");
		}
		else
		{
			responseMessage = new ServerMessage("userCreateError");
		}
		
		return responseMessage;
	}
	
	//validates login information
	private ServerMessage validateUser(String json)
	{
		User user = User.decodeFromClient(json);
		User validatedUser = userDAO.checkCredentials(user);
		ServerMessage responseMessage = null;
		
		if (validatedUser != null)
		{
			responseMessage = new ServerMessage("userValidated");
			//String threadName = Integer.toString(userId);
			//thread.setName(threadName);
			userId = validatedUser.getUserId();
		}
		else
		{
			responseMessage = new ServerMessage("userInvalid");
		}
		
		return responseMessage;
	}
	
	//creates a new UserGroup
	private ServerMessage createGroup(String json)
	{
		UserGroup group = UserGroup.decodeFromClient(json);
		boolean groupCreated = userGroupDAO.createGroup(group);
		ServerMessage responseMessage = null;
		
		if (groupCreated)
		{
			responseMessage = new ServerMessage("groupCreated");
			ServerConnection.refreshGroupCreated();
		}
		else
		{
			responseMessage = new ServerMessage("groupCreateError");
		}
		
		return responseMessage;
	}
	
	//creates a new GroupMembers row
	private ServerMessage joinGroup(String json)
	{
		ClientMessage clientMessage = ClientMessage.decodeFromClient(json);
		int groupId = clientMessage.getSendId();
		boolean groupJoined = userGroupDAO.joinGroup(groupId, userId);
		ServerMessage responseMessage = null;
		
		if (groupJoined)
		{
			responseMessage = new ServerMessage("groupJoined");
		}
		else
		{
			responseMessage = new ServerMessage("groupJoinError");
		}
		
		return responseMessage;
	}
	
	//returns all UserGroups
	private List<UserGroup> refreshAllGroups(String json)
	{
		ClientMessage clientMessage = ClientMessage.decodeFromClient(json);
		List<UserGroup> groups = userGroupDAO.getAllGroups(userId);
		return groups;
	}
	
	//Overload for calling group refresh from server side
	//not using client message for this action anyway
	private List<UserGroup> refreshAllGroups()
	{
		List<UserGroup> groups = userGroupDAO.getAllGroups(userId);
		return groups;
	}
	
	//returns all UserGroups a User belongs to 
	private List<UserGroup> refreshJoinedGroups(String json)
	{
		ClientMessage clientMessage = ClientMessage.decodeFromClient(json);
		List<UserGroup> groups = userGroupDAO.getJoinedGroups(userId);
		return groups;
	}
	
	//returns all Messages for a certain UserGroup
	private List<Message> refreshMessages(String json)
	{
		ClientMessage clientMessage = ClientMessage.decodeFromClient(json);
		int groupId = clientMessage.getSendId();
		currentGroupId = groupId;
		List<Message> messages = messageDAO.getMessages(groupId);
		return messages;
	}
	
	//overload for refreshing messages for an update
	//from another User
	private List<Message> refreshMessages(int groupId)
	{	
		List<Message> messages = messageDAO.getMessages(groupId);
		return messages;
	}

	//creates a new Message
	private ServerMessage saveMessage(String json)
	{
		Message message = Message.decodeFromClient(json);
		message.setSenderId(userId);
		boolean messageSaved = messageDAO.saveMessage(message);
		ServerMessage serverMessage = null;
		
		List<Integer> userIds = userGroupDAO.getGroupUserIds(currentGroupId);
		
		if (messageSaved)
		{
			serverMessage = new ServerMessage("messageSaved");
			ServerConnection.refreshMessages(userIds, currentGroupId);
		}
		else
		{
			serverMessage = new ServerMessage("messageSaveFailed");
		}
		
		return serverMessage;
	}
	
	//creates a new private Message
	private ServerMessage saveUserMessage(String json)
	{
		Message message = Message.decodeFromClient(json);
		message.setSenderId(userId);
		boolean messageSaved = messageDAO.saveMessage(message);
		ServerMessage serverMessage = null;
		
		List<Integer> userIds = userGroupDAO.getGroupUserIds(currentGroupId);
		
		if (messageSaved)
		{
			serverMessage = new ServerMessage("messageSaved");
			ServerConnection.refreshUserMessages(userIds, currentGroupId);
		}
		else
		{
			serverMessage = new ServerMessage("messageSaveFailed");
		}
		
		return serverMessage;
	}
	
	//returns all Users
	private List<User> refreshAllUsers(String json)
	{
		ClientMessage clientMessage = ClientMessage.decodeFromClient(json);
		List<User> users = userDAO.getAllUsers(userId);
		
		return users;
	}
	
	//returns all private message UserGroups a User belongs to
	private List<UserGroup> refreshPrivateMessageGroups(String json)
	{
		ClientMessage clientMessage = ClientMessage.decodeFromClient(json);
		List<UserGroup> privateMessageGroups = userGroupDAO.getPrivateMessageGroups(userId);
		
		return privateMessageGroups;
	}
	
	//create a new private UserGroup
	private ServerMessage messageNewUser(String json)
	{
		User otherUser = User.decodeMessageUser(json);
		UserGroup createdGroup = userGroupDAO.createPrivateMessageGroup(userId, otherUser);
		String sendGroupId = Integer.toString(createdGroup.getGroupId());
		ServerMessage serverMessage = new ServerMessage(sendGroupId);
		
		return serverMessage;
	}
	
	//refreshes all UserGroups for update from other User action
	public void sendGroupRefresh()
	{
		List<UserGroup> groups = refreshAllGroups();
		String data = JsonControl.encodeObjectSet(groups);
		serverCommands.add("updateAllGroups");
		serverDatas.add(data);
	}
	
	//refreshes all Messages for update from other User action
	public void sendMessageRefresh(int groupId)
	{
		List<Message> messages = refreshMessages(groupId);
		String data = JsonControl.encodeObjectSet(messages);
		serverCommands.add("updateMessages");
		serverDatas.add(data);
	}
	
	//refreshes all private Messages for update from other User action
	public void sendUserMessageRefresh(int groupId)
	{
		List<Message> messages = refreshMessages(groupId);
		String data = JsonControl.encodeObjectSet(messages);
		serverCommands.add("updateUserMessages");
		serverDatas.add(data);
	}
}
