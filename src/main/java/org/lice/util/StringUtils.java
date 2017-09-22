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

		StringReader sr = new StringReader(str);
		StringBuilder sb = new StringBuilder();

		try {
			while (true) {
				int ch;
				if ((ch = sr.read()) == -1)
					break;

				if (ch == '\\') {

					ch = sr.read();
					switch (ch) {
						case '\\':
							sb.append('\\');
							continue;
						case '\"':
							sb.append('\"');
							continue;
						case '/':
							sb.append('/');
							continue;
						case 'b':
							sb.append('\b');
							continue;
						case 'f':
							sb.append('\f');
							continue;
						case 'n':
							sb.append('\n');
							continue;
						case 'r':
							sb.append('\r');
							continue;
						case 't':
							sb.append('\t');
							continue;
						case 'u':
							String sbb = String.valueOf((char) sr.read()) +
									(char) sr.read() +
									(char) sr.read() +
									(char) sr.read();
							int ii =
									Integer.parseInt(sbb, 16);

							sb.append((char) ii);
							continue;
						default:
							return null; //TODO
					}

				} else {
					sb.append((char) ch);
				}
			}

		} catch (IOException ignored) {

		}

		return sb.toString();
	}

	private StringUtils() {
		throw new UnsupportedOperationException();
	}
}
