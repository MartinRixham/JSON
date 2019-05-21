package yirgacheffe.json;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import yirgacheffe.parser.JSONParser;
import yirgacheffe.parser.JSONLexer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JSONObject implements JsonData
{
	private Map<String, Object> properties = new HashMap<>();

	public JSONObject()
	{
	}

	public JSONObject(String data)
	{
		CharStream charStream = CharStreams.fromString(data);
		JSONLexer lexer = new JSONLexer(charStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ParseErrorListener errorListener = new ParseErrorListener();

		JSONParser parser = new JSONParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(errorListener);

		JSONParser.ObjectContext context = parser.object();

		if (errorListener.hasError())
		{
			String message = Arrays.toString(errorListener.getErrors());

			throw new JSONException(message.substring(1, message.length() - 1));
		}

		this.parse(context);
	}

	void parse(JSONParser.ObjectContext	context)
	{
		Object[] properties = context.property().toArray();

		for (Object propertyContext: properties)
		{
			this.parseProperty((JSONParser.PropertyContext) propertyContext);
		}
	}

	private void parseProperty(JSONParser.PropertyContext context)
	{
		String key = context.STRING().getText();
		String keyString = key.substring(1, key.length() - 1);
		JSONParser.ValueContext valueContext = context.value();
		String valueString = valueContext.getText();

		Object value = JSONValue.getValue(valueContext, valueString);

		this.properties.put(keyString, value);
	}

	public boolean has(String property)
	{
		return this.properties.containsKey(property);
	}

	public boolean getBoolean(String property)
	{
		if (this.properties.containsKey(property))
		{
			Object value = this.properties.get(property);

			return PropertyValue.getBoolean(value);
		}
		else
		{
			return false;
		}
	}

	public double getNumber(String property)
	{
		if (this.properties.containsKey(property))
		{
			Object value = this.properties.get(property);

			return PropertyValue.getNumber(value);
		}
		else
		{
			return Double.NaN;
		}
	}

	public String getString(String property)
	{
		if (this.properties.containsKey(property))
		{
			Object value = this.properties.get(property);

			return PropertyValue.getString(value);
		}
		else
		{
			return "";
		}
	}

	public JSONObject getObject(String property)
	{
		if (this.properties.containsKey(property))
		{
			Object value = this.properties.get(property);

			if (value instanceof JSONObject)
			{
				return (JSONObject) value;
			}
			else
			{
				return new NullJSONObject();
			}
		}
		else
		{
			return new NullJSONObject();
		}
	}

	public JSONArray getArray(String property)
	{
		if (this.properties.containsKey(property))
		{
			Object value = this.properties.get(property);

			if (value instanceof JSONArray)
			{
				return (JSONArray) value;
			}
			else
			{
				return new NullJSONArray();
			}
		}
		else
		{
			return new NullJSONArray();
		}
	}

	public void put(String property, JSONArray value)
	{
		this.properties.put(property, value);
	}

	public void put(String property, JSONObject value)
	{
		this.properties.put(property, value);
	}

	public void put(String property, String value)
	{
		this.properties.put(property, value);
	}

	public void put(String property, double value)
	{
		this.properties.put(property, value);
	}

	public void put(String property, long value)
	{
		this.properties.put(property, value);
	}

	public void put(String property, boolean value)
	{
		this.properties.put(property, value);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder("{");
		Object[] keys = this.properties.keySet().toArray();

		for (int i = 0; i < keys.length; i++)
		{
			builder.append('"');
			builder.append(keys[i]);
			builder.append("\":");

			this.appendValueString(builder, this.properties.get(keys[i]));

			if (i < keys.length - 1)
			{
				builder.append(',');
			}
		}

		builder.append('}');

		return builder.toString();
	}

	private void appendValueString(StringBuilder builder, Object value)
	{
		if (value instanceof String)
		{
			builder.append('"');
			builder.append(value.toString());
			builder.append('"');
		}
		else if (value == null)
		{
			builder.append("null");
		}
		else
		{
			builder.append(value.toString());
		}
	}
}
