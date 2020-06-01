package JsonInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import DataObjects.Data;
import DataObjects.Message;
import DataObjects.ServerMessage;
import DataObjects.User;
import DataObjects.UserGroup;

/*
 * JsonControl holds public static methods that
 * are related to basic JSON encoding and decoding
 * functions. The methods in JsonControl can all 
 * be used for any object type, so they are 
 * kept in a separate publicly accessible class.
 * JsonControl still relies on the accessObjectJson
 * method overridden in each object because each separate
 * object has different data and its own specific way
 * to encode that data.
 */

public class JsonControl 
{	
	//splits json into individual objects
	public static String[] splitObjectSet(String json)
	{
		int jsonLength = json.length();
		String strippedJson = json.substring(1, jsonLength - 1);
		String[] jsonItems = strippedJson.split("}");
		
		for (int i = 0; i < jsonItems.length; i++)
		{
			jsonItems[i] = jsonItems[i] + "}";
		}
	
		return jsonItems;
	}
	
	//splits object into data items
	//only used for single object json decoding
	public static String[] decodeObject(String object)
	{
		int objectLength = object.length();
		String cleanObject = object.substring(1, objectLength - 1);
		String[] pairs = cleanObject.split(",");
		int dataLength = pairs.length;
		String[] data = new String[dataLength];
		
		int indexCounter = 0;
		
		for (String pair : pairs)
		{
			String[] splitPair = pair.split(":");
			int itemLength = splitPair[1].length();
			String item = splitPair[1];
			
			if (item.contains("\""))
			{
				item = splitPair[1].substring(1, itemLength - 1);
			}
			
			data[indexCounter] = item;
			indexCounter += 1;
		}
		
		return data;	
	}
	
	//used to decode a Json String with multiple objects in it
	public static List<String[]> decodeObjectSet(String json)
	{
		List<String[]> data = new ArrayList<>();
		String[] jsonObjects = splitObjectSet(json);
		
		for (String jsonObject : jsonObjects)
		{
			String[] objectData = decodeObject(jsonObject);
			data.add(objectData);
		}
		
		return data;
	}
	
	//encodes a List of any of the DataObjects into a Json String
	public static String encodeObjectSet(List<? extends Data> objects)
	{
		String json = "[";
		int objectNum = objects.size();
		int counter = 0;
		
		for (Data object : objects)
		{
			json += object.accessObjectJson();
			counter += 1;
			
			if (counter < objectNum)
			{
				json += ",";
			}
		}
		
		json += "]";
		
		return json;
	}
}
