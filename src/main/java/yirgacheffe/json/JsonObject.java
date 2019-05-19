package yirgacheffe.json;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import yirgacheffe.parser.JSONParser;
import yirgacheffe.parser.JSONLexer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JsonObject implements JsonData
{
	private Map<String, Object> properties = new HashMap<>();

	public JsonObject()
	{
	}

	public JsonObject(String data)
	{
		CharStream charStream = CharStreams.fromString(data);
		JSONLexer lexer = new JSONLexer(charStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ParseErrorListener errorListener = new ParseErrorListener();

		JSONParser parser = new JSONParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(errorListener);

		JSONParser.ObjectContext context = parser.object();

		this.parse(context);

		if (errorListener.hasError())
		{
			String message = Arrays.toString(errorListener.getErrors());

			throw new JsonException(message.substring(1, message.length() - 1));
		}
	}

	void parse(JSONParser.ObjectContext	context)
	{
		for (JSONParser.PropertyContext propertyContext: context.property())
		{
			this.parseProperty(propertyContext);
		}
	}

	private void parseProperty(JSONParser.PropertyContext context)
	{
		String key = context.STRING().getText();
		String keyString = key.substring(1, key.length() - 1);
		JSONParser.ValueContext valueContext = context.value();
		String valueString = valueContext.getText();

		Object value = new JsonValue(valueContext, valueString).getValue();

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

			return new BooleanValue(value).getBoolean();
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

			return new NumberValue(value).getNumber();
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

			return new StringValue(value).getString();
		}
		else
		{
			return "";
		}
	}

	public JsonObject getObject(String property)
	{
		if (this.properties.containsKey(property))
		{
			Object value = this.properties.get(property);

			if (value instanceof JsonObject)
			{
				return (JsonObject) value;
			}
			else
			{
				return new NullJsonObject();
			}
		}
		else
		{
			return new NullJsonObject();
		}
	}

	public JsonArray getArray(String property)
	{
		if (this.properties.containsKey(property))
		{
			Object value = this.properties.get(property);

			if (value instanceof JsonArray)
			{
				return (JsonArray) value;
			}
			else
			{
				return new NullJsonArray();
			}
		}
		else
		{
			return new NullJsonArray();
		}
	}

	public void put(String property, JsonArray value)
	{
		this.properties.put(property, value);
	}

	public void put(String property, JsonObject value)
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
