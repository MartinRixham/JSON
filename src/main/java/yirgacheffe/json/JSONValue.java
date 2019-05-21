package yirgacheffe.json;

import yirgacheffe.parser.JSONParser;

final class JSONValue
{
	private JSONValue()
	{
	}

	public static Object getValue(JSONParser.ValueContext context, String valueString)
	{
		Object value = null;

		if (context.array() != null)
		{
			value = new JSONArray(context.array().value().toArray());;
		}
		if (context.object() != null)
		{
			JSONObject object = new JSONObject();
			object.parse(context.object());

			value = object;
		}
		if (context.STRING() != null)
		{
			value = valueString.substring(1, valueString.length() - 1);
		}
		else if (context.NUMBER() != null)
		{
			if (valueString.contains(".") ||
				valueString.contains("e") ||
				valueString.contains("E"))
			{
				value = Double.valueOf(valueString);
			}
			else
			{
				value = Long.valueOf(valueString);
			}
		}
		else if (context.TRUE() != null)
		{
			value = true;
		}
		else if (context.FALSE() != null)
		{
			value = false;
		}

		return value;
	}
}
