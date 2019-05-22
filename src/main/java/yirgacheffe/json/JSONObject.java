package yirgacheffe.json;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonStructure;
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
		JsonObject json = this.json == null ? this.builder.build() : this.json;

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
		JsonObject json = this.json == null ? this.builder.build() : this.json;

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
		JsonObject json = this.json == null ? this.builder.build() : this.json;

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
		JsonObject json = this.json == null ? this.builder.build() : this.json;

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
		JsonObject json = this.json == null ? this.builder.build() : this.json;

		if (json.containsKey(property))
		{
			return JSONValue.getArray(json.get(property));
		}
		else
		{
			return new NullJSONArray();
		}
	}

	public void put(String property, JSONArray value)
	{
		if (this.builder == null)
		{
			JsonObjectBuilder builder = Json.createObjectBuilder(this.json);
			builder.add(property, value.toJson());

			this.json = builder.build();
		}
		else
		{
			this.builder.add(property, value.toJson());
		}
	}

	public void put(String property, JSONObject value)
	{
		if (this.builder == null)
		{
			JsonObjectBuilder builder = Json.createObjectBuilder(this.json);
			builder.add(property, value.toJson());

			this.json = builder.build();
		}
		else
		{
			this.builder.add(property, value.toJson());
		}
	}

	public void put(String property, String value)
	{
		if (this.builder == null)
		{
			JsonObjectBuilder builder = Json.createObjectBuilder(this.json);
			builder.add(property, value);

			this.json = builder.build();
		}
		else
		{
			this.builder.add(property, value);
		}
	}

	public void put(String property, double value)
	{
		if (this.builder == null)
		{
			JsonObjectBuilder builder = Json.createObjectBuilder(this.json);
			builder.add(property, value);

			this.json = builder.build();
		}
		else
		{
			this.builder.add(property, value);
		}
	}

	public void put(String property, long value)
	{
		if (this.builder == null)
		{
			JsonObjectBuilder builder = Json.createObjectBuilder(this.json);
			builder.add(property, value);

			this.json = builder.build();
		}
		else
		{
			this.builder.add(property, value);
		}
	}

	public void put(String property, boolean value)
	{
		if (this.builder == null)
		{
			JsonObjectBuilder builder = Json.createObjectBuilder(this.json);
			builder.add(property, value);

			this.json = builder.build();
		}
		else
		{
			this.builder.add(property, value);
		}
	}

	JsonStructure toJson()
	{
		if (this.json == null)
		{
			return this.builder.build();
		}
		else
		{
			return this.json;
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
