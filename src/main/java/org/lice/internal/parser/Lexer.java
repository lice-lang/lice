package org.lice.internal.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.Objects;

/**
 * Created by Glavo on 17-9-21.
 *
 * @author Glavo
 * @since 4.0.0
 */
@SuppressWarnings("WeakerAccess")
public class Lexer {
	public LiceReader reader;

	public Lexer(LiceReader reader) {
		Objects.requireNonNull(reader);
		this.reader = reader;
	}

	public void skip() throws IOException {
		reader.skip();
	}

	public void reInit(Reader reader) {
		Objects.requireNonNull(reader);
		this.reader.reInit(reader);
	}
}
