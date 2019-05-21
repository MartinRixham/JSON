package yirgacheffe.json;

import yirgacheffe.parser.JSONParser;

final class JSONValue
{
	private JSONValue()
	{
	}

	public static boolean getBoolean(JSONParser.ValueContext context)
	{
		Object value = getValue(context);

		if (value instanceof JSONArray || value instanceof JSONObject)
		{
			return true;
		}
		if (value instanceof String)
		{
			return ((String) value).length() > 0;
		}
		else if (value instanceof Long)
		{
			return ((long) value) != 0;
		}
		else if (value instanceof Double)
		{
			return ((double) value) != 0;
		}
		else if (value instanceof Boolean)
		{
			return value.equals(true);
		}
		else
		{
			return false;
		}
	}

	public static double getNumber(JSONParser.ValueContext context)
	{
		Object value = getValue(context);

		if (value instanceof Long)
		{
			return (long) value;
		}
		else if (value instanceof Double)
		{
			return (double) value;
		}
		else
		{
			return Double.NaN;
		}
	}

	public static String getString(JSONParser.ValueContext context)
	{
		Object value = getValue(context);

		if (value == null)
		{
			return "null";
		}
		else
		{
			return value.toString();
		}
	}

	public static JSONObject getObject(JSONParser.ValueContext context)
	{
		Object value = getValue(context);

		if (value instanceof JSONObject)
		{
			return (JSONObject) value;
		}
		else
		{
			return new NullJSONObject();
		}
	}

	public static JSONArray getArray(JSONParser.ValueContext context)
	{
		Object value = getValue(context);

		if (value instanceof JSONArray)
		{
			return (JSONArray) value;
		}
		else
		{
			return new NullJSONArray();
		}
	}

	private static Object getValue(JSONParser.ValueContext context)
	{
		String valueString = context.getText();
		Object value = null;

		if (context.array() != null)
		{
			value = new JSONArray(context.array().value().toArray());
		}
		if (context.object() != null)
		{
			value = new JSONObject(context.object().property().toArray());
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

	static void appendValueString(StringBuilder builder, Object value)
	{
		if (value instanceof JSONParser.ValueContext)
		{
			builder.append(((JSONParser.ValueContext) value).getText());
		}
		else if (value instanceof String)
		{
			builder.append('"');
			builder.append(value.toString());
			builder.append('"');
		}
		else if (value == null)
		{
			builder.append("null");
		}
		else
		{
			builder.append(value.toString());
		}
	}
}
