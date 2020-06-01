package Client;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import DataObjects.ClientMessage;
import DataObjects.Data;
import DataObjects.Message;
import DataObjects.ServerMessage;
import DataObjects.User;
import DataObjects.UserGroup;
import JsonInterface.JsonControl;

/*
 * ClientProtocol is the main class
 * for communication between the client 
 * and the server. I probably won't have 
 * enough comments to explain everything yet,
 * but I'll try to give an overview.
 */
	
 /*
  * The client system runs based on a request,
  * response, and update protocol. In the main loop,
  * it first retrieves the UI's requestCommands
  * and requestDatas ArrayLists. It then checks the
  * size of these Lists to determine if there were any 
  * new user actions since the last iteration. If there
  * are new user actions, it sends the requests out to 
  * the server and removes the requests from the Lists. 
  * It then reads in the response from the server and 
  * checks if the response is an updates. Depending
  * on the type of response, it handles the command
  * and data from the server with either the processUpdate
  * or processResponse methods. Finally, if there are 
  * no user action requests, then the client sends
  * out a request for updates and either receives back
  * an update or a message that there are currently
  * no updates.
  */

public class ClientProtocol 
{
	//ClientProtocol is constructed with the 
	//input and output streams of the client socket
	private BufferedReader in = null;
	private PrintWriter out = null;
	private AbstractUI clientUI = null;
	
	public ClientProtocol(BufferedReader in, PrintWriter out, AbstractUI clientUI)
	{
		this.in = in;
		this.out = out;
		this.clientUI = clientUI;
	}

	// boolean controlling runConnection loop
	boolean activeConnection = true;

	// main method for controlling the functionality
	// of the client system, see above comment block
	// for more explanation
	public void runConnection()
	{	
		while (activeConnection)
		{
			List<Data> rawRequestDatas = getRequestDatas();
			
			List<String> requestCommands = getRequestCommands();
			List<String> requestDatas = convertRequestDatas(rawRequestDatas);
			
			try
			{
				Thread.sleep(500);
				
				if (requestCommands.size() > 0 && requestDatas.size() > 0)
				{
					String requestCommand = requestCommands.get(0);
					requestCommands.remove(0);
					
					String requestData = requestDatas.get(0);
					rawRequestDatas.remove(0);
					
					out.println(requestCommand);
					out.println(requestData);
					
					String responseCommand = in.readLine();
					String responseData = in.readLine();
					
					if (checkForUpdate(responseCommand))
					{
						processUpdate(responseCommand, responseData);
					}
					else
					{
						processResponse(responseCommand, responseData);
						//////////////////////////////////////////////////do I need to keep sending request??
						//keep sending request because 
						//requestCommands.remove(0);
						//requestDatas.remove(0);
					}
				}
				else
				{
					out.println("updateRequest");
					
					String responseCommand = in.readLine();
					
					if (!responseCommand.equals("noUpdates"))
					{
						String responseData = in.readLine();
						processUpdate(responseCommand, responseData);
					}
				}
			}
			catch (Exception e)
			{
				activeConnection = false;
				e.printStackTrace();
			}
		}
	}
	
	private List<String> getRequestCommands()
	{
		List<String> requestCommands = clientUI.sendRequestCommands();
		return requestCommands;
	}
	
	private List<Data> getRequestDatas()
	{
		List<Data> requestDatas = clientUI.sendRequestDatas();
		return requestDatas;
	}
	
	//converts List<Data> into List<String> with JSON
	//for sending to server
	private List<String> convertRequestDatas(List<Data> requestDatas)
	{
		List<String> convertedRequestDatas = new ArrayList<>();
		
		for (int i = 0; i < requestDatas.size(); i++)
		{
			String stringData = requestDatas.get(i).accessObjectJson();
			convertedRequestDatas.add(stringData);
		}
		
		return convertedRequestDatas;
	}
	
	//checks if command from server is included in the
	//list of possible update commands
	private boolean checkForUpdate(String command)
	{
		String[] updateCommands = {"updateAllGroups", "updateMessages", "updateUserMessages", "updateAllUsers", "updateMessagedUsers"};
		boolean isUpdate = false;
		
		if (Arrays.asList(updateCommands).contains(command))
		{
			isUpdate = true;
		}
		
		return isUpdate;
	}
	
	/*
	 * When the processResponse and processUpdate methods are called,
	 * they take the command that they are passed and compare it to 
	 * a list of commands for different actions in the UI. 
	 * Depending on the command, they decode the data passed
	 * with the correct object JSON decoding method and pass the
	 * decoded data to the corresponding method to update the UI. 
	 */
	
