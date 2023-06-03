package yirgacheffe.json;

public interface JSONValue
{
	boolean isNull();

	boolean isBoolean();

	boolean isNumber();

	boolean isString();

	boolean isObject();

	boolean isArray();

	boolean getBoolean();

	double getNumber();

	String getString();

	JSONObject.Read getObject();

	JSONArray.Read getArray();

	String validate();

	class Invalid implements JSONValue
	{
		private String error;

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
	}

	class Valid implements JSONValue
	{
		private CharSequence value;

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
			try
			{
				Double.parseDouble(this.value.toString());

				return true;
			}
			catch (NumberFormatException e)
			{
				return false;
			}
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
			if (this.isObject())
			{
				return JSONObject.read(this.value).toString();
			}
			else if (this.isArray())
			{
				return JSONArray.read(this.value).toString();
			}
			else
			{
				return this.value.toString();
			}
		}
	}

	static JSONValue read(CharSequence value)
	{
		if (value == null || value.length() == 0)
		{
			return new Invalid("");
		}

		String string = value.toString();

		try
		{
			Double.parseDouble(string);

			return new Valid(string);
		}
		catch (NumberFormatException e)
		{
			if (string.length() == 1)
			{
				return new Invalid(
					"Failed to parse value: " + string + " is not a JSON value.");
			}
			else if (string.equals("null") ||
				string.equals("true") ||
				string.equals("false") ||
				string.charAt(0) == '"' && string.charAt(string.length() - 1) == '"' ||
				string.charAt(0) == '{' && string.charAt(string.length() - 1) == '}' ||
				string.charAt(0) == '[' && string.charAt(string.length() - 1) == ']')
			{
				return new Valid(string);
			}
			else
			{
				return new Invalid(
					"Failed to parse value: " + string + " is not a JSON value.");
			}
		}
	}
}
