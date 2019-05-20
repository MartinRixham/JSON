package yirgacheffe.json;

class NullJSONArray extends JSONArray
{
	@Override
	public boolean getBoolean(int property)
	{
		return false;
	}

	@Override
	public double getNumber(int property)
	{
		return Double.NaN;
	}

	@Override
	public String getString(int property)
	{
		return "";
	}

	@Override
	public JSONObject getObject(int property)
	{
		return new NullJSONObject();
	}

	@Override
	public JSONArray getArray(int property)
	{
		return new NullJSONArray();
	}

	@Override
	public String toString()
	{
		return "";
	}
}
