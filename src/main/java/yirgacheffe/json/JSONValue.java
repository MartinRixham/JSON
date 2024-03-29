package yirgacheffe.json;

import java.util.regex.Pattern;

public abstract class JSONValue
{
	public abstract boolean isNull();

	public abstract boolean isBoolean();

	public abstract boolean isNumber();

	public abstract boolean isString();

	public abstract boolean isObject();

	public abstract boolean isArray();

	public abstract boolean getBoolean();

	public abstract double getNumber();

	public abstract String getString();

	public abstract JSONObject.Read getObject();

	public abstract JSONArray.Read getArray();

	public abstract String validate();

	@Override
	public abstract String toString();

	abstract String print();

	private static final Pattern JSON_NUMBER_PATTERN =
		Pattern.compile("-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?");

	public static final class Invalid extends JSONValue
	{
		private String error;

		private Invalid()
		{
			this.error = "";
		}

		Invalid(String error)
		{
			this.error = error;
		}

		@Override
		public boolean isNull()
		{
			return false;
		}

		@Override
		public boolean isBoolean()
		{
			return false;
		}

		@Override
		public boolean isNumber()
		{
			return false;
		}

		@Override
		public boolean isString()
		{
			return false;
		}

		@Override
		public boolean isObject()
		{
			return false;
		}

		@Override
		public boolean isArray()
		{
			return false;
		}

		@Override
		public boolean getBoolean()
		{
			return false;
		}

		@Override
		public double getNumber()
		{
			return Double.NaN;
		}

		@Override
		public String getString()
		{
			return "";
		}

		@Override
		public JSONObject.Read getObject()
		{
			return new JSONObject.Invalid(this.error);
		}

		@Override
		public JSONArray.Read getArray()
		{
			return new JSONArray.Invalid(this.error);
		}

		@Override
		public String validate()
		{
			return this.error;
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

	public static final class Valid extends JSONValue
	{
		private final CharSequence value;

		private Valid()
		{
			this.value = "";
		}

		Valid(CharSequence value)
		{
			this.value = value;
		}

		@Override
		public boolean isNull()
		{
			return this.value.toString().equals("null");
		}

		@Override
		public boolean isBoolean()
		{
			String string = this.value.toString();

			return string.equals("true") || string.equals("false");
		}

		@Override
		public boolean isNumber()
		{
			return JSON_NUMBER_PATTERN.matcher(this.value).matches();
		}

		@Override
		public boolean isString()
		{
			return this.value.charAt(0) == '"';
		}

		@Override
		public boolean isObject()
		{
			return this.value.charAt(0) == '{';
		}

		@Override
		public boolean isArray()
		{
			return this.value.charAt(0) == '[';
		}

		@Override
		public boolean getBoolean()
		{
			String string = this.value.toString();

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
		public double getNumber()
		{
			try
			{
				return Double.parseDouble(this.value.toString());
			}
			catch (NumberFormatException e)
			{
				return Double.NaN;
			}
		}

		@Override
		public String getString()
		{
			CharSequence value = this.value;

			if (value.charAt(0) == '"' &&
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
		public JSONObject.Read getObject()
		{
			return JSONObject.read(this.value);
		}

		@Override
		public JSONArray.Read getArray()
		{
			return JSONArray.read(this.value);
		}

		@Override
		public String validate()
		{
			if (this.isObject())
			{
				return JSONObject.read(this.value).validate();
			}
			else if (this.isArray())
			{
				return JSONArray.read(this.value).validate();
			}
			else
			{
				return "";
			}
		}

		@Override
		public boolean equals(Object other)
		{
			if (this.isObject())
			{
				return other.equals(JSONObject.read(this.value));
			}
			else if (this.isArray())
			{
				return other.equals(JSONArray.read(this.value));
			}
			else if (other instanceof Valid)
			{
				return this.value.toString().equals(((Valid) other).value.toString());
			}
			else
			{
				return false;
			}
		}

		@Override
		public int hashCode()
		{
			if (this.isObject())
			{
				return JSONObject.read(this.value).hashCode();
			}
			else if (this.isArray())
			{
				return JSONArray.read(this.value).hashCode();
			}
			else
			{
				return this.value.hashCode();
			}
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
			if (this.isObject())
			{
				return JSONObject.read(this.value).print();
			}
			else if (this.isArray())
			{
				return JSONArray.read(this.value).print();
			}
			else
			{
				return this.value.toString();
			}
		}
	}

	public static JSONValue read(CharSequence value)
	{
		if (value == null)
		{
			return new Invalid("Failed to parse value: No data.");
		}

		String string = value.toString();

		if (string.length() == 0)
		{
			return new Invalid("Failed to parse value: No data.");
		}
		else if (JSON_NUMBER_PATTERN.matcher(string).matches())
		{
			return new Valid(value);
		}
		else if (string.length() == 1)
		{
			return new Invalid(
				"Failed to parse value: " + value + " is not a JSON value.");
		}
		else if (string.equals("null") ||
			string.equals("true") ||
			string.equals("false") ||
			string.charAt(0) == '"' && string.charAt(value.length() - 1) == '"' ||
			string.charAt(0) == '{' && string.charAt(value.length() - 1) == '}' ||
			string.charAt(0) == '[' && string.charAt(value.length() - 1) == ']')
		{
			return new Valid(string);
		}
		else
		{
			return new Invalid(
				"Failed to parse value: " + value + " is not a JSON value.");
		}
	}
}
