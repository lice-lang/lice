package org.lice.internal.parser;

/**
 * Created by Glavo on 17-9-23.
 *
 * @author Glavo
 * @since 4.0.0
 */
public abstract class Token {
	public enum Type {
		StringLiteral,
		IntegerLiteral,
		LongLiteral,
		BigIntegerLiteral,
		FloatLiteral,
		DoubleLiteral,
		BigDecimalLiteral,
		BooleanLiteral,
		Identify
	}
	public enum Error {
		Unclosed,
		InvalidEscape,
		InvalidLiteral
	}
}
