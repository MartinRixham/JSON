package yirgacheffe.json;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import java.io.StringReader;

public class JSONObject implements JsonData
{
	private JsonObject json;

	private JsonObjectBuilder builder;

	public JSONObject()
	{
		this.builder = Json.createObjectBuilder();
	}

	public JSONObject(String data)
	{
		JsonReader reader = Json.createReader(new StringReader(data));

		this.json = reader.readObject();
	}

	JSONObject(JsonObject json)
	{
		this.json = json;
	}

	public boolean has(String property)
	{
		if (this.json == null)
		{
			JsonObject json = this.builder.build();

			return json.containsKey(property);
		}
		else
		{
			return this.json.containsKey(property);
		}
	}

	public boolean getBoolean(String property)
	{
		JsonObject json = this.json;

		if (json == null)
		{
			json = this.toReadMode();
		}

		if (json.containsKey(property))
		{
			return JSONValue.getBoolean(json.get(property));
		}
		else
		{
			return false;
		}
	}

	public double getNumber(String property)
	{
		JsonObject json = this.json;

		if (json == null)
		{
			json = this.toReadMode();
		}

		if (json.containsKey(property))
		{
			return JSONValue.getNumber(json.get(property));
		}
		else
		{
			return Double.NaN;
		}
	}

	public String getString(String property)
	{
		JsonObject json = this.json;

		if (json == null)
		{
			json = this.toReadMode();
		}

		if (json.containsKey(property))
		{
			return JSONValue.getString(json.get(property));
		}
		else
		{
			return "";
		}
	}

	public JSONObject getObject(String property)
	{
		JsonObject json = this.json;

		if (json == null)
		{
			json = this.toReadMode();
		}

		if (json.containsKey(property))
		{
			return JSONValue.getObject(json.get(property));
		}
		else
		{
			return new NullJSONObject();
		}
	}

	public JSONArray getArray(String property)
	{
		JsonObject json = this.json;

		if (json == null)
		{
			json = this.toReadMode();
		}

		if (json.containsKey(property))
		{
			return JSONValue.getArray(json.get(property));
		}
		else
		{
			return new NullJSONArray();
		}
	}

	private JsonObject toReadMode()
	{
		JsonObject json = this.builder.build();
		this.json = json;
		this.builder = null;

		return json;
	}

	public void put(String property, JSONArray value)
	{
		JsonObjectBuilder builder = this.builder;

		if (builder == null)
		{
			builder = this.toWriteMode();
		}

		builder.add(property, value.toJson());
	}

	public void put(String property, JSONObject value)
	{
		JsonObjectBuilder builder = this.builder;

		if (builder == null)
		{
			builder = this.toWriteMode();
		}

		builder.add(property, value.toJson());
	}

	public void put(String property, String value)
	{
		JsonObjectBuilder builder = this.builder;

		if (builder == null)
		{
			builder = this.toWriteMode();
		}

		builder.add(property, value);
	}

	public void put(String property, double value)
	{
		JsonObjectBuilder builder = this.builder;

		if (builder == null)
		{
			builder = this.toWriteMode();
		}

		builder.add(property, value);
	}

	public void put(String property, long value)
	{
		JsonObjectBuilder builder = this.builder;

		if (builder == null)
		{
			builder = this.toWriteMode();
		}

		builder.add(property, value);
	}

	public void put(String property, boolean value)
	{
		JsonObjectBuilder builder = this.builder;

		if (builder == null)
		{
			builder = this.toWriteMode();
		}

		builder.add(property, value);
	}

	private JsonObjectBuilder toWriteMode()
	{
		JsonObjectBuilder builder = Json.createObjectBuilder(this.json);
		this.builder = builder;
		this.json = null;

		return builder;
	}

	JsonObjectBuilder toJson()
	{
		if (this.builder == null)
		{
			return Json.createObjectBuilder(this.json);
		}
		else
		{
			return this.builder;
		}
	}

	@Override
	public String toString()
	{
		if (this.builder == null)
		{
			return this.json.toString();
		}
		else
		{
			return this.builder.build().toString();
		}
	}
}
