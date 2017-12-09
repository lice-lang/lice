package org.lice.module;

/**
 * Created by Glavo on 17-9-22.
 *
 * @author Glavo
 * @since 4.0.0
 */
public interface LiceModule {
	String name();

	Object get(String name);

	void set(String name, Object value);
}
