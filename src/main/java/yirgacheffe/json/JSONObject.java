package yirgacheffe.json;

import java.nio.CharBuffer;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Map.Entry;

public final class JSONObject
{
	private JSONObject()
	{
	}

	public interface Read
	{
		boolean has(String key);

		boolean getBoolean(String key);

		double getNumber(String key);

		String getString(String key);

		Read getObject(String key);

		JSONArray.Read getArray(String key);

		JSONValue getValue(String key);

		String validate();

		Set<String> getKeys();
	}

	public static final class Write
	{
		private final Map<String, String> map = new LinkedHashMap<>();

		private Write()
		{
		}

		public Write put(String key, boolean value)
		{
			this.map.put(key, Boolean.toString(value));

			return this;
		}

		public Write put(String key, long value)
		{
			this.map.put(key, Long.toString(value));

			return this;
		}

		public Write put(String key, double value)
		{
			this.map.put(key, Double.toString(value));

			return this;
		}

		public Write put(String key, String value)
		{
			this.map.put(key, "\"" + value + "\"");

			return this;
		}

		public Write put(String key, Write value)
		{
			this.map.put(key, value.toString());

			return this;
		}

		public Write put(String key, JSONArray.Write value)
		{
			this.map.put(key, value.toString());

			return this;
		}

		public Read read()
		{
			return new Valid(this.map);
		}

		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder("{");

			for (Map.Entry<String, String> pair: this.map.entrySet())
			{
				builder
					.append('"')
					.append(pair.getKey())
					.append("\":")
					.append(pair.getValue())
					.append(',');
			}

			if (this.map.size() > 0)
			{
				builder.deleteCharAt(builder.length() - 1);
			}

			builder.append('}');

