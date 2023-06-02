package yirgacheffe.json;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.HashSet;

public class JSONObject
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

	public static class Write
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

	static class Invalid implements Read
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
			return this.hashCode() == other.hashCode();
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

	static class Valid implements Read
	{
		private final Map<String, String> map;

		private Valid(Map<String, String> map)
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
			String value =  this.map.get(key);

			if (value == null)
			{
				return false;
			}

			try
			{
				return Double.parseDouble(value) != 0d;
			}
			catch (NumberFormatException e)
			{
				return !(value.length() == 0 ||
					value.equals("null") ||
					value.equals("false") ||
					value.equals("\"\""));
			}
		}

		@Override
		public double getNumber(String key)
		{
			String value = this.map.get(key);

			if (value == null)
			{
				return Double.NaN;
			}
			else
			{
				try
				{
					return Double.parseDouble(value);
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
			String value = this.map.get(key);

			if (value == null)
			{
				return "";
			}
			else if (value.length() > 1 &&
				value.charAt(0) == '"' &&
				value.charAt(value.length() - 1) == '"')
			{
				return value.substring(1, value.length() - 1);
			}
			else
			{
				return value;
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
				return new JSONArray.Invalid("Failed to read array with key \"" + key + "\".");
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

			for (Map.Entry<String, String> pair: this.map.entrySet())
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
			return this.hashCode() == other.hashCode();
		}

		@Override
		public int hashCode()
		{
			int hash = 0;

			for (String value: this.map.values())
			{
				hash = hash ^ JSONValue.read(value).hashCode();
			}

			return hash;
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

	public static Read read(String string)
	{
		if (string == null || string.length() == 0)
		{
			return new Invalid("Failed to parse object: No data.");
		}

		Map<String, String> map = new LinkedHashMap<>();
		byte state = START;
		int depth = 0;
		StringBuilder builder = new StringBuilder();
		boolean escape = false;
		String key = "";
		byte type = LITERAL;

		for (int i = 0; i < string.length(); i++)
		{
			char character = string.charAt(i);

			if (state == IN_VALUE)
			{
				if (escape)
				{
					builder.append(character);
					escape = false;
				}
				else if (character == '\\')
				{
					escape = true;
				}
				else if (type == STRING)
				{
					builder.append(character);

					if (character == '"')
					{
						map.put(key, builder.toString());
						state = BEFORE_KEY;
					}
				}
				else if (type == OBJECT)
				{
					builder.append(character);

					if (character == '}')
					{
						if (depth == 0)
						{
							map.put(key, builder.toString());
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
					builder.append(character);
					if (character == ']')
					{
						if (depth == 0)
						{
							map.put(key, builder.toString());
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
						map.put(key, builder.toString());
						state = END;
					}
					else if (character == ',')
					{
						map.put(key, builder.toString());
						state = BEFORE_KEY;
					}
					else if (!Character.isWhitespace(character))
					{
						builder.append(character);
					}
				}
			}
			else if (state == IN_KEY)
			{
				if (escape)
				{
					builder.append(character);
					escape = false;
				}
				else if (character == '\\')
				{
					escape = true;
				}
				else if (character == '"')
				{
					key = builder.toString();
					state = AFTER_KEY;
				}
				else
				{
					builder.append(character);
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
					builder.setLength(0);
					state = IN_KEY;
				}
				else if (character != ',' && !Character.isWhitespace(character))
				{
					return new Invalid("Failed to parse object at character " + i + ": Found " + character + " when expecting key.");
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
					return new Invalid("Failed to parse object at character " + i + ": Found " + character + " when expecting :.");
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
					else {
						type = LITERAL;
					}

					builder.setLength(0);
					builder.append(character);
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
					return new Invalid("Failed to parse object: Started with " + character + " instead of {.");
				}
			}
			else if (!Character.isWhitespace(character))
			{
				return new Invalid("Failed to parse object at character " + i + ": Found " + character + " after end of object.");
			}
		}

		if (state == END)
		{
			return new Valid(map);
		}
		else
		{
			return new Invalid("Failed to parse object: Ran out of characters before end of object.");
		}
	}
}
