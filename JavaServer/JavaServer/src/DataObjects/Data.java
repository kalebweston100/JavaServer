package DataObjects;

/*
 * Data is the interface implemented by
 * every object in the data layer.
 * This allows for polymorphism, which
 * is a vital function in the current
 * design of the system.
 * It requires every object to implement
 * accessObjectJson because this method
 * is used to encode and send data both 
 * ways for communication.
 */

public interface Data
{
	public String accessObjectJson();
}
