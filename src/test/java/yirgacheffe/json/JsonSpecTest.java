package yirgacheffe.json;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class JsonSpecTest
{
	@Test
	public void testValidJson()
	{
		JsonSpec spec = new JsonSpec();

		assertTrue(spec.isValid("{}"));
		assertEquals(0, spec.getErrors("{}").length);
	}

	@Test
	public void testValidationCache()
	{
		JsonSpec spec = new JsonSpec();

		assertEquals(0, spec.getErrors("{}").length);
		assertTrue(spec.isValid("{}"));
	}

	@Test
	public void testEmptyString()
	{
		JsonSpec spec = new JsonSpec();

		assertFalse(spec.isValid(""));
		assertEquals(1, spec.getErrors("").length);
	}

	@Test
	public void testNull()
	{
		JsonSpec spec = new JsonSpec();

		assertEquals(0, spec.getErrors(null).length);
		assertFalse(spec.isValid(null));
	}

	@Test
	public void testInvalidJson()
	{
		PrintStream originalError = System.err;

		OutputStream spyError = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(spyError);

		System.setErr(printStream);

		JsonSpec spec = new JsonSpec();

		assertFalse(spec.isValid("{"));
		assertEquals(1, spec.getErrors("{").length);
		Assert.assertEquals("", spyError.toString());

		System.setErr(originalError);
	}

	@Test
	public void testInvalidRetrievedFromCache()
	{
		JsonSpec spec = new JsonSpec();

		assertEquals(1, spec.getErrors("{").length);
		assertFalse(spec.isValid("{"));
	}
}
