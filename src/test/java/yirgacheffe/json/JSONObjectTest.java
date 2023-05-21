package yirgacheffe.json;

import org.junit.Test;

import jakarta.json.JsonException;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JSONObjectTest
{
	@Test
	public void testEmptyObject()
	{
		JSONObject json = new JSONObject();

		assertFalse(json.has("thingy"));
		assertFalse(json.getBoolean("thingy"));
		assertTrue(Double.isNaN(json.getNumber("thingy")));
		assertEquals("", json.getString("thingy"));
		assertEquals("", json.getObject("thingy").toString());
		assertEquals("", json.getArray("thingy").toString());
		assertEquals("{}", json.toString());
	}

	@Test
	public void testParseEmptyObject()
	{
		JSONObject json = new JSONObject("{}");

		assertFalse(json.has("thingy"));
		assertFalse(json.getBoolean("thingy"));
		assertTrue(Double.isNaN(json.getNumber("thingy")));
		assertEquals("", json.getString("thingy"));
		assertEquals("", json.getObject("thingy").toString());
		assertEquals("", json.getArray("thingy").toString());
		assertEquals("{}", json.toString());
	}

	@Test
	public void testParseString()
	{
		JSONObject json = new JSONObject("{ \"thingy\": \"sumpt\" }");

		assertTrue(json.has("thingy"));
		assertTrue(json.getBoolean("thingy"));
		assertTrue(Double.isNaN(json.getNumber("thingy")));
		assertEquals("sumpt", json.getString("thingy"));
		assertEquals("", json.getObject("thingy").toString());
		assertEquals("", json.getArray("thingy").toString());
		assertEquals("{\"thingy\":\"sumpt\"}", json.toString());
	}

	@Test
	public void testParseEmptyString()
	{
		JSONObject json = new JSONObject("{ \"thingy\": \"\" }");

		assertTrue(json.has("thingy"));
		assertFalse(json.getBoolean("thingy"));
		assertTrue(Double.isNaN(json.getNumber("thingy")));
		assertEquals("", json.getString("thingy"));
		assertEquals("", json.getObject("thingy").toString());
		assertEquals("", json.getArray("thingy").toString());
		assertEquals("{\"thingy\":\"\"}", json.toString());
	}

	@Test
	public void testParseZeroInteger()
	{
		JSONObject json = new JSONObject("{ \"zero\": 0 }");

		assertTrue(json.has("zero"));
		assertFalse(json.getBoolean("zero"));
		assertEquals(0, json.getNumber("zero"), 0);
		assertEquals("0", json.getString("zero"));
		assertEquals("", json.getObject("zero").toString());
		assertEquals("", json.getArray("zero").toString());
		assertEquals("{\"zero\":0}", json.toString());
	}

	@Test
	public void testParseOneInteger()
	{
		JSONObject json = new JSONObject("{ \"one\": 1 }");

		assertTrue(json.has("one"));
		assertTrue(json.getBoolean("one"));
		assertEquals(1, json.getNumber("one"), 0);
		assertEquals("1", json.getString("one"));
		assertEquals("", json.getObject("one").toString());
		assertEquals("", json.getArray("one").toString());
		assertEquals("{\"one\":1}", json.toString());
	}

	@Test
	public void testParseZeroFloat()
	{
		JSONObject json = new JSONObject("{ \"zero\": 0.0 }");

		assertTrue(json.has("zero"));
		assertFalse(json.getBoolean("zero"));
		assertEquals(0, json.getNumber("zero"), 0);
		assertEquals("0.0", json.getString("zero"));
		assertEquals("", json.getObject("zero").toString());
		assertEquals("", json.getArray("zero").toString());
		assertEquals("{\"zero\":0.0}", json.toString());
	}

	@Test
	public void testParseOneFloat()
	{
		JSONObject json = new JSONObject("{ \"one\": 1.0 }");

		assertTrue(json.has("one"));
		assertTrue(json.getBoolean("one"));
		assertEquals(1, json.getNumber("one"), 0);
		assertEquals("1.0", json.getString("one"));
		assertEquals("", json.getObject("one").toString());
		assertEquals("", json.getArray("one").toString());
		assertEquals("{\"one\":1.0}", json.toString());
	}

	@Test
	public void testParseZeroExponential()
	{
		JSONObject json = new JSONObject("{ \"zero\": 0E2 }");

		assertTrue(json.has("zero"));
		assertFalse(json.getBoolean("zero"));
		assertEquals(0, json.getNumber("zero"), 0);
		assertEquals("0E+2", json.getString("zero"));
		assertEquals("", json.getObject("zero").toString());
		assertEquals("", json.getArray("zero").toString());
		assertEquals("{\"zero\":0E+2}", json.toString());
	}

	@Test
	public void testParseOneHundredExponential()
	{
		JSONObject json = new JSONObject("{ \"one\": 1e2 }");

		assertTrue(json.has("one"));
		assertTrue(json.getBoolean("one"));
		assertEquals(100, json.getNumber("one"), 0);
		assertEquals("1E+2", json.getString("one"));
		assertEquals("", json.getObject("one").toString());
		assertEquals("", json.getArray("one").toString());
		assertEquals("{\"one\":1E+2}", json.toString());
	}

	@Test
	public void testParseTrue()
	{
		JSONObject json = new JSONObject("{ \"tru\": true }");

		assertTrue(json.has("tru"));
		assertTrue(json.getBoolean("tru"));
		assertTrue(Double.isNaN(json.getNumber("tru")));
		assertEquals("true", json.getString("tru"));
		assertEquals("", json.getObject("tru").toString());
		assertEquals("", json.getArray("tru").toString());
		assertEquals("{\"tru\":true}", json.toString());
	}

	@Test
	public void testParseFalse()
	{
		JSONObject json = new JSONObject("{ \"fals\": false }");

		assertTrue(json.has("fals"));
		assertFalse(json.getBoolean("fals"));
		assertTrue(Double.isNaN(json.getNumber("fals")));
		assertEquals("false", json.getString("fals"));
		assertEquals("", json.getObject("fals").toString());
		assertEquals("", json.getArray("fals").toString());
		assertEquals("{\"fals\":false}", json.toString());
	}

	@Test
	public void testParseNull()
	{
		JSONObject json = new JSONObject("{ \"nul\": null }");

		assertTrue(json.has("nul"));
		assertFalse(json.getBoolean("nul"));
		assertTrue(Double.isNaN(json.getNumber("nul")));
		assertEquals("null", json.getString("nul"));
		assertEquals("", json.getObject("nul").toString());
		assertEquals("", json.getArray("nul").toString());
		assertEquals("{\"nul\":null}", json.toString());
	}

	@Test
	public void testInvalidJson()
	{
		PrintStream originalError = System.err;

		OutputStream spyError = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(spyError);

		System.setErr(printStream);

		JsonException exception = null;

		try
		{
			JSONObject json = new JSONObject("{ \"nul\": null");
		}
		catch (JsonException e)
		{
			exception = e;
		}

		assertNotNull(exception);

		assertEquals(
			"Invalid token=EOF at (line no=1, column no=39, offset=38)." +
				" Expected tokens are: [COMMA, CURLYCLOSE]",
			exception.getMessage());

		assertEquals("", spyError.toString());

		System.setErr(originalError);
	}

	@Test
	public void testObjectWithChildObject()
	{
		JSONObject json = new JSONObject("{ \"obj\": {} }");

		assertTrue(json.has("obj"));
		assertTrue(json.getBoolean("obj"));
		assertTrue(Double.isNaN(json.getNumber("obj")));
		assertEquals("{}", json.getString("obj"));
		assertEquals("{}", json.getObject("obj").toString());
		assertEquals("", json.getArray("obj").toString());
		assertEquals("{\"obj\":{}}", json.toString());
	}

	@Test
	public void testObjectWithChildArray()
	{
		JSONObject json = new JSONObject("{ \"arr\": [] }");

		assertTrue(json.has("arr"));
		assertTrue(json.getBoolean("arr"));
		assertTrue(Double.isNaN(json.getNumber("arr")));
		assertEquals("[]", json.getString("arr"));
		assertEquals("", json.getObject("arr").toString());
		assertEquals("[]", json.getArray("arr").toString());
		assertEquals("{\"arr\":[]}", json.toString());
	}

	@Test
	public void testPutProperties()
	{
		JSONObject json = new JSONObject();

		json.put("obj", new JSONObject());
		json.put("arr", new JSONArray());
		json.put("notherObj", new JSONObject("{}"));
		json.put("notherArr", new JSONArray("[]"));
		json.put("thingy", "sumpt");
		json.put("int", 1);
		json.put("float", 1.0);
		json.put("exp", 1e2);
		json.put("tru", true);

		assertEquals(
			"{" +
				"\"obj\":{}," +
				"\"arr\":[]," +
				"\"notherObj\":{}," +
				"\"notherArr\":[]," +
				"\"thingy\":\"sumpt\"," +
				"\"int\":1," +
				"\"float\":1.0," +
				"\"exp\":100.0," +
				"\"tru\":true" +
			"}",
			json.toString());
	}

	@Test
	public void testPutPropertiesOnEmptyObject()
	{
		JSONObject json = new JSONObject("{}");

		json.put("obj", new JSONObject());
		json.put("arr", new JSONArray());
		json.put("notherObj", new JSONObject("{}"));
		json.put("notherArr", new JSONArray("[]"));
		json.put("thingy", "sumpt");
		json.put("int", 1);
		json.put("float", 1.0);
		json.put("exp", 1e2);
		json.put("tru", true);

		assertEquals(
			"{" +
				"\"obj\":{}," +
				"\"arr\":[]," +
				"\"notherObj\":{}," +
				"\"notherArr\":[]," +
				"\"thingy\":\"sumpt\"," +
				"\"int\":1," +
				"\"float\":1.0," +
				"\"exp\":100.0," +
				"\"tru\":true" +
			"}",
			json.toString());
	}

	@Test
	public void testGetDeepObjectProperty()
	{
		JSONObject json =
			new JSONObject(
				"{ \"arr\": [1,2,{\"wibble\":{\"thingy\":[[8,8,8,\"sumpt\"]]}}] }");

		assertEquals("sumpt",
			json.getArray("arr")
				.getObject(2)
				.getObject("wibble")
				.getArray("thingy")
				.getArray(0)
				.getString(3));
	}
}
