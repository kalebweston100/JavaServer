package Server;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import DataObjects.Data;
import DataObjects.Message;
import DataObjects.ServerMessage;
import DataObjects.User;
import DataObjects.UserGroup;
import JsonInterface.JsonControl;

/*
 * ServerControl starts the server program
 * by creating a ServerConnection object 
 * and calling the runServer method.
 */

public class ServerControl 
{
	public static void main(String[] args) 
	{
		ServerConnection server = new ServerConnection();
		server.runServer();
	}
}
