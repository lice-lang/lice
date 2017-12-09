package org.lice.internal.parser;


/**
 * Created by Glavo on 17-9-21.
 *
 * @author Glavo
 * @since 4.0.0
 */
public class LiceReaderTest {
/*
	private static final String res = "1234567890\nabcdefghij";
	private static final String spaceRes = "  0  \t\r 21\tasdsa\nds";

	@Test
	public void testSkip() throws IOException {
		LiceReader reader = new LiceReader(new StringReader(spaceRes));

		reader.skip();
		assertEquals(reader.ch, '0');

		//noinspection ResultOfMethodCallIgnored
		reader.read();
		reader.skip();
		assertEquals(reader.ch, '2');
	}

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
		assertEquals(reader.line, 3);
		assertEquals(reader.column, 0);
		assertEquals(reader.ch, '\u0000');

	}

	@Test(expected = UnsupportedOperationException.class)
	public void testReadChars() throws IOException {
		LiceReader reader = new LiceReader(new StringReader(res));
		char[] chs = new char[20];

		reader.read(chs, 0, 10);
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

	@Test
	public void testClose() throws Exception {
		class TestReader extends Reader {
			private boolean closed = false;
			@Override
			public int read(char[] cbuf, int off, int len) throws IOException {
				return 0;
			}

			@Override
			public void close() throws IOException {
				closed = true;
			}
		}

		TestReader testReader = new TestReader();
		LiceReader liceReader = new LiceReader(testReader);
		liceReader.close();
		assertTrue(testReader.closed);

	}
*/
}