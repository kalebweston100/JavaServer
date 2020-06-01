package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;

/*
 * ServerConnection is the main part of the server.
 * Only one ServerConnection object is created, but
 * it creates a UserSocket object for each new client
 * that connects to the ServerSocket.
 * It also has a LinkedList of UserSockets that contains every
 * new connected client so that it can implement instant updates
 * between clients.
 */

public class ServerConnection
{	
	private boolean running = true;
	//List that stores all UserSockets currently active
	private static List<UserSocket> activeUsers = new LinkedList<>();
	
	public void runServer()
	{
		try
		(
			ServerSocket serverSocket = new ServerSocket(80);
		)
		{
			//creates a UserSocket for any new client that connects,
			//adds them to activeUsers, and starts a new Thread for them
			while (running)
			{
				System.out.println("Server Socket Running");
				UserSocket userSocket = new UserSocket(serverSocket.accept());
				activeUsers.add(userSocket);
				userSocket.thread.start();
			}
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("Connection was lost");
		}
		
	}
	
	/*
	 * Instant updates are implemented through public static
	 * methods in the ServerConnection class. These methods 
	 * are called from the ServerProtocol class of the client
	 * that performs an action that requires updates for other
	 * clients. The ServerConnection class then loops through
	 * the activeUsers List and gets the ServerProtocols
	 * of all relevant clients. A method in these ServerProtocols
	 * is called which triggers their UserSocket to send update
	 * data to its client UI to render the new data.
	 */
	
	//refreshes the UI all groups page when
	//any client creates a new group
	public static void refreshGroupCreated()
	{
		for (UserSocket user : activeUsers)
		{
			ServerProtocol userProtocol = user.sp;
			userProtocol.sendGroupRefresh();
		}
	}
	
	//refreshes the UI group message page if the client is
	//in the list of user ids that belong to the group,
	//and the user is currently viewing that group
	public static void refreshMessages(List<Integer> userIds, int groupId)
	{
		for (UserSocket user : activeUsers)
		{
			ServerProtocol userProtocol = user.sp;
			
			if (userIds.contains(userProtocol.userId))
			{
				if (groupId == userProtocol.currentGroupId)
				{
					userProtocol.sendMessageRefresh(groupId);
				}
			}
		}
	}
	
	//similar action to the refreshMessages method, but is used
	//for the private messaging system
	public static void refreshUserMessages(List<Integer> userIds, int groupId)
	{
		for (UserSocket user : activeUsers)
		{
			ServerProtocol userProtocol = user.sp;
			
			if (userIds.contains(userProtocol.userId))
			{
				if (groupId == userProtocol.currentGroupId)
				{
					userProtocol.sendUserMessageRefresh(groupId);
				}
			}
		}
	}
}

