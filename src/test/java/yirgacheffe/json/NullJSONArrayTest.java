package yirgacheffe.json;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NullJSONArrayTest
{
	@Test
	public void testNullJsonObject()
	{
		JSONArray json = new NullJSONArray();

		assertFalse(json.getBoolean(1.0));
		assertFalse(json.getBoolean(1));
		assertTrue(Double.isNaN(json.getNumber(1.0)));
		assertTrue(Double.isNaN(json.getNumber(1)));
		assertEquals("", json.getString(1.0));
		assertEquals("", json.getString(1));
		assertEquals("", json.getObject(1.0).toString());
		assertEquals("", json.getObject(1).toString());
		assertEquals("", json.getArray(1.0).toString());
		assertEquals("", json.getArray(1).toString());
		assertEquals("", json.toString());
	}
}
