package Database;

import java.util.List;

import DataObjects.User;
import DataObjects.UserGroup;

/*
 * UserGroupActions is another interface
 * that has all the methods for UserGroup
 * database updates. Unlike the other DAO
 * interfaces, it has more complicated
 * actions. This is due to the complex
 * nature of the private messaging system 
 * that still relies on the UserGroup object.
 */

public interface UserGroupActions 
{
	public boolean createGroup(UserGroup group);
	public boolean joinGroup(int groupId, int userId);
	public List<UserGroup> getAllGroups(int userId);
	public List<UserGroup> getJoinedGroups(int userId);
	public UserGroup createPrivateMessageGroup(int currentUserId, User otherUser);
	public List<Integer> getGroupUserIds(int groupId);
	public List<UserGroup> getPrivateMessageGroups(int userId);
}
