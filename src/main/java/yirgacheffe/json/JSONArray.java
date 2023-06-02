package yirgacheffe.json;

import java.util.*;

public class JSONArray
{
	private JSONArray()
	{
	}

	public interface Read extends Iterable<JSONValue>
	{
		boolean getBoolean(int index);

		Double getNumber(int index);

		String getString(int index);

		JSONObject.Read getObject(int index);

		JSONArray.Read getArray(int index);

		JSONValue getValue(int index);

		int length();

		String validate();
	}

	public static class Write
	{
		private List<String> list = new ArrayList<>();

		Write push(boolean value)
		{
			this.list.add(Boolean.toString(value));

			return this;
		}

		Write push(long value)
		{
			this.list.add(Long.toString(value));

			return this;
		}

		Write push(double value)
		{
			this.list.add(Double.toString(value));

			return this;
		}

		Write push(String value)
		{
			this.list.add(("\"" + value + "\""));

			return this;
		}

		Write push(JSONObject.Write value)
		{
			this.list.add((value.toString()));

			return this;
		}

		Write push(Write value)
		{
			this.list.add((value.toString()));

			return this;
		}

		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder("[");

			for (String value: this.list)
			{
				builder.append(value).append(',');
			}

			if (this.list.size() > 0)
			{
				builder.deleteCharAt(builder.length() - 1);
			}

			builder.append(']');

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
		public boolean getBoolean(int index)
		{
			return false;
		}

		@Override
		public Double getNumber(int index)
		{
			return Double.NaN;
		}

		@Override
		public String getString(int index)
		{
			return "";
		}

		@Override
		public JSONObject.Read getObject(int index)
		{
			return new JSONObject.Invalid(this.error);
		}

		@Override
		public Read getArray(int index)
		{
			return new Invalid(this.error);
		}

		@Override
		public JSONValue getValue(int index)
		{
			return null;
		}

		@Override
		public int length()
		{
			return 0;
		}

		@Override
		public String validate()
		{
			return this.error;
		}

		@Override
		public Iterator<JSONValue> iterator()
		{
			return new Iterator<JSONValue>()
			{
				@Override
				public boolean hasNext()
				{
					return false;
				}

				@Override
				public JSONValue next()
				{
					throw new IndexOutOfBoundsException();
				}
			};
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
		private List<String> list;

		private Valid(List<String> list)
		{
			this.list = list;
		}

		@Override
		public boolean getBoolean(int index)
		{
			if (index >= 0 && this.list.size() > index)
			{
				String value = this.list.get(index);

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
			else
			{
				return false;
			}
		}

		@Override
		public Double getNumber(int index)
		{
			if (index < 0 || this.list.size() <= index)
			{
				return Double.NaN;
			}

			String value = this.list.get(index);

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
		public String getString(int index)
		{
			if (index < 0 || this.list.size() <= index)
			{
				return "";
			}

			String value = this.list.get(index);

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
		public JSONObject.Read getObject(int index)
		{
			if (index >= 0 && this.list.size() > index)
			{
				return JSONObject.read(this.list.get(index));
			}
			else
			{
				return new JSONObject.Invalid("Failed to read array index " + index + " of array of length " + this.list.size() + ".");
			}
		}

		@Override
		public Read getArray(int index)
		{
			if (index >= 0 && this.list.size() > index)
			{
				return read(this.list.get(index));
			}
			else
			{
				return new Invalid("Failed to read array index " + index + " of array of length " + this.list.size() + ".");
			}
		}

		@Override
		public JSONValue getValue(int index)
		{
			if (index >= 0 && this.list.size() > index)
			{
				return JSONValue.read(this.list.get(index));
			}
			else
			{
				return new JSONValue.Invalid("Failed to read array index " + index + " of array of length " + this.list.size() + ".");
			}
		}

		@Override
		public int length()
		{
			return this.list.size();
		}

		@Override
		public String validate()
		{
			StringBuilder errors = new StringBuilder();

			for (int i = 0; i < this.list.size(); i++)
			{
				String error = JSONValue.read(this.list.get(i)).validate();

				if (error.length() > 0)
				{
					errors.append("Value at array position ")
						.append(i)
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
		public Iterator<JSONValue> iterator()
		{
			return new Iterator<JSONValue>()
			{
				private final int index = 0;

				@Override
				public boolean hasNext()
				{
					return index < list.size();
				}

				@Override
				public JSONValue next()
				{
					return JSONValue.read(list.get(index));
				}
			};
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

			for (String value: this.list)
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
	private static final byte BEFORE_VALUE = 1;
	private static final byte IN_VALUE = 2;
	private static final byte AFTER_VALUE = 3;
	private static final byte END = 4;

	private static final byte LITERAL = 0;
	private static final byte STRING = 1;
	private static final byte OBJECT = 2;
	private static final byte ARRAY = 3;

	public static Read read(String string)
	{
		if (string == null || string.length() == 0)
		{
			return new Invalid("Failed to parse array: No data.");
		}

		List<String> list = new ArrayList<>();
		byte state = START;
		int depth = 0;
		StringBuilder builder = new StringBuilder();
		boolean escape = false;
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
						list.add(builder.toString());
						state = AFTER_VALUE;
					}
				}
				else if (type == OBJECT)
				{
					builder.append(character);

					if (character == '}')
					{
						if (depth == 0)
						{
							list.add(builder.toString());
							state = AFTER_VALUE;
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
							list.add(builder.toString());
							state = AFTER_VALUE;
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
					if (character == ']')
					{
						list.add(builder.toString());
						state = END;
					}
					else if (character == ',')
					{
						list.add(builder.toString());
						state = BEFORE_VALUE;
					}
					else if (!Character.isWhitespace(character))
					{
						builder.append(character);
					}
				}
			}
			else if (state == BEFORE_VALUE)
			{
				if (character == ']')
				{
					state = END;
				}
				else if (!Character.isWhitespace(character))
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
			else if (state == AFTER_VALUE)
			{
				if (character == ']')
				{
					state = END;
				}
				else if (character == ',')
				{
					state = BEFORE_VALUE;
				}
				else if (!Character.isWhitespace(character))
				{
					return new Invalid("Failed to parse array at character " + i + ": Found " + character + " when expecting ,.");
				}
			}
			else if (state == START)
			{
				if (character == '[')
				{
					state = BEFORE_VALUE;
				}
				else if (!Character.isWhitespace(character))
				{
					return new Invalid("Failed to parse array: Started with " + character + " instead of [.");
				}
			}
			else if (!Character.isWhitespace(character))
			{
				return new Invalid("Failed to parse array at character " + i + ": Found " + character + " after end of array.");
			}
		}

		if (state == END)
		{
			return new Valid(list);
		}
		else
		{
			return new Invalid("Failed to parse array: Ran out of characters before end of array.");
		}
	}
}
