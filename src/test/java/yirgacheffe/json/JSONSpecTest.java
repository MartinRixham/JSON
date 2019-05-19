package yirgacheffe.json;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class JSONSpecTest
{
	@Test
	public void testValidJson()
	{
		JSONSpec spec = new JSONSpec();

		assertTrue(spec.isValid("{}"));
		assertEquals(0, spec.getErrors("{}").length);
	}

	@Test
	public void testValidationCache()
	{
		JSONSpec spec = new JSONSpec();

		assertEquals(0, spec.getErrors("{}").length);
		assertTrue(spec.isValid("{}"));
	}

	@Test
	public void testEmptyString()
	{
		JSONSpec spec = new JSONSpec();

		assertFalse(spec.isValid(""));
		assertEquals(1, spec.getErrors("").length);
	}

	@Test
	public void testNull()
	{
		JSONSpec spec = new JSONSpec();

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

		JSONSpec spec = new JSONSpec();

		assertFalse(spec.isValid("{"));
		assertEquals(1, spec.getErrors("{").length);
		Assert.assertEquals("", spyError.toString());

		System.setErr(originalError);
	}

	@Test
	public void testInvalidRetrievedFromCache()
	{
		JSONSpec spec = new JSONSpec();

		assertEquals(1, spec.getErrors("{").length);
		assertFalse(spec.isValid("{"));
	}
}
