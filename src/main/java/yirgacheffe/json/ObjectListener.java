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
		String value = context.value().getText();
		String keyString = key.substring(1, key.length() - 1);
		String valueString = value.substring(1, value.length() - 1);

		this.properties.put(keyString, valueString);
	}
}
