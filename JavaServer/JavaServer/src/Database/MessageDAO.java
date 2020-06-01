package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DataObjects.Message;

/*
 * MessageDAO is the concrete implementation
 * of MessageActions, and contains all the 
 * methods in the interface. It also contains
 * all the methods related to formatting data
 * returned from the MySQL queries into the
 * data types requested by its MessageActions
 * methods.
 */

public class MessageDAO implements MessageActions
{
	private Connection connection = null;
	
	public MessageDAO(MySQLConnection mysqlc)
	{
		this.connection = mysqlc.getConnection();
	}
	
	//inserts a new Message into the database
	@Override
	public boolean saveMessage(Message message)
	{
		String senderId = Integer.toString(message.getSenderId());
		String groupId = Integer.toString(message.getGroupId());
		String messageContent = message.getMessageContent();
		
		boolean actionSuccess = false;
		
		try
		{
			String query = "INSERT INTO Message (senderId, groupId, messageContent)"
						 + "VALUES ('" + senderId + "', '" + groupId + "', '" + messageContent + "')";
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
	
	//retrieves Messages for a certain UserGroup formatted into a List<Message>
	@Override
	public List<Message> getMessages(int groupId)
	{
		List<String[]> messageResults = queryMessages(groupId);
		String[] senderUsernames = getSenderUsernames(groupId);
		List<Message> messages = constructMessagesFromDatabase(messageResults, senderUsernames);
		return messages;
	}
	
	//queries the database for all Messages from a certain UserGroup
	private List<String[]> queryMessages(int groupId)
	{
		String queryId = Integer.toString(groupId);
		
		List<String[]> data = new ArrayList<>();
		
		try
		{
			String query = "SELECT * FROM Message "
						 + "WHERE groupId='" + queryId + "'";
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
	
	//gets the usernames of the Users who sent Messages in a UserGroup
	private String[] getSenderUsernames(int groupId)
	{
		List<String[]> messageData = queryMessages(groupId);
		String[] senderUsernames = new String[messageData.size()];
		
		for (int i = 0; i < messageData.size(); i++)
		{
			String[] message = messageData.get(i);
			String senderId = message[1];
			/*
			String messageId = message[0];
			String groupId = message[2];
			String messageContent = message[3];
			*/
			try
			{
				String query = "SELECT username FROM User "
							 + "WHERE userId='" + senderId + "'";
				Statement statement = connection.createStatement();
				ResultSet results = statement.executeQuery(query);
				List<String[]> data = MySQLConnection.queryToList(results);
				String senderUsername = data.get(0)[0];
				
				senderUsernames[i] = senderUsername;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		return senderUsernames;
	}
	
	//constructs a formatted List<Message> from raw query data
	//for Message content and User usernames to display
	private List<Message> constructMessagesFromDatabase(List<String[]> messageResults, String[] senderUsernames)
	{
		List<Message> messages = new ArrayList<>();
		
		for (int i = 0; i < messageResults.size(); i++)
		{
			String[] result = messageResults.get(i);
			String senderUsername = senderUsernames[i];
			String messageContent = result[3];
			/*
			String messageId = result[0];
			String senderId = result[1];
			String groupId = result[2];
			*/
			Message message = new Message(senderUsername, messageContent);
			messages.add(message);
		}
		
		return messages;
	} 
}
