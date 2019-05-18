package yirgacheffe.json;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import yirgacheffe.parser.JSONParser;
import yirgacheffe.parser.JSONLexer;

import java.util.HashMap;
import java.util.Map;

public class JsonObject implements JsonData
{
	private Map<String, Object> properties = new HashMap<>();

	public JsonObject()
	{
	}

	public JsonObject(String data)
	{
		CharStream charStream = CharStreams.fromString(data);
		JSONLexer lexer = new JSONLexer(charStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		JSONParser parser = new JSONParser(tokens);
		ParseTree tree = parser.object();
		ParseTreeWalker walker = new ParseTreeWalker();

		walker.walk(new ObjectListener(this.properties), tree);
	}

	public boolean has(String property)
	{
		return this.properties.containsKey(property);
	}

	public String getString(String property)
	{
		return (String) this.properties.getOrDefault(property, "");
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder("{");

		for (String key: this.properties.keySet())
		{
			builder.append('"');
			builder.append(key);
			builder.append("\":\"");
			builder.append(this.properties.get(key).toString());
			builder.append('"');
		}

		builder.append('}');

		return builder.toString();
	}
}
