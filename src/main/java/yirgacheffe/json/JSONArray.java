package yirgacheffe.json;

import java.nio.CharBuffer;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Iterator;

public final class JSONArray
{
	private JSONArray()
	{
	}

	public abstract static class Read implements Iterable<JSONValue>
	{
		public abstract boolean getBoolean(int index);

		public abstract Double getNumber(int index);

		public abstract String getString(int index);

		public abstract JSONObject.Read getObject(int index);

		public abstract JSONArray.Read getArray(int index);

		public abstract JSONValue getValue(int index);

		public abstract int length();

		public abstract String validate();

		@Override
		public abstract String toString();

		abstract String print();
	}

	public static final class Write
	{
		private final List<String> list = new LinkedList<>();

		public Write push(boolean value)
		{
			this.list.add(Boolean.toString(value));

			return this;
		}

		public Write push(boolean... values)
		{
			String[] strings = new String[values.length];

			for (int i = 0; i < values.length; i++)
			{
				strings[i] = Boolean.toString(values[i]);
			}

			this.list.addAll(Arrays.asList(strings));

			return this;
		}

		public Write push(long value)
		{
			this.list.add(Long.toString(value));

			return this;
		}

		public Write push(long... values)
		{
			String[] strings = new String[values.length];

			for (int i = 0; i < values.length; i++)
			{
				strings[i] = Long.toString(values[i]);
			}

			this.list.addAll(Arrays.asList(strings));

			return this;
		}

		public Write push(double value)
		{
			this.list.add(Double.toString(value));

			return this;
		}

		public Write push(double... values)
		{
			String[] strings = new String[values.length];

			for (int i = 0; i < values.length; i++)
			{
				strings[i] = Double.toString(values[i]);
			}

			this.list.addAll(Arrays.asList(strings));

			return this;
		}

		public Write push(String value)
		{
			this.list.add(("\"" + value + "\""));

			return this;
		}

		public Write push(String... values)
		{
			String[] strings = new String[values.length];

			for (int i = 0; i < values.length; i++)
			{
				strings[i] = "\"" + values[i] + "\"";
			}

			this.list.addAll(Arrays.asList(strings));

			return this;
		}

		public Write push(JSONObject.Write value)
		{
			this.list.add((value.toString()));

			return this;
		}

		public Write push(JSONObject.Write... values)
		{
			String[] strings = new String[values.length];

			for (int i = 0; i < values.length; i++)
			{
				strings[i] = values[i].toString();
			}

			this.list.addAll(Arrays.asList(strings));

			return this;
		}

		public Write push(Write value)
		{
			this.list.add((value.toString()));

			return this;
		}

		public Write push(Write... values)
		{
			String[] strings = new String[values.length];

			for (int i = 0; i < values.length; i++)
			{
				strings[i] = values[i].toString();
			}

			this.list.addAll(Arrays.asList(strings));

			return this;
		}

