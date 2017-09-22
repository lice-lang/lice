package org.lice.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;

/**
 * Created by Glavo on 17-9-22.
 *
 * @author Glavo
 * @since 4.0.0
 */
public final class StringUtils {
	public static String escape(String str) {
		Objects.requireNonNull(str);
		StringBuilder builder = new StringBuilder(str.length());
		StringReader reader = new StringReader(str);
		int ch;

		try {
			while ((ch = reader.read()) != -1) {
				switch (ch) {
					case '\\':
						ch = reader.read();

						break;

					default:
						builder.append((char) ch);
						break;
				}
			}
		} catch (IOException ignored) {
			assert false;
		}

		return builder.toString();
	}

	private StringUtils() {
	}
}
