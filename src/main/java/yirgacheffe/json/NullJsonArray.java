package yirgacheffe.json;

class NullJsonArray extends JsonArray
{
	@Override
	public boolean getBoolean(double property)
	{
		return false;
	}

	@Override
	public boolean getBoolean(int property)
	{
		return false;
	}

	@Override
	public double getNumber(double property)
	{
		return Double.NaN;
	}

	@Override
	public double getNumber(int property)
	{
		return Double.NaN;
	}

	@Override
	public String getString(double property)
	{
		return "";
	}

	@Override
	public JsonObject getObject(double property)
	{
		return new NullJsonObject();
	}

	@Override
	public JsonObject getObject(int property)
	{
		return new NullJsonObject();
	}

	@Override
	public JsonArray getArray(double property)
	{
		return new NullJsonArray();
	}

	@Override
	public JsonArray getArray(int property)
	{
		return new NullJsonArray();
	}

	@Override
	public String toString()
	{
		return "";
	}
}
