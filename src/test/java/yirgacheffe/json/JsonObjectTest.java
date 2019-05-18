package yirgacheffe.json;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JsonObjectTest
{
	@Test
	public void testEmptyObject()
	{
		JsonObject json = new JsonObject();

		assertFalse(json.has("thingy"));
		assertEquals("", json.getString("thingy"));
		assertEquals("{}", json.toString());
	}

	@Test
	public void testParseEmptyObject()
	{
		JsonObject json = new JsonObject("{}");

		assertFalse(json.has("thingy"));
		assertEquals("", json.getString("thingy"));
		assertEquals("{}", json.toString());
	}

	@Test
	public void testParseString()
	{
		JsonObject json = new JsonObject("{ \"thingy\": \"sumpt\" }");

		assertTrue(json.has("thingy"));
		assertEquals("sumpt", json.getString("thingy"));
		assertEquals("{\"thingy\":\"sumpt\"}", json.toString());
	}
}
