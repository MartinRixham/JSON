package yirgacheffe.json;

import yirgacheffe.parser.JSONBaseListener;
import yirgacheffe.parser.JSONParser;

class ArrayListener extends JSONBaseListener
{
	private Object[] properties;

	private int index = 0;

	ArrayListener(Object[] properties)
	{
		this.properties = properties;
	}

	@Override
	public void exitValue(JSONParser.ValueContext context)
	{
		String valueString = context.getText();

		Object value = new JsonValue(context, valueString).getValue();

		this.properties[this.index++] = value;
	}
}
