package org.lice.internal.parser.deprecated;

import org.lice.runtime.ast.Node;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
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
	public StringBuilder buffer = new StringBuilder(20);


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
	public void reInit(String str) {
		reInit(new StringReader(str));
	}

	public void reSet(String str) {
		reader.reInit(new StringReader(str));
		reader.line = 1;
		reader.column = 0;
		reader.ch = '\u0000';
	}

	public Node nextNode() throws IOException {
		buffer.setLength(0);
		reader.skip();

		if(reader.ch == '\u0000') return null;

		return null; //TODO
	}
}
