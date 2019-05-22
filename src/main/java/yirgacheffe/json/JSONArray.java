package yirgacheffe.json;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonReader;
import javax.json.JsonStructure;
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
		JsonArray json = this.json == null ? this.builder.build() : this.json;

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
		JsonArray json = this.json == null ? this.builder.build() : this.json;

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
		JsonArray json = this.json == null ? this.builder.build() : this.json;

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
		JsonArray json = this.json == null ? this.builder.build() : this.json;

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
		JsonArray json = this.json == null ? this.builder.build() : this.json;

		if (0 <= index &&  index < json.size())
		{
			return JSONValue.getArray(json.get(index));
		}
		else
		{
			return new NullJSONArray();
		}
	}

	public void put(JSONArray value)
	{
		if (this.builder == null)
		{
			JsonArrayBuilder builder = Json.createArrayBuilder(this.json);
			builder.add(value.toJson());

			this.json = builder.build();
		}
		else
		{
			this.builder.add(value.toJson());
		}
	}

	public void put(JSONObject value)
	{
		if (this.builder == null)
		{
			JsonArrayBuilder builder = Json.createArrayBuilder(this.json);
			builder.add(value.toJson());

			this.json = builder.build();
		}
		else
		{
			this.builder.add(value.toJson());
		}
	}

	public void put(String value)
	{
		if (this.builder == null)
		{
			JsonArrayBuilder builder = Json.createArrayBuilder(this.json);
			builder.add(value);

			this.json = builder.build();
		}
		else
		{
			this.builder.add(value);
		}
	}

	public void put(double value)
	{
		if (this.builder == null)
		{
			JsonArrayBuilder builder = Json.createArrayBuilder(this.json);
			builder.add(value);

			this.json = builder.build();
		}
		else
		{
			this.builder.add(value);
		}
	}

	public void put(long value)
	{
		if (this.builder == null)
		{
			JsonArrayBuilder builder = Json.createArrayBuilder(this.json);
			builder.add(value);

			this.json = builder.build();
		}
		else
		{
			this.builder.add(value);
		}
	}

	public void put(boolean value)
	{
		if (this.builder == null)
		{
			JsonArrayBuilder builder = Json.createArrayBuilder(this.json);
			builder.add(value);

			this.json = builder.build();
		}
		else
		{
			this.builder.add(value);
		}
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
