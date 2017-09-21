package org.lice.runtime.ast;

import org.lice.runtime.Undefined;

import javax.script.Bindings;
import javax.script.ScriptContext;

/**
 * Created by Glavo on 17-9-21.
 *
 * @author Glavo
 * @since 4.0.0
 */
@SuppressWarnings("WeakerAccess")
public class SymbolNode extends Node {
	public final String symbol;

	public SymbolNode(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public Object eval(Bindings bindings) {
		Object o;
		o = bindings.getOrDefault(symbol, Undefined.undefined);

		return o;
	}
}
