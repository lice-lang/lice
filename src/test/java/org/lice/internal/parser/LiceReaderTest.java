package org.lice.internal.parser;

import org.junit.Test;

import java.awt.image.ImagingOpException;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

/**
 * Created by Glavo on 17-9-21.
 *
 * @author Glavo
 * @since 4.0.0
 */
public class LiceReaderTest {
	private static final String res = "1234567890\nabcdefghij";

	@SuppressWarnings("ResultOfMethodCallIgnored")
	@Test
	public void testRead() throws Exception {
		LiceReader reader = new LiceReader(new StringReader(res));

		assertEquals(reader.line, 1);
		assertEquals(reader.column, 0);
		assertEquals(reader.ch, '\u0000');

		assertEquals(reader.read(), '1');
		assertEquals(reader.line, 1);
		assertEquals(reader.column, 1);
		assertEquals(reader.ch, '1');

		while (reader.read() != '0') {
		}

		assertEquals(reader.line, 1);
		assertEquals(reader.column, 10);
		assertEquals(reader.ch, '0');

		assertEquals(reader.read(), '\n');
		assertEquals(reader.line, 2);
		assertEquals(reader.column, 0);
		assertEquals(reader.ch, '\n');

		assertEquals(reader.read(), 'a');
		assertEquals(reader.line, 2);
		assertEquals(reader.column, 1);
		assertEquals(reader.ch, 'a');

		while (reader.read() != -1) {
		}
		assertEquals(reader.line, 2);
		assertEquals(reader.column, 10);
		assertEquals(reader.ch, '\u0000');

	}

	@Test
	public void testReInit() throws IOException {
		LiceReader reader = new LiceReader(new StringReader(res));

		while (reader.read() != -1) {
		}
		assertEquals(reader.line, 3);
		assertEquals(reader.column, 0);
		assertEquals(reader.ch, '\u0000');

		reader.reInit(new StringReader(res));

		assertEquals(reader.read(), '1');
		assertEquals(reader.line, 3);
		assertEquals(reader.column, 1);
		assertEquals(reader.ch, '1');
	}

}