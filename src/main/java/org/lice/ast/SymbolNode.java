package org.lice.ast;

import org.lice.api.scripting.LiceBindings;
import org.lice.runtime.NameError;
import org.lice.runtime.Undefined;

import javax.script.Bindings;

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
		Object o = LiceBindings.getOrUndefined(bindings, symbol);

		if (o == Undefined.undefined)
			throw new NameError("name '" + symbol + "' is not defined");
		else
			return o;
	}
}
