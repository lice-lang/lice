package org.lice.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Glavo on 17-9-22.
 *
 * @author Glavo
 * @since 4.0.0
 */
public class StringUtilsTest {
	public static final String str1 = "abcdef";
	public static final String str2 = "a\\n\\f\\b\\t\\\"";
	public static final String str3 = "a\\u0023\\b";
	public static final String str4 = "\\a";

	@Test
	public void testEscape() {
		assertEquals(StringUtils.escape(str1), "abcdef");
		assertEquals(StringUtils.escape(str2), "a\n\f\b\t\"");
		assertEquals(StringUtils.escape(str3), "a\u0023\b");
		try {
			StringUtils.escape(str4);
			fail();

		} catch (StringEscapeException ignored) {

		}
	}
}