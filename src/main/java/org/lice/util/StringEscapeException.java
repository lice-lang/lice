package org.lice.util;

/**
 * Created by Glavo on 17-9-22.
 *
 * @author Glavo
 * @since 4.0.0
 */
public class StringEscapeException extends RuntimeException {
	public static void unclosed() throws StringEscapeException {
		throw new StringEscapeException("unclosed string literal");
	}

	public static void invalid() throws StringEscapeException {
		throw new StringEscapeException("invalid escape character");
	}

	private final String message;

	public StringEscapeException(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return message;
	}
}
