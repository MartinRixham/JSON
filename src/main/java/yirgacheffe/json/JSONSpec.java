package yirgacheffe.json;

import jakarta.json.Json;
import jakarta.json.JsonException;
import jakarta.json.JsonReader;
import jakarta.json.JsonStructure;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class JSONSpec
{
	private Map<Integer, String> cache = new HashMap<>();

	public boolean isValid(String data)
	{
		if (data == null)
		{
			return false;
		}

		int key = data.hashCode();

		if (this.cache.containsKey(key))
		{
			return this.cache.get(key).length() == 0;
		}

		try
		{
			this.parseJson(data);
		}
		catch (JsonException e)
		{
			this.cache.put(key, e.getMessage());

			return false;
		}

		this.cache.put(key, "");

		return true;
	}

	public String getError(String data)
	{
		if (data == null)
		{
			data = "";
		}

		int key = data.hashCode();

		if (this.cache.containsKey(key))
		{
			return this.cache.get(key);
		}

		try
		{
			this.parseJson(data);
		}
		catch (JsonException e)
		{
			String message = e.getMessage();

			this.cache.put(key, message);

			return message;
		}

		this.cache.put(key, "");

		return "";
	}

	private void parseJson(String data)
	{
		JsonReader reader = Json.createReader(new StringReader(data));
		JsonStructure json = reader.read();
	}
}
