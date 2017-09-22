package org.lice.runtime;

/**
 * Created by Glavo on 17-9-22.
 *
 * @author Glavo
 * @since 4.0.0
 */
public final class Unit {
	public static final Unit unit = new Unit();

	private Unit() {
	}

	@Override
	public String toString() {
		return "()";
	}
}
