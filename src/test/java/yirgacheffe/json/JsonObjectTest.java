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
		assertFalse(json.getBoolean("thingy"));
		assertTrue(Double.isNaN(json.getNumber("thingy")));
		assertEquals("", json.getString("thingy"));
		assertEquals("{}", json.toString());
	}

	@Test
	public void testParseEmptyObject()
	{
		JsonObject json = new JsonObject("{}");

		assertFalse(json.has("thingy"));
		assertFalse(json.getBoolean("thingy"));
		assertTrue(Double.isNaN(json.getNumber("thingy")));
		assertEquals("", json.getString("thingy"));
		assertEquals("{}", json.toString());
	}

	@Test
	public void testParseString()
	{
		JsonObject json = new JsonObject("{ \"thingy\": \"sumpt\" }");

		assertTrue(json.has("thingy"));
		assertTrue(json.getBoolean("thingy"));
		assertTrue(Double.isNaN(json.getNumber("thingy")));
		assertEquals("sumpt", json.getString("thingy"));
		assertEquals("{\"thingy\":\"sumpt\"}", json.toString());
	}

	@Test
	public void testParseEmptyString()
	{
		JsonObject json = new JsonObject("{ \"thingy\": \"\" }");

		assertTrue(json.has("thingy"));
		assertFalse(json.getBoolean("thingy"));
		assertTrue(Double.isNaN(json.getNumber("thingy")));
		assertEquals("", json.getString("thingy"));
		assertEquals("{\"thingy\":\"\"}", json.toString());
	}

	@Test
	public void testParseZeroInteger()
	{
		JsonObject json = new JsonObject("{ \"zero\": 0 }");

		assertTrue(json.has("zero"));
		assertFalse(json.getBoolean("zero"));
		assertEquals(0, json.getNumber("zero"), 0);
		assertEquals("0", json.getString("zero"));
		assertEquals("{\"zero\":0}", json.toString());
	}

	@Test
	public void testParseOneInteger()
	{
		JsonObject json = new JsonObject("{ \"one\": 1 }");

		assertTrue(json.has("one"));
		assertTrue(json.getBoolean("one"));
		assertEquals(1, json.getNumber("one"), 0);
		assertEquals("1", json.getString("one"));
		assertEquals("{\"one\":1}", json.toString());
	}

	@Test
	public void testParseZeroFloat()
	{
		JsonObject json = new JsonObject("{ \"zero\": 0.0 }");

		assertTrue(json.has("zero"));
		assertFalse(json.getBoolean("zero"));
		assertEquals(0, json.getNumber("zero"), 0);
		assertEquals("0.0", json.getString("zero"));
		assertEquals("{\"zero\":0.0}", json.toString());
	}

	@Test
	public void testParseOneFloat()
	{
		JsonObject json = new JsonObject("{ \"one\": 1.0 }");

		assertTrue(json.has("one"));
		assertTrue(json.getBoolean("one"));
		assertEquals(1, json.getNumber("one"), 0);
		assertEquals("1.0", json.getString("one"));
		assertEquals("{\"one\":1.0}", json.toString());
	}

	@Test
	public void testParseZeroExponential()
	{
		JsonObject json = new JsonObject("{ \"zero\": 0E2 }");

		assertTrue(json.has("zero"));
		assertFalse(json.getBoolean("zero"));
		assertEquals(0, json.getNumber("zero"), 0);
		assertEquals("0.0", json.getString("zero"));
		assertEquals("{\"zero\":0.0}", json.toString());
	}

	@Test
	public void testParseOneHundredExponential()
	{
		JsonObject json = new JsonObject("{ \"one\": 1e2 }");

		assertTrue(json.has("one"));
		assertTrue(json.getBoolean("one"));
		assertEquals(100, json.getNumber("one"), 0);
		assertEquals("100.0", json.getString("one"));
		assertEquals("{\"one\":100.0}", json.toString());
	}

	@Test
	public void testParseTrue()
	{
		JsonObject json = new JsonObject("{ \"tru\": true }");

		assertTrue(json.has("tru"));
		assertTrue(json.getBoolean("tru"));
		assertTrue(Double.isNaN(json.getNumber("tru")));
		assertEquals("true", json.getString("tru"));
		assertEquals("{\"tru\":true}", json.toString());
	}

	@Test
	public void testParseFalse()
	{
		JsonObject json = new JsonObject("{ \"fals\": false }");

		assertTrue(json.has("fals"));
		assertFalse(json.getBoolean("fals"));
		assertTrue(Double.isNaN(json.getNumber("fals")));
		assertEquals("false", json.getString("fals"));
		assertEquals("{\"fals\":false}", json.toString());
	}

	@Test
	public void testParseNull()
	{
		JsonObject json = new JsonObject("{ \"nul\": null }");

		assertTrue(json.has("nul"));
		assertFalse(json.getBoolean("nul"));
		assertTrue(Double.isNaN(json.getNumber("nul")));
		assertEquals("null", json.getString("nul"));
		assertEquals("{\"nul\":null}", json.toString());
	}
}
