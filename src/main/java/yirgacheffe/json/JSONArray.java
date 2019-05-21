package yirgacheffe.json;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import yirgacheffe.parser.JSONLexer;
import yirgacheffe.parser.JSONParser;

import java.util.Arrays;

public class JSONArray implements JsonData
{
	private static final int INITIAL_SIZE = 32;

	private int length;

	private Object[] array;

	public JSONArray()
	{
		this.array = new Object[INITIAL_SIZE];
		this.length = 0;
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

		if (errorListener.hasError())
		{
			String message = Arrays.toString(errorListener.getErrors());

			throw new JSONException(message.substring(1, message.length() - 1));
		}

		this.array = context.value().toArray();
		this.length = this.array.length;
	}

	JSONArray(Object[] values)
	{
		this.array = values;
		this.length = values.length;
	}

	public boolean getBoolean(int index)
	{
		if (this.array.length > index)
		{
			return JSONValue.getBoolean(this.array[index]);
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
			return JSONValue.getNumber(this.array[index]);
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
			return JSONValue.getString(
				(JSONParser.ValueContext) this.array[index]);
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
			return JSONValue.getObject(this.array[index]);
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
			return JSONValue.getArray(this.array[index]);
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
			JSONValue.appendValueString(builder, this.array[i]);

			if (i < this.length - 1)
			{
				builder.append(',');
			}
		}

		builder.append(']');

		return builder.toString();
	}

	public int length()
	{
		return this.length;
	}
}
