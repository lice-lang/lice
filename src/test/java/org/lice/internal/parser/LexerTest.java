package org.lice.internal.parser;

import org.junit.Test;
import org.lice.internal.parser.deprecated.Lexer;

/**
 * Created by Glavo on 17-9-22.
 *
 * @author Glavo
 * @since 4.0.0
 */
public class LexerTest {
	@Test(expected = NullPointerException.class)
	public void testConstructByNull() {
		new Lexer(null);
	}
}