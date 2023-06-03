package yirgacheffe.json;

interface JSONValue
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
			return 0;
		}

		@Override
		public String getString()
		{
			return null;
		}

		@Override
		public JSONObject.Read getObject()
		{
			return null;
		}

		@Override
		public JSONArray.Read getArray()
		{
			return null;
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
		private String string;

		Valid(String string)
		{
			this.string = string;
		}

		@Override
		public boolean isNull()
		{
			return this.string.equals("null");
		}

		@Override
		public boolean isBoolean()
		{
			return this.string.equals("true") || this.string.equals("false");
		}

		@Override
		public boolean isNumber()
		{
			try
			{
				Double.parseDouble(this.string);

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
			return this.string.charAt(0) == '"';
		}

		@Override
		public boolean isObject()
		{
			return this.string.charAt(0) == '{';
		}

		@Override
		public boolean isArray()
		{
			return this.string.charAt(0) == '[';
		}

		@Override
		public boolean getBoolean()
		{
			return this.string.equals("true");
		}

		@Override
		public double getNumber()
		{
			try
			{
				return Double.parseDouble(this.string);
			}
			catch (NumberFormatException e)
			{
				return Double.NaN;
			}
		}

		@Override
		public String getString()
		{
			return this.string.substring(1, this.string.length() - 1);
		}

		@Override
		public JSONObject.Read getObject()
		{
			return JSONObject.read(this.string);
		}

		@Override
		public JSONArray.Read getArray()
		{
			return JSONArray.read(this.string);
		}

		@Override
		public String validate()
		{
			if (this.isObject())
			{
				return JSONObject.read(this.string).validate();
			}
			else if (this.isArray())
			{
				return JSONArray.read(this.string).validate();
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
				return other.equals(JSONObject.read(this.string));
			}
			else if (this.isArray())
			{
				return other.equals(JSONArray.read(this.string));
			}
			else if (other instanceof Valid)
			{
				return this.string.equals(((Valid) other).string);
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
				return JSONObject.read(this.string).hashCode();
			}
			else if (this.isArray())
			{
				return JSONArray.read(this.string).hashCode();
			}
			else
			{
				return this.string.hashCode();
			}
		}

		@Override
		public String toString()
		{
			if (this.isObject())
			{
				return JSONObject.read(this.string).toString();
			}
			else if (this.isArray())
			{
				return JSONArray.read(this.string).toString();
			}
			else
			{
				return this.string.toString();
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
