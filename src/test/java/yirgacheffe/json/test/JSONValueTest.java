package yirgacheffe.json.test;

import org.junit.Test;
import yirgacheffe.json.JSONArray;
import yirgacheffe.json.JSONObject;
import yirgacheffe.json.JSONValue;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class JSONValueTest
{
	@Test
	public void testNoData()
	{
		JSONValue value = JSONValue.read("");

		assertFalse(value.isNull());
		assertFalse(value.isBoolean());
		assertFalse(value.isNumber());
		assertFalse(value.isString());
		assertFalse(value.isObject());
		assertFalse(value.isArray());

		assertFalse(value.getBoolean());
		assertEquals(Double.NaN, value.getNumber());
		assertEquals("", value.getString());

		assertEquals(
			"Failed to parse value: No data.",
			value.getObject().toString());

		assertEquals(
			"Failed to parse value: No data.",
			value.getArray().toString());

		assertEquals(JSONValue.read(""), value);

		assertEquals(
			JSONValue.read("").hashCode(),
			value.hashCode());

		assertNotEquals(0, value.hashCode());

		assertEquals("Failed to parse value: No data.", value.validate());
		assertEquals("Failed to parse value: No data.", value.toString());
		assertNotEquals("", value);
	}

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

		assertEquals(JSONValue.read("null"), value);
		assertEquals(JSONValue.read("null").hashCode(), value.hashCode());
		assertEquals("", value.validate());
		assertEquals("null", value.toString());
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

		assertEquals(JSONValue.read("true"), value);
		assertEquals(JSONValue.read("true").hashCode(), value.hashCode());
		assertEquals("", value.validate());
		assertEquals("true", value.toString());
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

		assertEquals(JSONValue.read("false"), value);
		assertEquals(JSONValue.read("false").hashCode(), value.hashCode());
		assertEquals("", value.validate());
		assertEquals("false", value.toString());
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

		assertEquals(JSONValue.read("1"), value);
		assertEquals(JSONValue.read("1").hashCode(), value.hashCode());
		assertEquals("", value.validate());
		assertEquals("1", value.toString());
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
		assertEquals("", value.validate());
		assertEquals("1.1", value.toString());
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

		assertEquals(JSONValue.read("\"it's a string!\""), value);
		assertEquals(JSONValue.read("\"it's a string!\"").hashCode(), value.hashCode());
		assertEquals("", value.validate());
		assertEquals("\"it's a string!\"", value.toString());
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

		assertEquals(JSONValue.read("\"it's a string!"), value);
		assertEquals(JSONValue.read("\"it's a string!").hashCode(), value.hashCode());

		assertEquals(
			"Failed to parse value: \"it's a string! is not a JSON value.",
			value.validate());

		assertEquals(
			"Failed to parse value: \"it's a string! is not a JSON value.",
			value.toString());
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

		assertEquals("", value.validate());
		assertEquals("{}", value.toString());
		assertEquals(JSONValue.read("{}"), value);
		assertEquals(JSONObject.read("{}"), value);
		assertNotEquals("{}", value);
		assertEquals(JSONValue.read("{}").hashCode(), value.hashCode());
		assertNotEquals(0, value.hashCode());
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

		assertEquals("Failed to parse value: { is not a JSON value.", value.validate());
		assertEquals("Failed to parse value: { is not a JSON value.", value.toString());
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

		assertEquals("", value.validate());
		assertEquals("[]", value.toString());
		assertEquals(JSONValue.read("[]"), value);
		assertEquals(JSONArray.read("[]"), value);
		assertNotEquals("[]", value);
		assertEquals(JSONValue.read("[]").hashCode(), value.hashCode());
		assertNotEquals(0, value.hashCode());
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

		assertEquals("Failed to parse value: [ is not a JSON value.", value.validate());
		assertEquals("Failed to parse value: [ is not a JSON value.", value.toString());
	}

	@Test
	public void testInvalidObject()
	{
		JSONValue value = JSONValue.read("{\"key\":nonsense}");

		assertEquals(
			"{\n" +
			"    \"value of key\":" +
				" \"Failed to parse value: nonsense is not a JSON value.\"\n" +
			"}",
			value.getObject().toString());

		assertEquals(
			"{\"value of key\":" +
				" \"Failed to parse value: nonsense is not a JSON value.\"}",
			value.validate());

		assertEquals(
			"{\n" +
			"    \"value of key\":" +
				" \"Failed to parse value: nonsense is not a JSON value.\"\n" +
			"}",
			value.toString());
	}

	@Test
	public void testInvalidArray()
	{
		JSONValue value = JSONValue.read("[nonsense]");

		assertEquals(
			"{\n" +
			"    \"value at array position 0\":" +
				" \"Failed to parse value: nonsense is not a JSON value.\"\n" +
			"}",
			value.getArray().toString());

		assertEquals(
			"{\"value at array position 0\":" +
				" \"Failed to parse value: nonsense is not a JSON value.\"}",
			value.validate());

		assertEquals(
			"{\n" +
			"    \"value at array position 0\":" +
				" \"Failed to parse value: nonsense is not a JSON value.\"\n" +
			"}",
			value.toString());
	}
}
