package yirgacheffe.json;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

class ParseErrorListener extends BaseErrorListener
{
	private List<String> errors = new ArrayList<>();

	@Override
	public void syntaxError(
		Recognizer<?, ?> recognizer,
		Object offendingSymbol,
		int line,
		int charPosition,
		String message,
		RecognitionException e)
	{
		this.errors.add("line " + line + ":" + charPosition + " " + message);
	}

	public boolean hasError()
	{
		return this.errors.size() > 0;
	}

	public String[] getErrors()
	{
		return this.errors.toArray(new String[0]);
	}
}
