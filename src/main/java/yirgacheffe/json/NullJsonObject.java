package yirgacheffe.json;

class NullJsonObject extends JsonObject
{
	public boolean has(String property)
	{
		return false;
	}

	public boolean getBoolean(String property)
	{
		return false;
	}

	public double getNumber(String property)
	{
		return Double.NaN;
	}

	public String getString(String property)
	{
		return "";
	}

	public JsonObject getObject(String property)
	{
		return new NullJsonObject();
	}

	@Override
	public String toString()
	{
		return "";
	}
}
