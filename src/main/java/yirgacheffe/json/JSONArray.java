package yirgacheffe.json;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import yirgacheffe.parser.JSONLexer;
import yirgacheffe.parser.JSONParser;

import java.util.Arrays;

public class JSONArray implements JsonData
{
	private static final int LOG_THIRTY_TWO = 5;

	private int length = 0;

	private Object[] array;

	public JSONArray()
	{
		this.array = new Object[1 << LOG_THIRTY_TWO];
	}

	public JSONArray(String data)
	{
		CharStream charStream = CharStreams.fromString(data);
		JSONLexer lexer = new JSONLexer(charStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ParseErrorListener errorListener = new ParseErrorListener();

		JSONParser parser = new JSONParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(errorListener);

		JSONParser.ArrayContext context = parser.array();

		this.parse(context);

		if (errorListener.hasError())
		{
			String message = Arrays.toString(errorListener.getErrors());

			throw new JSONException(message.substring(1, message.length() - 1));
		}
	}

	void parse(JSONParser.ArrayContext context)
	{
		int power = LOG_THIRTY_TWO;
		int length = context.value().size() >> LOG_THIRTY_TWO;

		while (length > 0)
		{
			power++;
			length = length >> 1;
		}

		this.array = new Object[1 << power];
		this.length = context.value().size();

		for (int i = 0; i < context.value().size(); i++)
		{
			this.parseValue(context.value(i), i);
		}
	}

	private void parseValue(JSONParser.ValueContext context, int index)
	{
		String valueString = context.getText();

		Object value = new JSONValue(context, valueString).getValue();

		this.array[index] = value;
	}

	public boolean getBoolean(int index)
	{
		if (this.length > index)
		{
			Object value = this.array[index];

			return new BooleanValue(value).getBoolean();
		}
		else
		{
			return false;
		}
	}

	public double getNumber(int index)
	{
		if (this.length > index)
		{
			Object value = this.array[index];

			return new NumberValue(value).getNumber();
		}
		else
		{
			return Double.NaN;
		}
	}

	public String getString(int index)
	{
		if (this.length > index)
		{
			Object value = this.array[index];

			return new StringValue(value).getString();
		}
		else
		{
			return "";
		}
	}

	public JSONObject getObject(int index)
	{
		if (this.length > index)
		{
			Object value = this.array[index];

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

	public JSONArray getArray(int index)
	{
		if (this.length > index)
		{
			Object value = this.array[index];

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

	public void put(JSONArray value)
	{
		this.grow();
		this.array[this.length++] = value;
	}

	public void put(JSONObject value)
	{
		this.grow();
		this.array[this.length++] = value;
	}

	public void put(String value)
	{
		this.grow();
		this.array[this.length++] = value;
	}

	public void put(double value)
	{
		this.grow();
		this.array[this.length++] = value;
	}

	public void put(long value)
	{
		this.grow();
		this.array[this.length++] = value;
	}

	public void put(boolean value)
	{
		this.grow();
		this.array[this.length++] = value;
	}

	private void grow()
	{
		if (this.length == this.array.length)
		{
			int newLength = this.length << 1;

			this.array = Arrays.copyOf(this.array, newLength);
		}
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder("[");

		for (int i = 0; i < this.length; i++)
		{
			this.appendValueString(builder, this.array[i]);

			if (i < this.length - 1)
			{
				builder.append(',');
			}
		}

		builder.append(']');

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

	public int length()
	{
		return this.length;
	}
}