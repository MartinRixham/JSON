package yirgacheffe.json;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

final class JSONValue
{
	private JSONValue()
	{
	}

	public static boolean getBoolean(JsonValue value)
	{
		if (value instanceof JsonArray || value instanceof JsonObject)
		{
			return true;
		}
		if (value instanceof JsonString)
		{
			return ((JsonString) value).getString().length() > 0;
		}
		else if (value instanceof JsonNumber)
		{
			return ((JsonNumber) value).intValue() != 0;
		}
		else
		{
			return value.getValueType() == JsonValue.ValueType.TRUE;
		}
	}

	public static double getNumber(JsonValue value)
	{
		if (value instanceof JsonNumber)
		{
			return ((JsonNumber) value).doubleValue();
		}
		else
		{
			return Double.NaN;
		}
	}

	public static String getString(JsonValue value)
	{
		if (value == null)
		{
			return "null";
		}
		if (value instanceof JsonString)
		{
			return ((JsonString) value).getString();
		}
		else
		{
			return value.toString();
		}
	}

	public static JSONObject getObject(JsonValue value)
	{
		if (value instanceof JsonObject)
		{
			return new JSONObject((JsonObject) value);
		}
		else
		{
			return new NullJSONObject();
		}
	}

	public static JSONArray getArray(JsonValue value)
	{
		if (value instanceof JsonArray)
		{
			return new JSONArray((JsonArray) value);
		}
		else
		{
			return new NullJSONArray();
		}
	}
}