			return builder.toString();
		}
	}

	static final class Invalid implements Read
	{
		private final String error;

		Invalid(String error)
		{
			this.error = error;
		}

		@Override
		public boolean has(String key)
		{
			return false;
		}

		@Override
		public boolean getBoolean(String key)
		{
			return false;
		}

		@Override
		public double getNumber(String key)
		{
			return Double.NaN;
		}

		@Override
		public String getString(String key)
		{
			return "";
		}

		@Override
		public Read getObject(String key)
		{
			return new Invalid(this.error);
		}

		@Override
		public JSONArray.Read getArray(String key)
		{
			return new JSONArray.Invalid(this.error);
		}

		@Override
		public JSONValue getValue(String key)
		{
			return new JSONValue.Invalid(this.error);
		}

		@Override
		public String validate()
		{
			return this.error;
		}

		@Override
		public Set<String> getKeys()
		{
			return new HashSet<>();
		}

		@Override
		public boolean equals(Object other)
		{
			if (other instanceof JSONValue.Invalid)
			{
				return other.equals(this);
			}
			else if (other instanceof Invalid)
			{
				return this.error.equals(((Invalid) other).error);
			}
			else
			{
				return false;
			}
		}

		@Override
		public int hashCode()
		{
			return this.error.hashCode();
		}

		@Override
		public String toString()
		{
			return this.error;
		}
	}

	static final class Valid implements Read
	{
		private final Map<String, ? extends CharSequence> map;

		private Valid(Map<String, ? extends CharSequence> map)
		{
			this.map = map;
		}

		@Override
		public boolean has(String key)
		{
			return this.map.containsKey(key);
		}

		@Override
		public boolean getBoolean(String key)
		{
			CharSequence value = this.map.get(key);

			if (value == null)
			{
				return false;
			}

			String string = value.toString();

			try
			{
				return Double.parseDouble(string) != 0d;
			}
			catch (NumberFormatException e)
			{
				return !(string.length() == 0 ||
					string.equals("null") ||
					string.equals("false") ||
					string.equals("\"\""));
			}
		}

		@Override
		public double getNumber(String key)
		{
			CharSequence value = this.map.get(key);

			if (value == null)
			{
				return Double.NaN;
			}
			else
			{
				try
				{
					return Double.parseDouble(value.toString());
				}
				catch (NumberFormatException e)
				{
					return Double.NaN;
				}
			}
		}

		@Override
		public String getString(String key)
		{
			CharSequence value = this.map.get(key);

			if (value == null)
			{
				return "";
			}

			if (value.length() > 1 &&
				value.charAt(0) == '"' &&
				value.charAt(value.length() - 1) == '"')
			{
				return value.subSequence(1, value.length() - 1).toString();
			}
			else
			{
				return value.toString();
			}
		}

		@Override
		public Read getObject(String key)
		{
			if (this.map.containsKey(key))
			{
				return read(this.map.get(key));
			}
			else
			{
				return new Invalid("Failed to read object with key \"" + key + "\".");
			}
		}

		@Override
		public JSONArray.Read getArray(String key)
		{
			if (this.map.containsKey(key))
			{
				return JSONArray.read(this.map.get(key));
			}
			else
			{
				return new JSONArray.Invalid(
					"Failed to read array with key \"" + key + "\".");
			}
		}

		@Override
		public JSONValue getValue(String key)
		{
			return JSONValue.read(this.map.get(key));
		}

		@Override
		public String validate()
		{
			StringBuilder errors = new StringBuilder();

			for (Map.Entry<String, ? extends CharSequence> pair: this.map.entrySet())
			{
				String error = JSONValue.read(pair.getValue()).validate();

				if (error.length() > 0)
				{
					errors.append("Value of key ")
							.append(pair.getKey())
							.append(": ")
							.append(error)
							.append(", ");
				}
			}

			if (errors.length() == 0)
			{
				return "";
			}
			else
			{
				errors.setLength(errors.length() - 2);

				return errors.toString();
			}
		}

		@Override
		public Set<String> getKeys()
		{
			return this.map.keySet();
		}

		@Override
		public boolean equals(Object other)
		{
			if (other instanceof JSONValue.Valid)
			{
				return other.equals(this);
			}
			else if (other instanceof Valid)
			{
				Valid otherObject = (Valid) other;

				if (!this.map.keySet().equals(otherObject.map.keySet()))
				{
					return false;
				}

				for (String key: this.map.keySet())
				{
					if (!JSONValue.read(this.map.get(key))
						.equals(JSONValue.read(otherObject.map.get(key))))
					{
						return false;
					}
				}

				return true;
			}
			else
			{
				return false;
			}
		}

		@Override
		public int hashCode()
		{
			int hash = 1;

			for (CharSequence value: this.map.values())
			{
				hash = hash ^ JSONValue.read(value).hashCode();
			}

			return hash;
		}

		@Override
		public String toString()
		{
			if (this.map.size() == 0)
			{
				return "{}";
			}

			String indent = "    ";
			StringBuilder builder = new StringBuilder("{");

			for (Entry<String, ? extends CharSequence> pair: this.map.entrySet())
			{
				JSONValue value = JSONValue.read(pair.getValue());
				boolean newLine = value.isObject() || value.isArray();

				builder
					.append('\n')
					.append(indent)
					.append('"')
					.append(pair.getKey())
					.append("\":")
					.append(newLine ? '\n' : ' ')
					.append(newLine ? indent + indent : "")
					.append(value.toString()
						.replace("\n", "\n" + indent + indent))
					.append(',');
			}

			builder.setLength(builder.length() - 1);
			builder.append("\n}");

			return builder.toString();
		}
	}

	public static Write write()
	{
		return new Write();
	}

	private static final byte START = 0;
	private static final byte BEFORE_KEY = 1;
	private static final byte IN_KEY = 2;
	private static final byte AFTER_KEY = 3;
	private static final byte BEFORE_VALUE = 4;
	private static final byte IN_VALUE = 5;
	private static final byte END = 6;

	private static final byte LITERAL = 0;
	private static final byte STRING = 1;
	private static final byte OBJECT = 2;
	private static final byte ARRAY = 3;

	public static Read read(CharSequence characters)
	{
		if (characters == null || characters.length() == 0)
		{
			return new Invalid("Failed to parse object: No data.");
		}

		Map<String, CharSequence> map = new HashMap<>();
		byte state = START;
		int depth = 0;
		int offset = 0;
		boolean escape = false;
		String key = "";
		byte type = LITERAL;

		for (int i = 0; i < characters.length(); i++)
		{
			char character = characters.charAt(i);

			if (state == IN_VALUE)
			{
				if (escape)
				{
					escape = false;
				}
				else if (character == '\\')
				{
					escape = true;
				}
				else if (type == STRING)
				{
					if (character == '"')
					{
						map.put(key, CharBuffer.wrap(characters, offset, i + 1));
						state = BEFORE_KEY;
					}
				}
				else if (type == OBJECT)
				{
					if (character == '}')
					{
						if (depth == 0)
						{
							map.put(key, CharBuffer.wrap(characters, offset, i + 1));
							state = BEFORE_KEY;
						}
						else
						{
							depth -= 1;
						}
					}
					else if (character == '{')
					{
						depth += 1;
					}
				}
				else if (type == ARRAY)
				{
					if (character == ']')
					{
						if (depth == 0)
						{
							map.put(key, CharBuffer.wrap(characters, offset, i + 1));
							state = BEFORE_KEY;
						}
						else
						{
							depth -= 1;
						}
					}
					else if (character == '[')
					{
						depth += 1;
					}
				}
				else
				{
					if (character == '}')
					{
						map.put(key, CharBuffer.wrap(characters, offset, i));
						state = END;
					}
					else if (Character.isWhitespace(character) || character == ',')
					{
						map.put(key, CharBuffer.wrap(characters, offset, i));
						state = BEFORE_KEY;
					}
				}
			}
			else if (state == IN_KEY)
			{
				if (escape)
				{
					escape = false;
				}
				else if (character == '\\')
				{
					escape = true;
				}
				else if (character == '"')
				{
					key = CharBuffer.wrap(characters, offset + 1, i).toString();

					if (map.containsKey(key))
					{
						return new Invalid(
							"Failed to parse object: Duplicate key \"" + key + "\".");
					}
					else
					{
						state = AFTER_KEY;
					}
				}
			}
			else if (state == BEFORE_KEY)
			{
				if (character == '}')
				{
					state = END;
				}
				else if (character == '"')
				{
					offset = i;
					state = IN_KEY;
				}
				else if (character != ',' && !Character.isWhitespace(character))
				{
					return new Invalid(
						"Failed to parse object at character " + i +
						": Found " + character + " when expecting key.");
				}
			}
			else if (state == AFTER_KEY)
			{
				if (character == ':')
				{
					state = BEFORE_VALUE;
				}
				else if (Character.isWhitespace(character))
				{
					return new Invalid(
						"Failed to parse object at character " + i +
						": Found " + character + " when expecting :.");
				}
			}
			else if (state == BEFORE_VALUE)
			{
				if (!Character.isWhitespace(character))
				{
					if (character == '{')
					{
						depth = 0;
						type = OBJECT;
					}
					else if (character == '[')
					{
						depth = 0;
						type = ARRAY;
					}
					else if (character == '"')
					{
						type = STRING;
					}
					else
					{
						type = LITERAL;
					}

					offset = i;
					state = IN_VALUE;
				}
			}
			else if (state == START)
			{
				if (character == '{')
				{
					state = BEFORE_KEY;
				}
				else if (!Character.isWhitespace(character))
				{
					return new Invalid(
						"Failed to parse object: Started with " + character +
						" instead of {.");
				}
			}
			else if (!Character.isWhitespace(character))
			{
				return new Invalid(
					"Failed to parse object at character " + i +
					": Found " + character + " after end of object.");
			}
		}

		if (state == END)
		{
			return new Valid(map);
		}
		else
		{
			return new Invalid(
				"Failed to parse object: Ran out of characters before end of object.");
		}
	}
}
