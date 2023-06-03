package yirgacheffe.json;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class JSONObjectTest
{
	@Test
	public void testEmptyObject()
	{
		JSONObject.Read json = JSONObject.read("");

		assertFalse(json.has("thingy"));
		assertFalse(json.getBoolean("thingy"));
		assertTrue(Double.isNaN(json.getNumber("thingy")));
		assertEquals("", json.getString("thingy"));
		assertFalse(json.getValue("thingy") instanceof JSONValue.Valid);

		assertEquals(
			"Failed to parse object: No data.", json.getObject("thingy").toString());

		assertEquals(
			"Failed to parse object: No data.", json.getArray("thingy").toString());

		assertEquals("Failed to parse object: No data.", json.validate());
		assertNotEquals(JSONObject.read("{}"), json);
		assertNotEquals(JSONObject.read("{}").hashCode(), json.hashCode());
	}

	@Test
	public void testParseEmptyObject()
	{
		JSONObject.Read json = JSONObject.read("{}");

		assertFalse(json.has("thingy"));
		assertFalse(json.getBoolean("thingy"));
		assertTrue(Double.isNaN(json.getNumber("thingy")));
		assertEquals("", json.getString("thingy"));
		assertFalse(json.getValue("thingy") instanceof JSONValue.Valid);

		assertEquals(
			"Failed to read object with key \"thingy\".",
			json.getObject("thingy").toString());

		assertEquals(
			"Failed to read array with key \"thingy\".",
			json.getArray("thingy").toString());

		assertEquals("", json.validate());
		assertEquals(JSONObject.read("{}"), json);
		assertEquals(JSONObject.read("{}").hashCode(), json.hashCode());
	}

	@Test
	public void testParseString()
	{
		JSONObject.Read json = JSONObject.read("{ \"thingy\": \"sumpt\" }");

		assertTrue(json.has("thingy"));
		assertTrue(json.getBoolean("thingy"));
		assertTrue(Double.isNaN(json.getNumber("thingy")));
		assertEquals("sumpt", json.getString("thingy"));
		assertTrue(json.getValue("thingy") instanceof JSONValue.Valid);

		assertEquals(
			"Failed to parse object: Started with \" instead of {.",
			json.getObject("thingy").toString());

		assertEquals(
			"Failed to parse array: Started with \" instead of [.",
			json.getArray("thingy").toString());

		assertEquals("", json.validate());
		assertNotEquals(JSONObject.read("{}"), json);
		assertNotEquals(JSONObject.read("{}").hashCode(), json.hashCode());
	}

	@Test
	public void testParseEmptyString()
	{
		JSONObject.Read json = JSONObject.read("{ \"thingy\": \"\" }");

		assertTrue(json.has("thingy"));
		assertFalse(json.getBoolean("thingy"));
		assertTrue(Double.isNaN(json.getNumber("thingy")));
		assertEquals("", json.getString("thingy"));
		assertTrue(json.getValue("thingy") instanceof JSONValue.Valid);

		assertEquals(
			"Failed to parse object: Started with \" instead of {.",
			json.getObject("thingy").toString());

		assertEquals(
			"Failed to parse array: Started with \" instead of [.",
			json.getArray("thingy").toString());

		assertEquals("", json.validate());
		assertNotEquals(JSONObject.read("{}"), json);
		assertNotEquals(JSONObject.read("{}").hashCode(), json.hashCode());
	}

	@Test
	public void testParseEscapedString()
	{
		JSONObject.Read json = JSONObject.read("{ \"th\\\"ingy\": \"\\\"\" }");

		assertTrue(json.has("th\\\"ingy"));
		assertTrue(json.getBoolean("th\\\"ingy"));
		assertTrue(Double.isNaN(json.getNumber("th\\\"ingy")));
		assertEquals("\\\"", json.getString("th\\\"ingy"));
		assertTrue(json.getValue("th\\\"ingy") instanceof JSONValue.Valid);

		assertEquals("", json.validate());
		assertNotEquals(JSONObject.read("{}"), json);
		assertNotEquals(JSONObject.read("{}").hashCode(), json.hashCode());
	}

	@Test
	public void testParseDuplicateKey()
	{
		JSONObject.Read json = JSONObject.read("{ \"thingy\": \"\", \"thingy\": 0 }");

		assertFalse(json.has("thingy"));
		assertFalse(json.getBoolean("thingy"));
		assertTrue(Double.isNaN(json.getNumber("thingy")));
		assertEquals("", json.getString("thingy"));
		assertFalse(json.getValue("thingy") instanceof JSONValue.Valid);

		assertEquals(
			"Failed to parse object: Duplicate key \"thingy\".", json.validate());

		assertNotEquals(JSONObject.read("{}"), json);
		assertNotEquals(JSONObject.read("{}").hashCode(), json.hashCode());
	}

	@Test
	public void testParseZeroInteger()
	{
		JSONObject.Read json = JSONObject.read("{ \"zero\": 0 }");

		assertTrue(json.has("zero"));
		assertFalse(json.getBoolean("zero"));
		assertEquals(0, json.getNumber("zero"), 0);
		assertEquals("0", json.getString("zero"));
		assertTrue(json.getValue("zero") instanceof JSONValue.Valid);

		assertEquals(
			"Failed to parse object: Started with 0 instead of {.",
			json.getObject("zero").toString());

		assertEquals(
			"Failed to parse array: Started with 0 instead of [.",
			json.getArray("zero").toString());

		assertEquals("", json.validate());
		assertNotEquals(JSONObject.read("{}"), json);
		assertNotEquals(JSONObject.read("{}").hashCode(), json.hashCode());
	}

	@Test
	public void testParseOneInteger()
	{
		JSONObject.Read json = JSONObject.read("{ \"one\": 1 }");

		assertTrue(json.has("one"));
		assertTrue(json.getBoolean("one"));
		assertEquals(1, json.getNumber("one"), 0);
		assertEquals("1", json.getString("one"));
		assertTrue(json.getValue("one") instanceof JSONValue.Valid);

		assertEquals(
			"Failed to parse object: Started with 1 instead of {.",
			json.getObject("one").toString());

		assertEquals(
			"Failed to parse array: Started with 1 instead of [.",
			json.getArray("one").toString());

		assertEquals("", json.validate());
		assertNotEquals(JSONObject.read("{}"), json);
		assertNotEquals(JSONObject.read("{}").hashCode(), json.hashCode());
	}

	@Test
	public void testParseInteger()
	{
		JSONObject.Read json = JSONObject.read("{ \"twenty-two\": 22 }");

		assertTrue(json.has("twenty-two"));
		assertTrue(json.getBoolean("twenty-two"));
		assertEquals(22, json.getNumber("twenty-two"), 0);
		assertEquals("22", json.getString("twenty-two"));
		assertTrue(json.getValue("twenty-two") instanceof JSONValue.Valid);

		assertEquals("", json.validate());
		assertNotEquals(JSONObject.read("{}"), json);
		assertNotEquals(JSONObject.read("{}").hashCode(), json.hashCode());
	}

	@Test
	public void testParseZeroFloat()
	{
		JSONObject.Read json = JSONObject.read("{ \"zero\": 0.0 }");

		assertTrue(json.has("zero"));
		assertFalse(json.getBoolean("zero"));
		assertEquals(0, json.getNumber("zero"), 0);
		assertEquals("0.0", json.getString("zero"));
		assertTrue(json.getValue("zero") instanceof JSONValue.Valid);

		assertEquals(
			"Failed to parse object: Started with 0 instead of {.",
			json.getObject("zero").toString());

		assertEquals(
			"Failed to parse array: Started with 0 instead of [.",
			json.getArray("zero").toString());

		assertEquals("", json.validate());
		assertNotEquals(JSONObject.read("{}"), json);
		assertNotEquals(JSONObject.read("{}").hashCode(), json.hashCode());
	}

	@Test
	public void testParseOneFloat()
	{
		JSONObject.Read json = JSONObject.read("{ \"one\": 1.0 }");

		assertTrue(json.has("one"));
		assertTrue(json.getBoolean("one"));
		assertEquals(1, json.getNumber("one"), 0);
		assertEquals("1.0", json.getString("one"));
		assertTrue(json.getValue("one") instanceof JSONValue.Valid);

		assertEquals("", json.validate());
		assertNotEquals(JSONObject.read("{}"), json);
		assertNotEquals(JSONObject.read("{}").hashCode(), json.hashCode());
	}

	@Test
	public void testParseZeroExponential()
	{
		JSONObject.Read json = JSONObject.read("{ \"zero\": 0E2 }");

		assertTrue(json.has("zero"));
		assertFalse(json.getBoolean("zero"));
		assertEquals(0, json.getNumber("zero"), 0);
		assertEquals("0E2", json.getString("zero"));
		assertTrue(json.getValue("zero") instanceof JSONValue.Valid);

		assertEquals("", json.validate());
		assertNotEquals(JSONObject.read("{}"), json);
		assertNotEquals(JSONObject.read("{}").hashCode(), json.hashCode());
	}

	@Test
	public void testParseOneHundredExponential()
	{
		JSONObject.Read json = JSONObject.read("{ \"one\": 1e2 }");

		assertTrue(json.has("one"));
		assertTrue(json.getBoolean("one"));
		assertEquals(100, json.getNumber("one"), 0);
		assertEquals("1e2", json.getString("one"));
		assertTrue(json.getValue("one") instanceof JSONValue.Valid);

		assertEquals("", json.validate());
		assertNotEquals(JSONObject.read("{}"), json);
		assertNotEquals(JSONObject.read("{}").hashCode(), json.hashCode());
	}

	@Test
	public void testParseTrue()
	{
		JSONObject.Read json = JSONObject.read("{ \"tru\": true }");

		assertTrue(json.has("tru"));
		assertTrue(json.getBoolean("tru"));
		assertTrue(Double.isNaN(json.getNumber("tru")));
		assertEquals("true", json.getString("tru"));
		assertTrue(json.getValue("tru") instanceof JSONValue.Valid);

		assertEquals("", json.validate());
		assertNotEquals(JSONObject.read("{}"), json);
		assertNotEquals(JSONObject.read("{}").hashCode(), json.hashCode());
	}

	@Test
	public void testParseFalse()
	{
		JSONObject.Read json = JSONObject.read("{ \"fals\": false }");

		assertTrue(json.has("fals"));
		assertFalse(json.getBoolean("fals"));
		assertTrue(Double.isNaN(json.getNumber("fals")));
		assertEquals("false", json.getString("fals"));
		assertTrue(json.getValue("fals") instanceof JSONValue.Valid);

		assertEquals("", json.validate());
		assertNotEquals(JSONObject.read("{}"), json);
		assertNotEquals(JSONObject.read("{}").hashCode(), json.hashCode());
	}

	@Test
	public void testParseNull()
	{
		JSONObject.Read json = JSONObject.read("{ \"nul\": null }");

		assertTrue(json.has("nul"));
		assertFalse(json.getBoolean("nul"));
		assertTrue(Double.isNaN(json.getNumber("nul")));
		assertEquals("null", json.getString("nul"));
		assertTrue(json.getValue("nul") instanceof JSONValue.Valid);

		assertEquals("", json.validate());
		assertNotEquals(JSONObject.read("{}"), json);
		assertNotEquals(JSONObject.read("{}").hashCode(), json.hashCode());
	}

	@Test
	public void testInvalidJson()
	{
		JSONObject.Read json = JSONObject.read("{ \"nul\": null");

		assertEquals(
			"Failed to parse object: Ran out of characters before end of object.",
			json.toString());

		assertEquals(
			"Failed to parse object: Ran out of characters before end of object.",
			json.validate());

		assertFalse(json.getValue("nul") instanceof JSONValue.Valid);
		assertNotEquals(JSONObject.read("{}"), json);
		assertNotEquals(JSONObject.read("{}").hashCode(), json.hashCode());
		assertEquals(JSONObject.read("{ \"nul\": null"), json);
		assertNotEquals(JSONValue.read("{ \"nul\": null"), json);
		assertNotEquals("{ \"nul\": null", json);
		assertNotEquals(JSONObject.read(""), json);
		assertNotEquals(JSONObject.write().read(), json);
	}

	@Test
	public void testObjectWithChildObject()
	{
		JSONObject.Read json = JSONObject.read("{ \"obj\": {} }");

		assertTrue(json.has("obj"));
		assertTrue(json.getBoolean("obj"));
		assertTrue(Double.isNaN(json.getNumber("obj")));
		assertEquals("{}", json.getString("obj"));
		assertTrue(json.getValue("obj") instanceof JSONValue.Valid);
		assertEquals(JSONObject.read("{}"), json.getObject("obj"));

		assertEquals(
			"Failed to parse array: Started with { instead of [.",
			json.getArray("obj").toString());

		assertEquals("", json.validate());
		assertNotEquals(JSONObject.read("{}"), json);
		assertNotEquals(JSONObject.read("{}").hashCode(), json.hashCode());
	}

	@Test
	public void testObjectWithChildArray()
	{
		JSONObject.Read json = JSONObject.read("{ \"arr\": [] }");

		assertTrue(json.has("arr"));
		assertTrue(json.getBoolean("arr"));
		assertTrue(Double.isNaN(json.getNumber("arr")));
		assertEquals("[]", json.getString("arr"));
		assertTrue(json.getValue("arr") instanceof JSONValue.Valid);

		assertEquals(
			"Failed to parse object: Started with [ instead of {.",
			json.getObject("arr").toString());

		assertEquals(JSONArray.read("[]"), json.getArray("arr"));
		assertEquals("", json.validate());
		assertNotEquals(JSONObject.read("{}"), json);
		assertNotEquals(JSONObject.read("{}").hashCode(), json.hashCode());
	}

	@Test
	public void testPutProperties()
	{
		JSONObject.Write json = JSONObject.write();

		json.put("obj", JSONObject.write());
		json.put("arr", JSONArray.write());
		json.put("notherObj", JSONObject.write());
		json.put("notherArr", JSONArray.write());
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

		assertEquals("", json.read().validate());
	}

	@Test
	public void testPutChainedProperties()
	{
		JSONObject.Write json = JSONObject.write();

		json.put("obj", JSONObject.write())
			.put("arr", JSONArray.write())
			.put("notherObj", JSONObject.write())
			.put("notherArr", JSONArray.write())
			.put("thingy", "sumpt")
			.put("int", 1)
			.put("float", 1.0)
			.put("exp", 1e2)
			.put("tru", true)
			.put("fals", false);

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
				"\"tru\":true," +
				"\"fals\":false" +
			"}",
			json.toString());
	}

	@Test
	public void testGetDeepObjectProperty()
	{
		JSONObject.Read json =
			JSONObject.read(
				"{" +
					"\"arr\":" +
						"[" +
							"1," +
							"2," +
							"{" +
								"\"wibble\":" +
									"{" +
										"\"this\":\"that\"," +
										"\"thingy\":[[8,8,8,\"sumpt\"]]" +
									"}" +
							"}" +
						"]" +
				"}");

		assertEquals("sumpt",
			json.getArray("arr")
				.getObject(2)
				.getObject("wibble")
				.getArray("thingy")
				.getArray(0)
				.getString(3));

		assertEquals("", json.validate());
		assertNotEquals(JSONObject.read("{}"), json);
		assertNotEquals(JSONObject.read("{}").hashCode(), json.hashCode());

		assertEquals("{\n" +
			"    \"arr\":\n" +
			"        [\n" +
			"            1,\n" +
			"            2,\n" +
			"            {\n" +
			"                \"wibble\":\n" +
			"                    {\n" +
			"                        \"thingy\":\n" +
			"                            [\n" +
			"                                [8,8,8,\"sumpt\"]\n" +
			"                            ],\n" +
			"                        \"this\": \"that\"\n" +
			"                    }\n" +
			"            }\n" +
			"        ]\n" +
			"}", json.toString());
	}
}
