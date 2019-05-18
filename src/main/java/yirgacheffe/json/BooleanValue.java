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
		if (this.value instanceof JsonObject)
		{
			return true;
		}
		if (this.value instanceof String)
		{
			return ((String) this.value).length() > 0;
		}
		else if (this.value instanceof Long)
		{
			return ((long) this.value) != 0;
		}
		else if (this.value instanceof Double)
		{
			return ((double) this.value) != 0;
		}
		else if (this.value instanceof Boolean)
		{
			return this.value.equals(true);
		}
		else
		{
			return false;
		}
	}
}
