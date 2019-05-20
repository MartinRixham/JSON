package yirgacheffe.json;

final class PropertyValue
{
	private PropertyValue()
	{
	}

	public static boolean getBoolean(Object value)
	{
		if (value instanceof JSONArray || value instanceof JSONObject)
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

	public static double getNumber(Object value)
	{
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

	public static String getString(Object value)
	{
		if (value == null)
		{
			return "null";
		}
		else
		{
			return value.toString();
		}
	}
}
