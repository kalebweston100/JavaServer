package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import DataObjects.User;
import Database.MySQLConnection;

/*
 * UserSocket objects are created
 * by the ServerConnection class for
 * each new client that connects.
 * When created, it takes the socket it
 * was passed by the ServerConnection 
 * ServerSocket, creates a new thread to 
 * run the UserSocket's separate program on,
 * and creates a new MySQLConnection object.
 * It then gets the input and output streams
 * from the socket and passes the streams,
 * the thread, and the MySQLConnection object
 * to a ServerProtocol object which will 
 * control the connection to the client.
 */

public class UserSocket implements Runnable
{
	Thread thread = null;
	private Socket socket = null;
	public ServerProtocol sp = null;
	private MySQLConnection mysqlc = null;
	
	//gets the socket passed from ServerConnection,
	//the current Thread, and creates a new MySQLConnection
	public UserSocket(Socket socket)
	{
		this.socket = socket;
		thread = new Thread(this);
		mysqlc = new MySQLConnection();
	}
	
	//gets the socket's input and output streams, and passes them
	//along with the database connection into a new ServerProtocol.
	//Starts the protocol after creating it
	public void run()
	{	
		try 
		(
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		)
		{
			sp = new ServerProtocol(in, out, thread, mysqlc);
			sp.controlConnection();
			
			System.out.println("socket closed");
			socket.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
