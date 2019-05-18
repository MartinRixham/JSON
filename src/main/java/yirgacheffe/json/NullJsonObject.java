package yirgacheffe.json;

class NullJsonObject extends JsonObject
{
	@Override
	public boolean has(String property)
	{
		return false;
	}

	@Override
	public boolean getBoolean(String property)
	{
		return false;
	}

	@Override
	public double getNumber(String property)
	{
		return Double.NaN;
	}

	@Override
	public String getString(String property)
	{
		return "";
	}

	@Override
	public JsonObject getObject(String property)
	{
		return new NullJsonObject();
	}

	@Override
	public JsonArray getArray(String property)
	{
		return new NullJsonArray();
	}

	@Override
	public String toString()
	{
		return "";
	}
}
