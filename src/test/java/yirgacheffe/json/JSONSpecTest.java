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
		assertEquals(0, spec.getError("{}").length());
	}

	@Test
	public void testValidationCache()
	{
		JSONSpec spec = new JSONSpec();

		assertEquals(0, spec.getError("{}").length());
		assertTrue(spec.isValid("{}"));
	}

	@Test
	public void testEmptyString()
	{
		JSONSpec spec = new JSONSpec();

		assertFalse(spec.isValid(""));

		assertEquals("Invalid token=EOF at (line no=1, column no=0, offset=-1). " +
				"Expected tokens are: [CURLYOPEN, SQUAREOPEN, STRING, NUMBER, TRUE, FALSE, NULL]",
			spec.getError(""));
	}

	@Test
	public void testNull()
	{
		JSONSpec spec = new JSONSpec();

		assertEquals("Invalid token=EOF at (line no=1, column no=0, offset=-1). " +
			"Expected tokens are: [CURLYOPEN, SQUAREOPEN, STRING, NUMBER, TRUE, FALSE, NULL]",
			spec.getError(null));

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

		assertEquals(
			"Invalid token=EOF at (line no=1, column no=3, offset=2). " +
				"Expected tokens are: [STRING, CURLYCLOSE]",
			spec.getError("{"));

		Assert.assertEquals("", spyError.toString());

		System.setErr(originalError);
	}

	@Test
	public void testInvalidRetrievedFromCache()
	{
		JSONSpec spec = new JSONSpec();

		assertEquals("Invalid token=EOF at (line no=1, column no=3, offset=2)." +
			" Expected tokens are: [STRING, CURLYCLOSE]",
			spec.getError("{"));

		assertFalse(spec.isValid("{"));
	}
}
