package yirgacheffe.json;

import yirgacheffe.parser.JSONParser;

public class JsonValue
{
	private JSONParser.ValueContext context;

	private String valueString;

	public JsonValue(JSONParser.ValueContext context, String valueString)
	{
		this.context = context;
		this.valueString = valueString;
	}

	public Object getValue()
	{
		String valueString = this.valueString;
		JSONParser.ValueContext context = this.context;
		Object value = null;

		if (context.object() != null)
		{
			value = new JsonObject(valueString);
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
