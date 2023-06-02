package yirgacheffe.json;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
		assertEquals("Failed to parse array: No data.", json.toString());
		assertEquals(0, json.length());
	}

	@Test
	public void testParseEmptyArray()
	{
		JSONArray.Read json = JSONArray.read("[]");

		assertFalse(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("", json.getString(0));
		assertEquals("Failed to read array index 0 of array of length 0.", json.getObject(0).toString());
		assertEquals("Failed to read array index 0 of array of length 0.", json.getArray(0).toString());

		assertFalse(json.getBoolean(-1));
		assertTrue(Double.isNaN(json.getNumber(-1)));
		assertEquals("", json.getString(-1));
		assertEquals("Failed to read array index -1 of array of length 0.", json.getObject(-1).toString());
		assertEquals("Failed to read array index -1 of array of length 0.", json.getArray(-1).toString());

		assertEquals(0, json.length());
	}

	@Test
	public void testParseString()
	{
		JSONArray.Read json = JSONArray.read("[\"sumpt\"]");

		assertTrue(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("sumpt", json.getString(0));
		assertEquals("Failed to parse object: Started with \" instead of {.", json.getObject(0).toString());
		assertEquals("Failed to parse array: Started with \" instead of [.", json.getArray(0).toString());

		assertEquals(1, json.length());
	}

	@Test
	public void testParseEmptyString()
	{
		JSONArray.Read json = JSONArray.read("[\"\"]");

		assertFalse(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("", json.getString(0));
		assertEquals("Failed to parse object: Started with \" instead of {.", json.getObject(0).toString());
		assertEquals("Failed to parse array: Started with \" instead of [.", json.getArray(0).toString());

		assertEquals(1, json.length());
	}

	@Test
	public void testParseZeroInteger()
	{
		JSONArray.Read json = JSONArray.read("[0]");

		assertFalse(json.getBoolean(0));
		assertEquals(0, json.getNumber(0), 0);
		assertEquals("0", json.getString(0));
		assertEquals("Failed to parse object: Started with 0 instead of {.", json.getObject(0).toString());
		assertEquals("Failed to parse array: Started with 0 instead of [.", json.getArray(0).toString());

		assertEquals(1, json.length());
	}

	@Test
	public void testParseTrue()
	{
		JSONArray.Read json = JSONArray.read("[true]");

		assertTrue(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("true", json.getString(0));
		assertEquals("Failed to parse object: Started with t instead of {.", json.getObject(0).toString());
		assertEquals("Failed to parse array: Started with t instead of [.", json.getArray(0).toString());

		assertEquals(1, json.length());
	}

	@Test
	public void testParseNull()
	{
		JSONArray.Read json = JSONArray.read("[null]");

		assertFalse(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("null", json.getString(0));
		assertEquals("Failed to parse object: Started with n instead of {.", json.getObject(0).toString());
		assertEquals("Failed to parse array: Started with n instead of [.", json.getArray(0).toString());

		assertEquals(1, json.length());
	}

	@Test
	public void testInvalidJson()
	{
		JSONArray.Read json = JSONArray.read("[null");

		assertEquals(
			"Failed to parse array: Ran out of characters before end of array.",
			json.toString());
	}

	@Test
	public void testObjectWithChildObject()
	{
		JSONArray.Read json = JSONArray.read("[{}]");

		assertTrue(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("{}", json.getString(0));
		assertEquals(JSONObject.read("{}"), json.getObject(0));
		assertEquals("Failed to parse array: Started with { instead of [.", json.getArray(0).toString());

		assertEquals(1, json.length());
	}

	@Test
	public void testObjectWithChildArray()
	{
		JSONArray.Read json = JSONArray.read("[[]]");

		assertTrue(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("[]", json.getString(0));
		assertEquals("Failed to parse object: Started with [ instead of {.", json.getObject(0).toString());
		assertEquals(JSONArray.read("[]"), json.getArray(0));

		assertEquals(1, json.length());
	}

	@Test
	public void testPutProperties()
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

		assertEquals(
			"[{},[],{},[],\"sumpt\",1,1.0,100.0,true]",
			json.toString());
	}

	@Test
	public void testArrayWithTwoElements()
	{
		JSONArray.Read json = JSONArray.read("[1,2]");

		assertTrue(json.getBoolean(1));
		assertEquals(2.0, json.getNumber(1), 0);
		assertEquals("2", json.getString(1));
		assertEquals("Failed to parse object: Started with 2 instead of {.", json.getObject(1).toString());
		assertEquals("Failed to parse array: Started with 2 instead of [.", json.getArray(1).toString());

		assertEquals(2, json.length());
	}

	@Test
	public void testGetElementPastEndOfArray()
	{
		JSONArray.Read json = JSONArray.read("[1]");

		assertFalse(json.getBoolean(1));
		assertTrue(Double.isNaN(json.getNumber(1)));
		assertEquals("", json.getString(1));
		assertEquals("Failed to read array index 1 of array of length 1.", json.getObject(1).toString());
		assertEquals("Failed to read array index 1 of array of length 1.", json.getArray(1).toString());

		assertEquals(1, json.length());
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
	}
}
