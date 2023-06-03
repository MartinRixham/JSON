package yirgacheffe.json;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JSONValueTest
{
	@Test
	public void testNullValue()
	{
		JSONValue value = JSONValue.read("null");

		assertTrue(value.isNull());
		assertFalse(value.isBoolean());
		assertFalse(value.isNumber());
		assertFalse(value.isString());
		assertFalse(value.isObject());
		assertFalse(value.isArray());

		assertFalse(value.getBoolean());
		assertEquals(Double.NaN, value.getNumber());
		assertEquals("null", value.getString());

		assertEquals(
			"Failed to parse object: Started with n instead of {.",
			value.getObject().toString());

		assertEquals(
			"Failed to parse array: Started with n instead of [.",
			value.getArray().toString());
	}

	@Test
	public void testTrueValue()
	{
		JSONValue value = JSONValue.read("true");

		assertFalse(value.isNull());
		assertTrue(value.isBoolean());
		assertFalse(value.isNumber());
		assertFalse(value.isString());
		assertFalse(value.isObject());
		assertFalse(value.isArray());

		assertTrue(value.getBoolean());
		assertEquals(Double.NaN, value.getNumber());
		assertEquals("true", value.getString());

		assertEquals(
			"Failed to parse object: Started with t instead of {.",
			value.getObject().toString());

		assertEquals(
			"Failed to parse array: Started with t instead of [.",
			value.getArray().toString());
	}

	@Test
	public void testFalseValue()
	{
		JSONValue value = JSONValue.read("false");

		assertFalse(value.isNull());
		assertTrue(value.isBoolean());
		assertFalse(value.isNumber());
		assertFalse(value.isString());
		assertFalse(value.isObject());
		assertFalse(value.isArray());

		assertFalse(value.getBoolean());
		assertEquals(Double.NaN, value.getNumber());
		assertEquals("false", value.getString());

		assertEquals(
			"Failed to parse object: Started with f instead of {.",
			value.getObject().toString());

		assertEquals(
			"Failed to parse array: Started with f instead of [.",
			value.getArray().toString());
	}

	@Test
	public void testDigitValue()
	{
		JSONValue value = JSONValue.read("1");

		assertFalse(value.isNull());
		assertFalse(value.isBoolean());
		assertTrue(value.isNumber());
		assertFalse(value.isString());
		assertFalse(value.isObject());
		assertFalse(value.isArray());

		assertTrue(value.getBoolean());
		assertEquals(1d, value.getNumber());
		assertEquals("1", value.getString());

		assertEquals(
			"Failed to parse object: Started with 1 instead of {.",
			value.getObject().toString());

		assertEquals(
			"Failed to parse array: Started with 1 instead of [.",
			value.getArray().toString());
	}

	@Test
	public void testDecimalValue()
	{
		JSONValue value = JSONValue.read("1.1");

		assertFalse(value.isNull());
		assertFalse(value.isBoolean());
		assertTrue(value.isNumber());
		assertFalse(value.isString());
		assertFalse(value.isObject());
		assertFalse(value.isArray());

		assertTrue(value.getBoolean());
		assertEquals(1.1, value.getNumber());
		assertEquals("1.1", value.getString());
		assertFalse(value.getObject() instanceof JSONObject.Valid);
		assertFalse(value.getArray() instanceof JSONArray.Valid);
	}

	@Test
	public void testStringValue()
	{
		JSONValue value = JSONValue.read("\"it's a string!\"");

		assertFalse(value.isNull());
		assertFalse(value.isBoolean());
		assertFalse(value.isNumber());
		assertTrue(value.isString());
		assertFalse(value.isObject());
		assertFalse(value.isArray());

		assertTrue(value.getBoolean());
		assertEquals(Double.NaN, value.getNumber());
		assertEquals("it's a string!", value.getString());
		assertFalse(value.getObject() instanceof JSONObject.Valid);
		assertFalse(value.getArray() instanceof JSONArray.Valid);
	}

	@Test
	public void testUnclosedStringValue()
	{
		JSONValue value = JSONValue.read("\"it's a string!");

		assertFalse(value.isNull());
		assertFalse(value.isBoolean());
		assertFalse(value.isNumber());
		assertFalse(value.isString());
		assertFalse(value.isObject());
		assertFalse(value.isArray());

		assertFalse(value.getBoolean());
		assertEquals(Double.NaN, value.getNumber());
		assertEquals("", value.getString());
		assertFalse(value.getObject() instanceof JSONObject.Valid);
		assertFalse(value.getArray() instanceof JSONArray.Valid);
	}

	@Test
	public void testObjectValue()
	{
		JSONValue value = JSONValue.read("{}");

		assertFalse(value.isNull());
		assertFalse(value.isBoolean());
		assertFalse(value.isNumber());
		assertFalse(value.isString());
		assertTrue(value.isObject());
		assertFalse(value.isArray());

		assertTrue(value.getBoolean());
		assertEquals(Double.NaN, value.getNumber());
		assertEquals("{}", value.getString());
		assertEquals(JSONObject.write().read(), value.getObject());
		assertFalse(value.getArray() instanceof JSONArray.Valid);
	}

	@Test
	public void testUnclosedObjectValue()
	{
		JSONValue value = JSONValue.read("{");

		assertFalse(value.isNull());
		assertFalse(value.isBoolean());
		assertFalse(value.isNumber());
		assertFalse(value.isString());
		assertFalse(value.isObject());
		assertFalse(value.isArray());

		assertFalse(value.getBoolean());
		assertEquals(Double.NaN, value.getNumber());
		assertEquals("", value.getString());
		assertFalse(value.getObject() instanceof JSONObject.Valid);
		assertFalse(value.getArray() instanceof JSONArray.Valid);
	}

	@Test
	public void testArrayValue()
	{
		JSONValue value = JSONValue.read("[]");

		assertFalse(value.isNull());
		assertFalse(value.isBoolean());
		assertFalse(value.isNumber());
		assertFalse(value.isString());
		assertFalse(value.isObject());
		assertTrue(value.isArray());

		assertTrue(value.getBoolean());
		assertEquals(Double.NaN, value.getNumber());
		assertEquals("[]", value.getString());
		assertFalse(value.getObject() instanceof JSONObject.Valid);
		assertEquals(JSONArray.write().read(), value.getArray());
	}

	@Test
	public void testUnclosedArrayValue()
	{
		JSONValue value = JSONValue.read("[");

		assertFalse(value.isNull());
		assertFalse(value.isBoolean());
		assertFalse(value.isNumber());
		assertFalse(value.isString());
		assertFalse(value.isObject());
		assertFalse(value.isArray());

		assertFalse(value.getBoolean());
		assertEquals(Double.NaN, value.getNumber());
		assertEquals("", value.getString());
		assertFalse(value.getObject() instanceof JSONObject.Valid);
		assertFalse(value.getArray() instanceof JSONArray.Valid);
	}
}
