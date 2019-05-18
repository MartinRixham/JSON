package yirgacheffe.json;

class StringValue
{
	private Object value;

	StringValue(Object value)
	{
		this.value = value;
	}

	public String getString()
	{
		if (this.value == null)
		{
			return "null";
		}
		else
		{
			return this.value.toString();
		}
	}
}
