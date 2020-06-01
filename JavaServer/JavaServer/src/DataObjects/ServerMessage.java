package DataObjects;

import JsonInterface.JsonControl;
import JsonInterface.JsonObject;

/*
 * ServerMessage is similar to ClientMessage
 * as it is only used for communication.
 * However, it is used for communication
 * in the opposite direction from ClientMessage,
 * as it is always sent from the server
 * to the client. It always stores a 
 * message as a String sent to the client.
 * This String either tells the client
 * the success or failure of a requested
 * action, or is a command to update
 * its UI.
 */

public class ServerMessage implements Data
{
	private String message = "";
	
	public ServerMessage(String message)
	{
		this.message = message;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	//see explanation of method in Data
	@Override
	public String accessObjectJson()
	{
		JsonObject jsonObject = new JsonObject();
		
		String message = getMessage();
		jsonObject.addItem("message", message);
		
		String json = jsonObject.accessJson();
		return json;
	}
	
	//decodes Json sent from server into ServerMessage 
	public static ServerMessage decodeFromServer(String json)
	{
		String[] data = JsonControl.decodeObject(json);
		String message = data[0];
		ServerMessage serverMessage = new ServerMessage(message);
		return serverMessage;
	}
}
