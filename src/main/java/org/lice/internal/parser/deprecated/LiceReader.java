package org.lice.internal.parser.deprecated;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by Glavo on 17-9-21.
 *
 * @author Glavo
 * @since 4.0.0
 */
@SuppressWarnings("ALL")
public class LiceReader extends Reader {
	Reader in;

	int line = 1;
	int column = 0;
	char ch = '\u0000';

	public void reInit(Reader reader) {
		this.in = reader;
	}

	/**
	 * Creates a new filtered reader.
	 *
	 * @param in a Reader object providing the underlying stream.
	 *
	 * @throws NullPointerException if <code>in</code> is <code>null</code>
	 */
	public LiceReader(Reader in) {
		this.in = in;
	}

	public void skip() throws IOException {
		while (ch == '\u0000' || Character.isWhitespace(ch)) {
			if (read() == -1) return;
		}
	}

	@Override
	public int read() throws IOException {
		int c = in.read();
		if (c == -1) {
			line++;
			column = 0;
			ch = '\u0000';
			return -1;
		}
		ch = (char) c;
		if (ch == '\n') {
			line++;
			column = 0;
		} else {
			column++;
		}

		return ch;
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void close() throws IOException {
		if (in != null) in.close();
	}
}
