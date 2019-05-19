package yirgacheffe.json;

class BooleanValue
{
	private Object value;

	BooleanValue(Object value)
	{
		this.value = value;
	}

	boolean getBoolean()
	{
		Object value = this.value;

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
}
