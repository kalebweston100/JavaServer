package DataObjects;

import JsonInterface.JsonControl;
import JsonInterface.JsonObject;

/*
 * ClientMessage is an object that 
 * is only used for communication
 * and never saved in the database.
 * This object is always sent from
 * the client to the server with an 
 * id from one of the objects
 * in the data layer.
 */

public class ClientMessage implements Data
{
	private int sendId = 0;
	
	public ClientMessage()
	{
		
	}
	
	public ClientMessage(int sendId)
	{
		this.sendId = sendId;
	}
	
	public int getSendId()
	{
		return sendId;
	}
	
	//see explanation of method in Data
	public String accessObjectJson()
	{
		JsonObject jsonObject = new JsonObject();
		
		int sendId = getSendId();
		jsonObject.addInt("sendId", sendId);

		String json = jsonObject.accessJson();
		return json;
	}
	
	//decodes Json sent from the client into a ClientMessage
	public static ClientMessage decodeFromClient(String json)
	{
		String[] data = JsonControl.decodeObject(json);
		int sendId = Integer.parseInt(data[0]);
		ClientMessage clientMessage = new ClientMessage(sendId);
		return clientMessage;
	}
}
