package yirgacheffe.json.test;

import org.junit.Assert;
import org.junit.Test;
import yirgacheffe.json.JSONArray;
import yirgacheffe.json.JSONObject;
import yirgacheffe.json.JSONValue;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class JSONArrayTest
{
	@Test
	public void testEmptyString()
	{
		JSONArray.Read json = JSONArray.read("");

		assertFalse(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("", json.getString(0));
		assertEquals("Failed to parse array: No data.", json.getObject(0).toString());
		assertEquals("Failed to parse array: No data.", json.getArray(0).toString());
		assertFalse(json.getValue(0) instanceof JSONValue.Valid);
		assertEquals("Failed to parse array: No data.", json.toString());

		assertEquals(0, json.length());
		assertEquals("Failed to parse array: No data.", json.validate());
		assertNotEquals(JSONArray.read("[]"), json);
		assertNotEquals(JSONArray.read("[]").hashCode(), json.hashCode());
	}

	@Test
	public void testParseEmptyArray()
	{
		JSONArray.Read json = JSONArray.read("[]");

		assertFalse(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("", json.getString(0));
		assertFalse(json.getValue(0) instanceof JSONValue.Valid);

		assertEquals(
			"Failed to read array index 0 of array of length 0.",
			json.getObject(0).toString());

		assertEquals(
			"Failed to read array index 0 of array of length 0.",
			json.getArray(0).toString());

		assertFalse(json.getBoolean(-1));
		assertTrue(Double.isNaN(json.getNumber(-1)));
		assertEquals("", json.getString(-1));

		assertEquals("Failed to read array index -1 of array of length 0.",
			json.getValue(-1).toString());

		assertEquals(
			"Failed to read array index -1 of array of length 0.",
			json.getObject(-1).toString());

		assertEquals(
			"Failed to read array index -1 of array of length 0.",
			json.getArray(-1).toString());

		assertEquals(0, json.length());
		assertEquals("", json.validate());
		assertEquals(JSONArray.read("[]"), json);
		assertEquals(JSONArray.read("[]").hashCode(), json.hashCode());
		assertNotEquals(0, json.hashCode());
	}

	@Test
	public void testParseNestedArrays()
	{
		JSONArray.Read json = JSONArray.read("[[]]");

		assertTrue(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("[]", json.getString(0));
		assertEquals("[]", json.getValue(0).toString());

		assertEquals(
			"Failed to parse object: Started with [ instead of {.",
			json.getObject(0).toString());

		assertEquals("[]", json.getArray(0).toString());
		assertEquals(1, json.length());
		assertEquals("", json.validate());
		assertEquals(JSONArray.read("[\n    []\n]"), json);
		assertEquals(JSONArray.read("[[]]").hashCode(), json.hashCode());
	}

	@Test
	public void testParseString()
	{
		JSONArray.Read json = JSONArray.read("[\"sumpt\"]");

		assertTrue(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("sumpt", json.getString(0));

		assertEquals(
			"Failed to parse object: Started with \" instead of {.",
			json.getObject(0).toString());

		assertEquals(
			"Failed to parse array: Started with \" instead of [.",
			json.getArray(0).toString());

		assertTrue(json.getValue(0) instanceof JSONValue.Valid);
		assertEquals(1, json.length());
		assertEquals("", json.validate());
		assertNotEquals(JSONArray.read("[]"), json);
		assertNotEquals(JSONArray.read("[]").hashCode(), json.hashCode());
	}

	@Test
	public void testParseEmptyString()
	{
		JSONArray.Read json = JSONArray.read("[\"\"]");

		assertFalse(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("", json.getString(0));
		assertTrue(json.getValue(0) instanceof JSONValue.Valid);

		assertEquals(1, json.length());
		assertEquals("", json.validate());
		assertNotEquals(JSONArray.read("[]"), json);
		assertNotEquals(JSONArray.read("[]").hashCode(), json.hashCode());
	}

	@Test
	public void testParseEscapedString()
	{
		JSONArray.Read json = JSONArray.read("[\"\\\"\"]");

		assertFalse(json.getBoolean(1));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("\\\"", json.getString(0));
		assertTrue(json.getValue(0) instanceof JSONValue.Valid);

		assertEquals(1, json.length());
		assertEquals("", json.validate());
		assertNotEquals(JSONArray.read("[]"), json);
		assertNotEquals(JSONArray.read("[]").hashCode(), json.hashCode());
	}

	@Test
	public void testParseZeroInteger()
	{
		JSONArray.Read json = JSONArray.read("[0]");

		assertFalse(json.getBoolean(0));
		assertEquals(0, json.getNumber(0), 0);
		assertEquals("0", json.getString(0));
		assertTrue(json.getValue(0) instanceof JSONValue.Valid);

		assertEquals(1, json.length());
		assertEquals("", json.validate());
		assertNotEquals(JSONArray.read("[]"), json);
		assertNotEquals(JSONArray.read("[]").hashCode(), json.hashCode());
	}

	@Test
	public void testParseInteger()
	{
		JSONArray.Read json = JSONArray.read("[22]");

		assertTrue(json.getBoolean(0));
		assertEquals(22, json.getNumber(0), 0);
		assertEquals("22", json.getString(0));
		assertTrue(json.getValue(0) instanceof JSONValue.Valid);

		assertEquals(1, json.length());
		assertEquals("", json.validate());
		assertNotEquals(JSONArray.read("[]"), json);
		assertNotEquals(JSONArray.read("[]").hashCode(), json.hashCode());
	}

	@Test
	public void testParseTrue()
	{
		JSONArray.Read json = JSONArray.read("[true]");

		assertTrue(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("true", json.getString(0));
		assertTrue(json.getValue(0) instanceof JSONValue.Valid);

		assertEquals(1, json.length());
		assertEquals("", json.validate());
		assertNotEquals(JSONArray.read("[]"), json);
		assertNotEquals(JSONArray.read("[]").hashCode(), json.hashCode());
	}

	@Test
	public void testParseNull()
	{
		JSONArray.Read json = JSONArray.read("[null]");

		assertFalse(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("null", json.getString(0));
		assertTrue(json.getValue(0) instanceof JSONValue.Valid);

		assertEquals(1, json.length());
		assertEquals("", json.validate());
		assertNotEquals(JSONArray.read("[]"), json);
		assertNotEquals(JSONArray.read("[]").hashCode(), json.hashCode());
	}

	@Test
	public void testInvalidJson()
	{
		JSONArray.Read json = JSONArray.read("[null");

		assertEquals(
			"Failed to parse array: Ran out of characters before end of array.",
			json.toString());

		assertEquals(
			"Failed to parse array: Ran out of characters before end of array.",
			json.validate());

		assertFalse(json.getValue(0) instanceof JSONValue.Valid);
		assertNotEquals(JSONArray.read("[]"), json);
		assertNotEquals(JSONArray.read("[]").hashCode(), json.hashCode());
		assertNotEquals(0, json.hashCode());
		assertEquals(JSONArray.read("[null"), json);
		assertNotEquals(JSONValue.read("[null"), json);
		assertNotEquals("[null", json);
		assertNotEquals(json, "[null");
		assertNotEquals(JSONArray.read(""), json);
		assertNotEquals(JSONArray.write().read(), json);

		int count = 0;

		for (JSONValue value: json)
		{
			count++;
		}

		assertEquals(0, count);
	}

	@Test
	public void testInvalidValue()
	{
		JSONArray.Read json = JSONArray.read("[nonsense]");

		assertFalse(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("", json.getString(0));

		assertEquals(
			"Failed to parse object: Started with n instead of {.",
			json.getObject(0).toString());

		assertEquals(
			"Failed to parse array: Started with n instead of [.",
			json.getArray(0).toString());

		assertFalse(json.getValue(0) instanceof JSONValue.Valid);

		assertEquals(
			"{\n    \"value at array position 0\":" +
				" \"Failed to parse value: nonsense is not a JSON value.\"\n}",
			json.toString());

		assertEquals(1, json.length());

		assertEquals(
			"{\"value at array position 0\": " +
				"\"Failed to parse value: nonsense is not a JSON value.\"}",
			json.validate());

		assertNotEquals(JSONArray.read("[]"), json);
		assertNotEquals(JSONArray.read("[]").hashCode(), json.hashCode());
	}

	@Test
	public void invalidData()
	{
		assertEquals("{\n" +
			"    \"value at array position 0\":\n" +
			"        {\n" +
			"            \"value at array position 0\":\n" +
			"                {\n" +
			"                    \"value at array position 0\": " +
			"\"Failed to parse value: nonsence is not a JSON value.\"\n" +
			"                }\n" +
			"        }\n" +
			"}", JSONArray.read("[[[nonsence]]]").toString());

		assertEquals("Failed to parse array at character 3: Found : when expecting ,.",
			JSONArray.read("[\"\":]").toString());

		assertEquals("Failed to parse array at character 2: Found : after end of array.",
			JSONArray.read("[]:").toString());

		assertEquals("Failed to parse array at character 13: Found : after end of array.",
			JSONArray.read("[\"\",nonsense]:").toString());
	}

	@Test
	public void testUnequalArraysOfSameLength()
	{
		JSONArray.Read first = JSONArray.read("[1,2]");
		JSONArray.Read second = JSONArray.read("[3,5]");

		assertNotEquals(first, second);
		assertNotEquals(first.hashCode(), second.hashCode());
	}

	@Test
	public void testObjectWithChildObject()
	{
		JSONArray.Read json = JSONArray.read("[{}]");

		assertTrue(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("{}", json.getString(0));
		Assert.assertEquals(JSONObject.read("{}"), json.getObject(0));

		assertEquals(
			"Failed to parse array: Started with { instead of [.",
			json.getArray(0).toString());

		assertEquals(1, json.length());
		assertEquals("", json.validate());
		assertNotEquals(JSONArray.read("[]"), json);
		assertNotEquals(JSONArray.read("[]").hashCode(), json.hashCode());
	}

	@Test
	public void testObjectWithChildArray()
	{
		JSONArray.Read json = JSONArray.read("[[]]");

		assertTrue(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("[]", json.getString(0));

		assertEquals(
			"Failed to parse object: Started with [ instead of {.",
			json.getObject(0).toString());

		assertEquals(JSONArray.read("[]"), json.getArray(0));

		assertEquals(1, json.length());
		assertEquals("", json.validate());
		assertNotEquals(JSONArray.read("[]"), json);
		assertNotEquals(JSONArray.read("[]").hashCode(), json.hashCode());
	}

	@Test
	public void testPushProperties()
	{
		JSONArray.Write json = JSONArray.write();

		json.push(JSONObject.write());
		json.push(JSONArray.write());
		json.push(JSONObject.write());
		json.push(JSONArray.write());
		json.push("sumpt");
		json.push(1);
		json.push(1.0);
		json.push(1e2);
		json.push(true);
		json.push(false);

		assertEquals(
			"[{},[],{},[],\"sumpt\",1,1.0,100.0,true,false]",
			json.toString());

		assertEquals("", json.read().validate());
	}

	@Test
	public void testPushChainedProperties()
	{
		JSONArray.Write json = JSONArray.write();

		json.push(JSONObject.write())
			.push(JSONArray.write())
			.push(JSONObject.write())
			.push(JSONArray.write())
			.push("sumpt")
			.push(1)
			.push(1.0)
			.push(1e2)
			.push(false)
			.push(true);

		assertEquals(
			"[{},[],{},[],\"sumpt\",1,1.0,100.0,false,true]",
			json.toString());
	}

	@Test
	public void testPushAllProperties()
	{
		JSONArray.Write json = JSONArray.write();

		json.push(JSONObject.write(), JSONObject.write());
		json.push(JSONArray.write(), JSONArray.write());
		json.push("thingy", "sumpt");
		json.push(1, 2);
		json.push(1.0, 2.0);
		json.push(1e2, 2e1);
		json.push(true, false);

		assertEquals(
			"[{},{},[],[],\"thingy\",\"sumpt\",1,2,1.0,2.0,100.0,20.0,true,false]",
			json.toString());
	}

	@Test
	public void testPushAllChainedProperties()
	{
		JSONArray.Write json = JSONArray.write();

		json.push(JSONObject.write(), JSONObject.write())
			.push(JSONArray.write(), JSONArray.write())
			.push("t", "s")
			.push(1, 2)
			.push(1.0, 2.0)
			.push(1e2, 2e1)
			.push(false, true)
			.push(true, false);

		assertEquals(
			"[{},{},[],[],\"t\",\"s\",1,2,1.0,2.0,100.0,20.0,false,true,true,false]",
			json.toString());
	}

	@Test
	public void testArrayWithTwoElements()
	{
		JSONArray.Read json = JSONArray.read("[1,2]");

		assertTrue(json.getBoolean(1));
		assertEquals(2.0, json.getNumber(1), 0);
		assertEquals("2", json.getString(1));

		assertEquals(2, json.length());
		assertEquals("", json.validate());
		assertNotEquals(JSONArray.read("[]"), json);
		assertNotEquals(JSONArray.read("[]").hashCode(), json.hashCode());
	}

	@Test
	public void testGetElementPastEndOfArray()
	{
		JSONArray.Read json = JSONArray.read("[1]");

		assertFalse(json.getBoolean(1));
		assertTrue(Double.isNaN(json.getNumber(1)));
		assertEquals("", json.getString(1));

		assertEquals(1, json.length());
		assertEquals("", json.validate());
		assertNotEquals(JSONArray.read("[]"), json);
		assertNotEquals(JSONArray.read("[]").hashCode(), json.hashCode());
	}

	@Test
	public void testArrayWithThirtyThreeElements()
	{
		JSONArray.Read json = JSONArray.read(
			"[0,1,2,3,4,5,6,7,8,9," +
				"10,11,12,13,14,15,16,17,18,19," +
				"20,21,22,23,24,25,26,27,28,29," +
				"30,31,32]");

		assertEquals(33, json.length());
		assertEquals("", json.validate());
		assertNotEquals(JSONArray.read("[]"), json);

		int count = 0;

		for (JSONValue value: json)
		{
			count++;
		}

		assertEquals(33, count);
	}
}