	private void processResponse(String command, String data)
	{	
		if (command.equals("refreshAllGroups"))
		{
			List<UserGroup> responseData = new ArrayList<>();
			
			if (data.length() > 2)
			{
				responseData = UserGroup.decodeFromServer(data);
			}
			clientUI.handleRefreshAllGroups(responseData);
		}
		else if (command.equals("refreshJoinedGroups"))
		{
			List<UserGroup> responseData = new ArrayList<>();
			
			if (data.length() > 2)
			{
				responseData = UserGroup.decodeFromServer(data);
			}
			clientUI.handleJoinedGroupRefresh(responseData);
		}
		else if (command.equals("validateUser"))
		{
			ServerMessage responseData = ServerMessage.decodeFromServer(data);
			clientUI.validateLogin(responseData);
		}
		else if (command.equals("createUser"))
		{
			ServerMessage responseData = ServerMessage.decodeFromServer(data);
			clientUI.validateCreateUser(responseData);
		}
		else if (command.equals("createGroup"))
		{
			ServerMessage responseData = ServerMessage.decodeFromServer(data);
			clientUI.validateCreateGroup(responseData);
		}
		else if (command.equals("joinGroup"))
		{
			ServerMessage responseData = ServerMessage.decodeFromServer(data);
			clientUI.validateJoinGroup(responseData);
		}
		else if (command.equals("saveMessage"))
		{
			ServerMessage responseData = ServerMessage.decodeFromServer(data);
			clientUI.validateSaveMessage(responseData);
		}
		else if (command.equals("saveUserMessage"))
		{
			ServerMessage responseData = ServerMessage.decodeFromServer(data);
			clientUI.validateSaveUserMessage(responseData);
		}
		else if (command.equals("refreshMessages"))
		{
			List<Message> responseData = new ArrayList<>();
			
			if (data.length() > 2)
			{
				responseData = Message.decodeFromServer(data);
			}
			clientUI.handleMessageRefresh(responseData);
		}
		else if (command.equals("refreshUserMessages"))
		{
			List<Message> responseData = new ArrayList<>();
			
			if (data.length() > 2)
			{
				responseData = Message.decodeFromServer(data);
			}
			clientUI.handleUserMessageRefresh(responseData);
		}
		else if (command.equals("displayAllUsers"))
		{
			List<User> responseData = new ArrayList<>();
			
			if (data.length() > 2)
			{
				responseData = User.decodeFromServer(data);
			}
			clientUI.handleAllUserRefresh(responseData);
		}
		else if (command.equals("displayMessagedUsers"))
		{
			List<UserGroup> responseData = new ArrayList<>();
			
			if (data.length() > 2)
			{
				responseData = UserGroup.decodeFromServer(data);
			}
			clientUI.handleMessagedUserRefresh(responseData);
		}
		else if (command.equals("messageNewUser"))
		{
			ServerMessage responseData = ServerMessage.decodeFromServer(data);
			clientUI.handleMessageNewUser(responseData);
		}
	}
	
	private void processUpdate(String command, String data)
	{
		if (command.equals("updateAllGroups"))
		{
			List<UserGroup> updateData = new ArrayList<>();
			
			if (data.length() > 2)
			{
				updateData = UserGroup.decodeFromServer(data);
			}
			clientUI.handleRefreshAllGroups(updateData);
		}
		else if (command.equals("updateMessages"))
		{
			List<Message> updateData = new ArrayList<>();
			
			if (data.length() > 2)
			{
				updateData = Message.decodeFromServer(data);
			}
			clientUI.handleMessageRefresh(updateData);
		}
		else if (command.equals("updateUserMessages"))
		{
			List<Message> updateData = new ArrayList<>();
			
			if (data.length() > 2)
			{
				updateData = Message.decodeFromServer(data);
			}
			clientUI.handleUserMessageRefresh(updateData);
		}
		else if (command.equals("updateAllUsers"))
		{
			List<User> updateData = new ArrayList<>();
			
			if (data.length() > 2)
			{
				updateData = User.decodeFromServer(data);
			}
			clientUI.handleAllUserRefresh(updateData);
		}
		else if (command.equals("updateMessagedUsers"))
		{
			List<UserGroup> updateData = new ArrayList<>();
			
			if (data.length() > 2)
			{
				updateData = UserGroup.decodeFromServer(data);
			}
			clientUI.handleMessagedUserRefresh(updateData);
		}
	}
	
}
