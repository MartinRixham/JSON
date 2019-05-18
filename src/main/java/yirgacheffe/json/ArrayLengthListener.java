package yirgacheffe.json;

import yirgacheffe.parser.JSONBaseListener;
import yirgacheffe.parser.JSONParser;

class ArrayLengthListener extends JSONBaseListener
{
	private int length = 0;

	@Override
	public void exitValue(JSONParser.ValueContext context)
	{
		this.length++;
	}

	public int getLength()
	{
		return this.length;
	}
}
