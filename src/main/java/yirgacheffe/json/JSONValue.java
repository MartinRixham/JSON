package yirgacheffe.json;

import yirgacheffe.parser.JSONParser;

final class JSONValue
{
	private JSONValue()
	{
	}

	public static boolean getBoolean(Object value)
	{
		if (value instanceof JSONParser.ValueContext)
		{
			value = getValue(value);
		}

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

	public static double getNumber(Object value)
	{
		if (value instanceof JSONParser.ValueContext)
		{
			value = getValue(value);
		}

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

	public static String getString(Object value)
	{
		if (value instanceof JSONParser.ValueContext)
		{
			value = getValue(value);
		}

		if (value == null)
		{
			return "null";
		}
		else
		{
			return value.toString();
		}
	}

	public static JSONObject getObject(Object value)
	{
		if (value instanceof JSONParser.ValueContext)
		{
			value = getValue(value);
		}

		if (value instanceof JSONObject)
		{
			return (JSONObject) value;
		}
		else
		{
			return new NullJSONObject();
		}
	}

	public static JSONArray getArray(Object value)
	{
		if (value instanceof JSONParser.ValueContext)
		{
			value = getValue(value);
		}

		if (value instanceof JSONArray)
		{
			return (JSONArray) value;
		}
		else
		{
			return new NullJSONArray();
		}
	}

	private static Object getValue(Object value)
	{
		JSONParser.ValueContext context = (JSONParser.ValueContext) value;

		if (context.array() != null)
		{
			return new JSONArray(context.array().value().toArray());
		}
		if (context.object() != null)
		{
			return new JSONObject(context.object().property().toArray());
		}
		if (context.STRING() != null)
		{
			String valueString = context.getText();

			return valueString.substring(1, valueString.length() - 1);
		}
		else if (context.NUMBER() != null)
		{
			String valueString = context.getText();

			if (valueString.contains(".") ||
				valueString.contains("e") ||
				valueString.contains("E"))
			{
				return Double.valueOf(valueString);
			}
			else
			{
				return Long.valueOf(valueString);
			}
		}
		else if (context.TRUE() != null)
		{
			return true;
		}
		else if (context.FALSE() != null)
		{
			return false;
		}
		else
		{
			return null;
		}
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
