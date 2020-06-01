package Client;

/*
 * ClientControl starts the client system.
 * It constructs a new ClientConnection object
 * and calls runClient, which starts the UI and 
 * communication with the server.
 */

public class ClientControl 
{

	public static void main(String[] args) 
	{
		// Creates and starts a new client.
		ClientConnection client = new ClientConnection();
		client.runClient();
	}

}
