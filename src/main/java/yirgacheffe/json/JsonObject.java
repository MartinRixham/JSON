package yirgacheffe.json;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
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

		ParseTree tree = parser.object();

		if (errorListener.hasError())
		{
			String message = Arrays.toString(errorListener.getErrors());

			throw new JsonException(message.substring(1, message.length() - 1));
		}

		ParseTreeWalker walker = new ParseTreeWalker();

		walker.walk(new ObjectListener(this.properties), tree);
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

			if (value instanceof JsonObject)
			{
				return true;
			}
			if (value instanceof String)
			{
				return ((String) value).length() > 0;
			}
			else if (value instanceof Long)
			{
				return ((long) value) != 0;
			}
			else if (value instanceof Double)
			{
				return ((double) value) != 0;
			}
			else if (value instanceof Boolean)
			{
				return value.equals(true);
			}
			else
			{
				return false;
			}
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

			if (value instanceof Long)
			{
				return (long) value;
			}
			else if (value instanceof Double)
			{
				return (double) value;
			}
			else
			{
				return Double.NaN;
			}
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

			if (value == null)
			{
				return "null";
			}
			else
			{
				return value.toString();
			}
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