		public Read read()
		{
			return new Valid(this.list);
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

	public static final class Invalid extends Read
	{
		private final String error;

		private Invalid()
		{
			this.error = "";
		}

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
			if (other instanceof Invalid)
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

		@Override
		String print()
		{
			throw new UnsupportedOperationException();
		}
	}

	public static final class Valid extends Read
	{
		private final List<? extends CharSequence> list;

		private Valid()
		{
			this.list = new ArrayList<>();
		}

		private Valid(List<? extends CharSequence> list)
		{
			this.list = list;
		}

		@Override
		public boolean getBoolean(int index)
		{
			if (index < 0 || this.list.size() <= index)
			{
				return false;
			}

			CharSequence value = this.list.get(index);
			JSONValue json = JSONValue.read(value);

			return json.getBoolean();
		}

		@Override
		public Double getNumber(int index)
		{
			if (index < 0 || this.list.size() <= index)
			{
				return Double.NaN;
			}

			CharSequence value = this.list.get(index);

			try
			{
				return Double.parseDouble(value.toString());
			}
			catch (NumberFormatException e)
			{
				return Double.NaN;
			}
		}

		@Override
		public String getString(int index)
		{
			if (index < 0 || this.list.size() <= index)
			{
				return "";
			}

			CharSequence value = this.list.get(index);
			JSONValue json = JSONValue.read(value);

			return json.getString();
		}

		@Override
		public JSONObject.Read getObject(int index)
		{
			if (0 <= index && index < this.list.size())
			{
				return JSONObject.read(this.list.get(index));
			}
			else
			{
				return new JSONObject.Invalid(
					"Failed to read array index " + index +
					" of array of length " + this.list.size() + ".");
			}
		}

		@Override
		public Read getArray(int index)
		{
			if (0 <= index && index < this.list.size())
			{
				return read(this.list.get(index));
			}
			else
			{
				return new Invalid(
					"Failed to read array index " + index +
					" of array of length " + this.list.size() + ".");
			}
		}

		@Override
		public JSONValue getValue(int index)
		{
			if (0 <= index && index < this.list.size())
			{
				return JSONValue.read(this.list.get(index));
			}
			else
			{
				return new JSONValue.Invalid(
					"Failed to read array index " + index +
					" of array of length " + this.list.size() + ".");
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
			boolean hasError = false;
			StringBuilder errors = new StringBuilder("{");

			for (int i = 0; i < this.list.size(); i++)
			{
				JSONValue value = JSONValue.read(this.list.get(i));
				String error = value.validate();

				if (error.length() > 0)
				{
					hasError = true;

					String formattedError =
						value.isObject() || value.isArray() ?
							error :
							'"' + error.replace("\\", "\\\\")
								.replace("\"", "\\\"") + '"';

					errors.append("\"value at array position ")
						.append(i)
						.append("\": ")
						.append(formattedError)
						.append(", ");
				}
			}

			if (hasError)
			{
				errors.setLength(errors.length() - 2);
				errors.append("}");

				return errors.toString();
			}
			else
			{
				return "";
			}
		}

		@Override
		public Iterator<JSONValue> iterator()
		{
			return new Iterator<JSONValue>()
			{
				private int index = 0;

				@Override
				public boolean hasNext()
				{
					return this.index < Valid.this.list.size();
				}

				@Override
				public JSONValue next()
				{
					JSONValue value = JSONValue.read(Valid.this.list.get(this.index));
					this.index++;
					return value;
				}
			};
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
				Valid otherArray = (Valid) other;

				if (this.length() != otherArray.length())
				{
					return false;
				}

				for (int i = 0; i < this.list.size(); i++)
				{
					if (!JSONValue.read(this.list.get(i))
						.equals(JSONValue.read(otherArray.list.get(i))))
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
			int hash = 2;

			for (CharSequence value: this.list)
			{
				hash = hash ^ JSONValue.read(value).hashCode();
			}

			return hash;
		}

		@Override
		public String toString()
		{
			String errors = this.validate();

			if (errors.length() > 0)
			{
				return JSONObject.read(errors).toString();
			}
			else
			{
				return this.print();
			}
		}

		@Override
		String print()
		{
			if (this.list.size() == 0)
			{
				return "[]";
			}

			String indent = "    ";
			StringBuilder builder = new StringBuilder("[");
			boolean newLine = false;

			for (CharSequence string: this.list)
			{
				JSONValue value = JSONValue.read(string);

				if (value.isObject() || value.isArray())
				{
					newLine = true;
				}
			}

			for (CharSequence value: this.list)
			{
				builder.append(newLine ? "\n" : "")
					.append(newLine ? indent : "")
					.append(JSONValue.read(value).print()
						.replace("\n", "\n" + indent))
					.append(newLine ? "," : ", ");
			}

			builder.setLength(builder.length() - (newLine ? 1 : 2));
			builder.append(newLine ? "\n" : "").append(']');

			return builder.toString();
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

	public static Read read(CharSequence characters)
	{
		if (characters == null || characters.length() == 0)
		{
			return new Invalid("Failed to parse array: No data.");
		}

		List<CharSequence> list = new ArrayList<>();
		byte state = START;
		int depth = 0;
		int offset = 0;
		boolean escape = false;
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
						list.add(CharBuffer.wrap(characters, offset, i + 1));
						state = AFTER_VALUE;
					}
				}
				else if (type == OBJECT)
				{
					if (character == '}')
					{
						if (depth == 0)
						{
							list.add(CharBuffer.wrap(characters, offset, i + 1));
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
					if (character == ']')
					{
						if (depth == 0)
						{
							list.add(CharBuffer.wrap(characters, offset, i + 1));
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
						list.add(CharBuffer.wrap(characters, offset, i));
						state = END;
					}
					else if (Character.isWhitespace(character) || character == ',')
					{
						list.add(CharBuffer.wrap(characters, offset, i));
						state = BEFORE_VALUE;
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
					else
					{
						type = LITERAL;
					}

					offset = i;
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
					return new Invalid(
						"Failed to parse array at character " + i +
						": Found " + character + " when expecting ,.");
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
					return new Invalid(
						"Failed to parse array: Started with " + character +
						" instead of [.");
				}
			}
			else if (!Character.isWhitespace(character))
			{
				return new Invalid("Failed to parse array at character " + i +
				": Found " + character + " after end of array.");
			}
		}

		if (state == END)
		{
			return new Valid(list);
		}
		else
		{
			return new Invalid(
				"Failed to parse array: Ran out of characters before end of array.");
		}
	}
}
