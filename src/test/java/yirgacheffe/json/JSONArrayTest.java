package yirgacheffe.json;

import org.junit.Test;

import javax.json.JsonException;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JSONArrayTest
{
	@Test
	public void testEmptyArray()
	{
		JSONArray json = new JSONArray();

		assertFalse(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("", json.getString(0));
		assertEquals("", json.getObject(0).toString());
		assertEquals("", json.getArray(0).toString());
		assertEquals("[]", json.toString());
		assertEquals(0, json.length());
	}

	@Test
	public void testParseEmptyArray()
	{
		JSONArray json = new JSONArray("[]");

		assertFalse(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("", json.getString(0));
		assertEquals("", json.getObject(0).toString());
		assertEquals("", json.getArray(0).toString());

		assertFalse(json.getBoolean(-1));
		assertTrue(Double.isNaN(json.getNumber(-1)));
		assertEquals("", json.getString(-1));
		assertEquals("", json.getObject(-1).toString());
		assertEquals("", json.getArray(-1).toString());

		assertEquals("[]", json.toString());
		assertEquals(0, json.length());
	}

	@Test
	public void testParseString()
	{
		JSONArray json = new JSONArray("[\"sumpt\"]");

		assertTrue(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("sumpt", json.getString(0));
		assertEquals("", json.getObject(0).toString());
		assertEquals("", json.getArray(0).toString());
		assertEquals("[\"sumpt\"]", json.toString());
		assertEquals(1, json.length());
	}

	@Test
	public void testParseEmptyString()
	{
		JSONArray json = new JSONArray("[\"\"]");

		assertFalse(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("", json.getString(0));
		assertEquals("", json.getObject(0).toString());
		assertEquals("", json.getArray(0).toString());
		assertEquals("[\"\"]", json.toString());
		assertEquals(1, json.length());
	}

	@Test
	public void testParseZeroInteger()
	{
		JSONArray json = new JSONArray("[0]");

		assertFalse(json.getBoolean(0));
		assertEquals(0, json.getNumber(0), 0);
		assertEquals("0", json.getString(0));
		assertEquals("", json.getObject(0).toString());
		assertEquals("", json.getArray(0).toString());
		assertEquals("[0]", json.toString());
		assertEquals(1, json.length());
	}

	@Test
	public void testParseTrue()
	{
		JSONArray json = new JSONArray("[true]");

		assertTrue(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("true", json.getString(0));
		assertEquals("", json.getObject(0).toString());
		assertEquals("", json.getArray(0).toString());
		assertEquals("[true]", json.toString());
		assertEquals(1, json.length());
	}

	@Test
	public void testParseNull()
	{
		JSONArray json = new JSONArray("[null]");

		assertFalse(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("null", json.getString(0));
		assertEquals("", json.getObject(0).toString());
		assertEquals("", json.getArray(0).toString());
		assertEquals("[null]", json.toString());
		assertEquals(1, json.length());
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
			JSONArray json = new JSONArray("[null");
		}
		catch (JsonException e)
		{
			exception = e;
		}

		assertNotNull(exception);

		assertEquals(
			"Invalid token=EOF at (line no=1, column no=15, offset=14)." +
				" Expected tokens are: [COMMA, CURLYCLOSE]",
			exception.getMessage());

		assertEquals("", spyError.toString());

		System.setErr(originalError);
	}

	@Test
	public void testObjectWithChildObject()
	{
		JSONArray json = new JSONArray("[{}]");

		assertTrue(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("{}", json.getString(0));
		assertEquals("{}", json.getObject(0).toString());
		assertEquals("", json.getArray(0).toString());
		assertEquals("[{}]", json.toString());
		assertEquals(1, json.length());
	}

	@Test
	public void testObjectWithChildArray()
	{
		JSONArray json = new JSONArray("[[]]");

		assertTrue(json.getBoolean(0));
		assertTrue(Double.isNaN(json.getNumber(0)));
		assertEquals("[]", json.getString(0));
		assertEquals("", json.getObject(0).toString());
		assertEquals("[]", json.getArray(0).toString());
		assertEquals("[[]]", json.toString());
		assertEquals(1, json.length());
	}

	@Test
	public void testPutProperties()
	{
		JSONArray json = new JSONArray();

		json.put(new JSONObject());
		json.put(new JSONArray());
		json.put("sumpt");
		json.put(1);
		json.put(1.0);
		json.put(1e2);
		json.put(true);

		assertEquals(
			"[{},[],\"sumpt\",1,1.0,100.0,true]",
			json.toString());
	}

	@Test
	public void testArrayWithTwoElements()
	{
		JSONArray json = new JSONArray("[1,2]");

		assertTrue(json.getBoolean(1));
		assertEquals(2.0, json.getNumber(1), 0);
		assertEquals("2", json.getString(1));
		assertEquals("", json.getObject(1).toString());
		assertEquals("", json.getArray(1).toString());
		assertEquals("[1,2]", json.toString());
		assertEquals(2, json.length());
	}

	@Test
	public void testGetElementPastEndOfArray()
	{
		JSONArray json = new JSONArray("[1]");

		assertFalse(json.getBoolean(1));
		assertTrue(Double.isNaN(json.getNumber(1)));
		assertEquals("", json.getString(1));
		assertEquals("", json.getObject(1).toString());
		assertEquals("", json.getArray(1).toString());
		assertEquals("[1]", json.toString());
		assertEquals(1, json.length());
	}

	@Test
	public void testArrayWithThirtyThreeElements()
	{
		JSONArray json = new JSONArray(
			"[0,1,2,3,4,5,6,7,8,9," +
				"10,11,12,13,14,15,16,17,18,19," +
				"20,21,22,23,24,25,26,27,28,29," +
				"30,31,32]");

		assertEquals(33, json.length());
	}

	@Test
	public void testPutThirtyThirdObjectOnArray()
	{
		JSONArray json = new JSONArray(
			"[0,1,2,3,4,5,6,7,8,9," +
				"10,11,12,13,14,15,16,17,18,19," +
				"20,21,22,23,24,25,26,27,28,29," +
				"30]");

		json.put(new JSONObject());
		json.put(new JSONObject());

		assertEquals(33, json.length());
	}

	@Test
	public void testPutThirtyThirdArrayOnArray()
	{
		JSONArray json = new JSONArray(
			"[0,1,2,3,4,5,6,7,8,9," +
				"10,11,12,13,14,15,16,17,18,19," +
				"20,21,22,23,24,25,26,27,28,29," +
				"30]");

		json.put(new JSONArray());
		json.put(new JSONArray());

		assertEquals(33, json.length());
	}

	@Test
	public void testPutThirtyThirdStringOnArray()
	{
		JSONArray json = new JSONArray(
			"[0,1,2,3,4,5,6,7,8,9," +
				"10,11,12,13,14,15,16,17,18,19," +
				"20,21,22,23,24,25,26,27,28,29," +
				"30]");

		json.put("");
		json.put("");

		assertEquals(33, json.length());
	}

	@Test
	public void testPutThirtyThirdIntegerOnArray()
	{
		JSONArray json = new JSONArray(
			"[0,1,2,3,4,5,6,7,8,9," +
				"10,11,12,13,14,15,16,17,18,19," +
				"20,21,22,23,24,25,26,27,28,29," +
				"30]");

		json.put(32);
		json.put(32);

		assertEquals(33, json.length());
	}

	@Test
	public void testPutThirtyThirdFloatOnArray()
	{
		JSONArray json = new JSONArray(
			"[0,1,2,3,4,5,6,7,8,9," +
				"10,11,12,13,14,15,16,17,18,19," +
				"20,21,22,23,24,25,26,27,28,29," +
				"30]");

		json.put(32.0);
		json.put(32.0);

		assertEquals(33, json.length());
	}

	@Test
	public void testPutThirtyThirdBooleanOnArray()
	{
		JSONArray json = new JSONArray(
			"[0,1,2,3,4,5,6,7,8,9," +
				"10,11,12,13,14,15,16,17,18,19," +
				"20,21,22,23,24,25,26,27,28,29," +
				"30]");

		json.put(true);
		json.put(true);

		assertEquals(33, json.length());
	}
}
