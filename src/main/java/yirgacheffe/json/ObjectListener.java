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

		Object value = new JsonValue(valueContext, valueString).getValue();

		this.properties.put(keyString, value);
	}
}
