package yirgacheffe.json;

import yirgacheffe.parser.JSONBaseListener;
import yirgacheffe.parser.JSONParser;

import java.util.Map;

class ObjectListener extends JSONBaseListener
{
	private Map<String, Object> properties;

	ObjectListener(Map<String, Object> properties)
	{
		this.properties = properties;
	}

	@Override
	public void exitProperty(JSONParser.PropertyContext context)
	{
		String key = context.STRING().getText();
		String keyString = key.substring(1, key.length() - 1);
		JSONParser.ValueContext valueContext = context.value();
		String valueString = valueContext.getText();

		Object value;

		if (valueContext.STRING() != null)
		{
			value = valueString.substring(1, valueString.length() - 1);
		}
		else if (valueContext.NUMBER() != null)
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
		else
		{
			throw new RuntimeException("Unsupported JSON value type.");
		}

		this.properties.put(keyString, value);
	}
}
