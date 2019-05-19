package yirgacheffe.json;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NullJSONObjectTest
{
	@Test
	public void testNullJsonObject()
	{
		JSONObject json = new NullJSONObject();

		assertFalse(json.has("thingy"));
		assertFalse(json.getBoolean("thingy"));
		assertTrue(Double.isNaN(json.getNumber("thingy")));
		assertEquals("", json.getString("thingy"));
		assertEquals("", json.getObject("thingy").toString());
		assertEquals("", json.getArray("thingy").toString());
		assertEquals("", json.toString());
	}
}
