package JsonInterface;
import java.util.ArrayList;
import java.util.List;

/*
 * JsonObject is used to encode data objects into a JSON String.
 * When the accessObjectJson method is called in a data object,
 * it constructs a JsonObject and adds all the object's data.
 * The JsonObject's accessJson method is then called, and the data
 * object returns the JSON String returned by the JsonObject.
 */

public class JsonObject 
{
	List<String> json = new ArrayList<>();
	
	public JsonObject()
	{
		
	}

	//adds an int to the JsonObject with a key
	//different from adding an item because
	//it has no quotes around it
	public void addInt(String key, int intValue)
	{
		String value = Integer.toString(intValue);
		String json_pair = "\"" + key + "\" :" + value; 
		json.add(json_pair);
	}
	
	//adds an item to the JsonObject with a key
	public void addItem(String key, String value)
	{
		String json_pair = "\"" + key + "\" :\"" + value + "\""; 
		json.add(json_pair);
	}
	
	//gets an item at an index from the json  ArrayList
	public String getItem(int index)
	{
		String json_pair = json.get(index);
		return json_pair;
	}
	
	//accesses all ints/items in the JsonObject with
	//their keys and encodes them into a Json String
	public String accessJson()
	{
		int json_length = json.size();
		String json_string = "{";
		
		for (int i = 0; i < json_length; i++)
		{
			String json_pair = json.get(i);
			
			if (i < json_length - 1)
			{
				json_string = json_string + json_pair + ",";
			}
			else
			{
				json_string = json_string + json_pair;
			}
		}
		
		json_string = json_string + "}";
		
		return json_string;
	}
}








