package yirgacheffe.json;

class NumberValue
{
	private Object value;

	NumberValue(Object value)
	{
		this.value = value;
	}

	public double getNumber()
	{
		if (this.value instanceof Long)
		{
			return (long) this.value;
		}
		else if (this.value instanceof Double)
		{
			return (double) this.value;
		}
		else
		{
			return Double.NaN;
		}
	}
}
