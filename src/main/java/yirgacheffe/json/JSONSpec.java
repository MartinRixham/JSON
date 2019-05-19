package yirgacheffe.json;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import yirgacheffe.parser.JSONLexer;
import yirgacheffe.parser.JSONParser;

import java.util.HashMap;
import java.util.Map;

public class JSONSpec
{
	private Map<Integer, String[]> cache = new HashMap<>();

	public boolean isValid(String data)
	{
		if (data == null)
		{
			return false;
		}

		int key = data.hashCode();

		if (this.cache.containsKey(key))
		{
			return this.cache.get(key).length == 0;
		}

		ParseErrorListener errorListener = this.parseJson(data);

		this.cache.put(key, errorListener.getErrors());

		return !errorListener.hasError();
	}

	public String[] getErrors(String data)
	{
		if (data == null)
		{
			return new String[0];
		}

		int key = data.hashCode();

		if (this.cache.containsKey(key))
		{
			return this.cache.get(key);
		}

		ParseErrorListener errorListener = this.parseJson(data);

		this.cache.put(key, errorListener.getErrors());

		return errorListener.getErrors();
	}

	private ParseErrorListener parseJson(String data)
	{
		CharStream charStream = CharStreams.fromString(data);
		JSONLexer lexer = new JSONLexer(charStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ParseErrorListener errorListener = new ParseErrorListener();

		JSONParser parser = new JSONParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(errorListener);

		ParseTree tree = parser.object();

		return errorListener;
	}
}
