package org.lice.runtime;

/**
 * Created by Glavo on 17-9-21.
 *
 * @author Glavo
 * @since 4.0.0
 */
public final class Undefined {
	public static final Undefined undefined = new Undefined();

	private Undefined() {
	}

	@Override
	public String toString() {
		return "undefined";
	}
}
