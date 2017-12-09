package org.lice.util;

import org.lice.runtime.LiceFunction;

import javax.script.Bindings;

/**
 * Created by Glavo on 17-9-22.
 *
 * @author Glavo
 * @since 4.0.0
 */
@SuppressWarnings("WeakerAccess")
public final class SymbolList {
	public static void defineFunction(Bindings bindings, String name, LiceFunction function) {
		bindings.put(name, function);
	}

	public static LiceFunction getFunction(Bindings bindings, String name) {
		return (LiceFunction) bindings.get(name);
	}

	public static boolean containsFunction(Bindings bindings, String name) {
		return bindings.get(name) instanceof LiceFunction;
	}

	public static void putField(Bindings bindings, String name, Object value) {
		bindings.put("field " + name, value);
	}

	public static Object getField(Bindings bindings, String name) {
		return bindings.get("field " + name);
	}

	public static boolean containsField(Bindings bindings, String name) {
		return bindings.containsKey("field " + name);
	}

	public static void setParent(Bindings self, Bindings parent) {
		putField(self, "parent", parent);
	}

	public static Bindings getParent(Bindings bindings) {
		return (Bindings) getField(bindings, "parent");
	}

	public static boolean containsParent(Bindings bindings) {
		return containsField(bindings, "parent");
	}

	private SymbolList() {
	}
}
