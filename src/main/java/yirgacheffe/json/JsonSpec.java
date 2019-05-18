package yirgacheffe.json;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import yirgacheffe.parser.JSONLexer;
import yirgacheffe.parser.JSONParser;

public class JsonSpec
{
	public boolean isValid(String data)
	{
		if (data == null)
		{
			return false;
		}

		ParseErrorListener errorListener = this.parseJson(data);

		return !errorListener.hasError();
	}

	public String[] getErrors(String data)
	{
		if (data == null)
		{
			return new String[0];
		}

		ParseErrorListener errorListener = this.parseJson(data);

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
