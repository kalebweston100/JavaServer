package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import DataObjects.User;

/*
 * ClientConnection creates a new socket
 * and retrieves the input and output 
 * streams from the socket. It then constructs
 * a new ClientUI and passes the object into
 * the constructor of a new ClientProtocol
 * in order to be rendered. The runConnection
 * method of the ClientProtocol object is then
 * called, and the program starts.
 */

public class ClientConnection 
{	
	//method that initiates client activity
	public void runClient()
	{
		try 
		(
			// Create a new socket connection to the server,
			// and get the input and output streams for the socket.
			Socket socket = new Socket("127.0.0.1", 80);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		)
		{	
			// Any type of UI that extends AbstractUI can be constructed
			// because ClientProtocol takes an AbstractUI object
			// in its constructor
			UI clientUI = new UI();
			
			// Pass the input and output streams, and a UI into 
			// the ClientProtocol constructor
			ClientProtocol cp = new ClientProtocol(in, out, clientUI);
			cp.runConnection();
			
			// Closes the socket after the program terminates.
			socket.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
