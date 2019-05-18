package yirgacheffe.json;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JsonArrayTest
{
	@Test
	public void testEmptyArray()
	{
		JsonArray json = new JsonArray();

		assertFalse(json.getBoolean(1.0));
		assertTrue(Double.isNaN(json.getNumber(1.0)));
		assertEquals("", json.getString(1.0));
		assertEquals("", json.getObject(1.0).toString());
		assertEquals("[]", json.toString());
		assertEquals(0, json.length());
	}

	@Test
	public void testParseEmptyObject()
	{
		JsonArray json = new JsonArray("[]");

		assertFalse(json.getBoolean(0.0));
		assertTrue(Double.isNaN(json.getNumber(0.0)));
		assertEquals("", json.getString(0.0));
		assertEquals("", json.getObject(0.0).toString());
		assertEquals("[]", json.toString());
		assertEquals(0, json.length());
	}

	@Test
	public void testParseString()
	{
		JsonArray json = new JsonArray("[\"sumpt\"]");

		assertTrue(json.getBoolean(0.0));
		assertTrue(Double.isNaN(json.getNumber(0.0)));
		assertEquals("sumpt", json.getString(0.0));
		assertEquals("", json.getObject(0.0).toString());
		assertEquals("[\"sumpt\"]", json.toString());
		assertEquals(1, json.length());
	}

	@Test
	public void testParseEmptyString()
	{
		JsonArray json = new JsonArray("[\"\"]");

		assertFalse(json.getBoolean(0.0));
		assertTrue(Double.isNaN(json.getNumber(0.0)));
		assertEquals("", json.getString(0.0));
		assertEquals("", json.getObject(0.0).toString());
		assertEquals("[\"\"]", json.toString());
		assertEquals(1, json.length());
	}

	@Test
	public void testParseZeroInteger()
	{
		JsonArray json = new JsonArray("[0]");

		assertFalse(json.getBoolean(0.0));
		assertEquals(0, json.getNumber(0.0), 0);
		assertEquals("0", json.getString(0.0));
		assertEquals("", json.getObject(0.0).toString());
		assertEquals("[0]", json.toString());
		assertEquals(1, json.length());
	}

	@Test
	public void testParseTrue()
	{
		JsonArray json = new JsonArray("[true]");

		assertTrue(json.getBoolean(0.0));
		assertTrue(Double.isNaN(json.getNumber(0.0)));
		assertEquals("true", json.getString(0.0));
		assertEquals("", json.getObject(0.0).toString());
		assertEquals("[true]", json.toString());
		assertEquals(1, json.length());
	}

	@Test
	public void testParseNull()
	{
		JsonArray json = new JsonArray("[null]");

		assertFalse(json.getBoolean(0.0));
		assertTrue(Double.isNaN(json.getNumber(0.0)));
		assertEquals("null", json.getString(0.0));
		assertEquals("", json.getObject(0.0).toString());
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
			JsonArray json = new JsonArray("[null");
		}
		catch (JsonException e)
		{
			exception = e;
		}

		assertNotNull(exception);
		assertEquals(
			"line 1:5 mismatched input '<EOF>' expecting {',', ']'}",
			exception.getMessage());
		assertEquals("", spyError.toString());

		System.setErr(originalError);
	}

	@Test
	public void testObjectWithChildObject()
	{
		JsonArray json = new JsonArray("[{}]");

		assertTrue(json.getBoolean(0.0));
		assertTrue(Double.isNaN(json.getNumber(0.0)));
		assertEquals("{}", json.getString(0.0));
		assertEquals("{}", json.getObject(0.0).toString());
		assertEquals("[{}]", json.toString());
		assertEquals(1, json.length());
	}

	@Test
	public void testPutProperties()
	{
		JsonArray json = new JsonArray();

		json.put(new JsonObject());
		json.put("sumpt");
		json.put(1);
		json.put(1.0);
		json.put(1e2);
		json.put(true);

		assertEquals(
			"[{},\"sumpt\",1,1.0,100.0,true]",
			json.toString());
	}

	@Test
	public void testArrayWithTwoElements()
	{
		JsonArray json = new JsonArray("[1,2]");

		assertTrue(json.getBoolean(1.0));
		assertEquals(2.0, json.getNumber(1.0), 0);
		assertEquals("2", json.getString(1.0));
		assertEquals("", json.getObject(1.0).toString());
		assertEquals("[1,2]", json.toString());
		assertEquals(2, json.length());
	}

	@Test
	public void testGetElementPastEndOfArray()
	{
		JsonArray json = new JsonArray("[1]");

		assertFalse(json.getBoolean(1.0));
		assertTrue(Double.isNaN(json.getNumber(1.0)));
		assertEquals("", json.getString(1.0));
		assertEquals("", json.getObject(1.0).toString());
		assertEquals("[1]", json.toString());
		assertEquals(1, json.length());
	}

	@Test
	public void testArrayWithThirtyThreeElements()
	{
		JsonArray json = new JsonArray(
			"[0,1,2,3,4,5,6,7,8,9," +
				"10,11,12,13,14,15,16,17,18,19," +
				"20,21,22,23,24,25,26,27,28,29," +
				"30,31,32]");

		assertEquals(33, json.length());
	}

	@Test
	public void testPutThirtyThirdObjectOnArray()
	{
		JsonArray json = new JsonArray(
			"[0,1,2,3,4,5,6,7,8,9," +
				"10,11,12,13,14,15,16,17,18,19," +
				"20,21,22,23,24,25,26,27,28,29," +
				"30]");

		json.put(new JsonObject());
		json.put(new JsonObject());

		assertEquals(33, json.length());
	}

	@Test
	public void testPutThirtyThirdStringOnArray()
	{
		JsonArray json = new JsonArray(
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
		JsonArray json = new JsonArray(
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
		JsonArray json = new JsonArray(
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
		JsonArray json = new JsonArray(
			"[0,1,2,3,4,5,6,7,8,9," +
				"10,11,12,13,14,15,16,17,18,19," +
				"20,21,22,23,24,25,26,27,28,29," +
				"30]");

		json.put(true);
		json.put(true);

		assertEquals(33, json.length());
	}
}
