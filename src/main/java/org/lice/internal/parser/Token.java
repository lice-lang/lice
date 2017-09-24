package org.lice.internal.parser;

import org.lice.runtime.MetaData;

/**
 * Created by Glavo on 17-9-23.
 *
 * @author Glavo
 * @since 4.0.0
 */
@SuppressWarnings("WeakerAccess")
public class Token {
	public enum Type {
		StringLiteral,
		IntegerLiteral,
		LongLiteral,
		BigIntegerLiteral,
		FloatLiteral,
		DoubleLiteral,
		BigDecimalLiteral,
		BooleanLiteral,
		Identifier
	}

	public enum Error {
		Unclosed,
		InvalidEscape,
		InvalidLiteral
	}

	public Type type;
	public String image;
	public MetaData meta = null;

	public Token(Type type, String image) {
		this.type = type;
		this.image = image;
	}

	public Token(Type type, String image, MetaData meta) {
		this.type = type;
		this.image = image;
		this.meta = meta;
	}

}
