package Database;

import java.util.List;

import DataObjects.User;

/*
 * UserActions is an interface that
 * is similar to MessageActions.
 * It contains all the methods
 * required for User database actions.
 */

public interface UserActions 
{
	public boolean createUser(User user);
	public User checkCredentials(User user);
	public List<User> getAllUsers(int userId);
}
