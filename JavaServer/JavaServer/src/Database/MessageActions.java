package Database;

import java.util.List;

import DataObjects.Message;

/*
 * MessageActions is the interface
 * that has all the methods related
 * to Message database actions in it.
 */

public interface MessageActions 
{
	public boolean saveMessage(Message message);
	public List<Message> getMessages(int groupId);
}
