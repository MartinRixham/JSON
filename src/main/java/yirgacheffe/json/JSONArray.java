package yirgacheffe.json;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonReader;
import java.io.StringReader;

public class JSONArray implements JsonData
{
	private JsonArray json;

	private JsonArrayBuilder builder;

	public JSONArray()
	{
		this.builder = Json.createArrayBuilder();
	}

	public JSONArray(String data)
	{
		JsonReader reader = Json.createReader(new StringReader(data));

		this.json = reader.readArray();
	}

	JSONArray(JsonArray json)
	{
		this.json = json;
	}

	public boolean getBoolean(int index)
	{
		JsonArray json = this.json;

		if (json == null)
		{
			json = this.toReadMode();
		}

		if (0 <= index &&  index < json.size())
		{
			return JSONValue.getBoolean(json.get(index));
		}
		else
		{
			return false;
		}
	}

	public double getNumber(int index)
	{
		JsonArray json = this.json;

		if (json == null)
		{
			json = this.toReadMode();
		}

		if (0 <= index &&  index < json.size())
		{
			return JSONValue.getNumber(json.get(index));
		}
		else
		{
			return Double.NaN;
		}
	}

	public String getString(int index)
	{
		JsonArray json = this.json;

		if (json == null)
		{
			json = this.toReadMode();
		}

		if (0 <= index &&  index < json.size())
		{
			return JSONValue.getString(json.get(index));
		}
		else
		{
			return "";
		}
	}

	public JSONObject getObject(int index)
	{
		JsonArray json = this.json;

		if (json == null)
		{
			json = this.toReadMode();
		}

		if (0 <= index &&  index < json.size())
		{
			return JSONValue.getObject(json.get(index));
		}
		else
		{
			return new NullJSONObject();
		}
	}

	public JSONArray getArray(int index)
	{
		JsonArray json = this.json;

		if (json == null)
		{
			json = this.toReadMode();
		}

		if (0 <= index &&  index < json.size())
		{
			return JSONValue.getArray(json.get(index));
		}
		else
		{
			return new NullJSONArray();
		}
	}

	private JsonArray toReadMode()
	{
		JsonArray json = this.builder.build();
		this.json = json;
		this.builder = null;

		return json;
	}

	public void put(JSONArray value)
	{
		JsonArrayBuilder builder = this.builder;

		if (builder == null)
		{
			builder = this.toWriteMode();
		}

		builder.add(value.toJson());
	}

	public void put(JSONObject value)
	{
		JsonArrayBuilder builder = this.builder;

		if (builder == null)
		{
			builder = this.toWriteMode();
		}

		builder.add(value.toJson());
	}

	public void put(String value)
	{
		JsonArrayBuilder builder = this.builder;

		if (builder == null)
		{
			builder = this.toWriteMode();
		}

		builder.add(value);
	}

	public void put(double value)
	{
		JsonArrayBuilder builder = this.builder;

		if (builder == null)
		{
			builder = this.toWriteMode();
		}

		builder.add(value);
	}

	public void put(long value)
	{
		JsonArrayBuilder builder = this.builder;

		if (builder == null)
		{
			builder = this.toWriteMode();
		}

		builder.add(value);
	}

	public void put(boolean value)
	{
		JsonArrayBuilder builder = this.builder;

		if (builder == null)
		{
			builder = this.toWriteMode();
		}

		builder.add(value);
	}

	private JsonArrayBuilder toWriteMode()
	{
		JsonArrayBuilder builder = Json.createArrayBuilder(this.json);
		this.builder = builder;
		this.json = null;

		return builder;
	}

	public int length()
	{
		if (this.json == null)
		{
			return this.builder.build().size();
		}
		else
		{
			return this.json.size();
		}
	}

	JsonArrayBuilder toJson()
	{
		if (this.builder == null)
		{
			return Json.createArrayBuilder(this.json);
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
