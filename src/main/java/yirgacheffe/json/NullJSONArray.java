package yirgacheffe.json;

class NullJSONArray extends JSONArray
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
	public JSONObject getObject(double property)
	{
		return new NullJSONObject();
	}

	@Override
	public JSONObject getObject(int property)
	{
		return new NullJSONObject();
	}

	@Override
	public JSONArray getArray(double property)
	{
		return new NullJSONArray();
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
