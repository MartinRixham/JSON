package yirgacheffe.json;

class NullJSONObject extends JSONObject
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
	public JSONObject getObject(String property)
	{
		return new NullJSONObject();
	}

	@Override
	public JSONArray getArray(String property)
	{
		return new NullJSONArray();
	}

	@Override
	public String toString()
	{
		return "";
	}
}
