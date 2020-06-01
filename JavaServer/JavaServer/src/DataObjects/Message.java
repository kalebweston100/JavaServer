package DataObjects;
import java.util.ArrayList;
import java.util.List;

import JsonInterface.JsonControl;
import JsonInterface.JsonObject;

/*
 * Message is the object that
 * stores the data for any messages
 * sent from a client. This object 
 * is used to send client message 
 * data to the server to save, and
 * to return message data to the client
 * to display.
 */

public class Message implements Data
{
	private int senderId = 0;
	private int groupId = 0;
	private String senderUsername = "";
	private String messageContent = "";
	
	public Message(int groupId, String messageContent)
	{
		this.groupId = groupId;
		this.messageContent = messageContent;
	}
	
	//constructor for server to return message
	//to be displayed when group is refreshed
	//no need for group id because messages are called
	//with group id each time a group is changed
	public Message(String senderUsername, String messageContent)
	{
		this.senderUsername = senderUsername;
		this.messageContent = messageContent;
	}
	
	public int getSenderId()
	{
		return senderId;
	}
	
	//set sender id when server gets it from thread name
	public void setSenderId(int senderId)
	{
		this.senderId = senderId;
	}
	
	public int getGroupId()
	{
		return groupId;
	}
	
	public String getMessageContent()
	{
		return messageContent;
	}
	
	public String getSenderUsername()
	{
		return senderUsername;
	}
	
	//check if client constructor or 
	//server constructor was used
	//and create json accordingly
	public String accessObjectJson()
	{
		JsonObject jsonObject = new JsonObject();
		int groupId = getGroupId();
		
		if (groupId != 0)
		{
			jsonObject.addInt("groupId", groupId);
		}
		else
		{
			String senderUsername = getSenderUsername();
			jsonObject.addItem("senderUsername", senderUsername);
		}
		
		String messageContent = getMessageContent();
		jsonObject.addItem("messageContent", messageContent);

		String json = jsonObject.accessJson();
		return json;
	}
	
	//decodes Json sent from the client into a Message
	//used to save Message
	public static Message decodeFromClient(String json)
	{
		String[] data = JsonControl.decodeObject(json);
		int groupId = Integer.parseInt(data[0]);
		String messageContent = data[1];
		
		Message message = new Message(groupId, messageContent);
		return message;
	}
	
	//decodes Json sent from the server into a List<Message>
	//used to display queried Message
	public static List<Message> decodeFromServer(String json)
	{
		List<String[]> objects = JsonControl.decodeObjectSet(json);
		List<Message> messages = new ArrayList<>();
		
		for (String[] object : objects)
		{
			String senderUsername = object[0];
			String messageContent = object[1];
			Message message = new Message(senderUsername, messageContent);
			messages.add(message);
		}
		
		return messages;
	}
	
}
