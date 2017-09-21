package org.lice.runtime.ast;

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
	public Node eval(Bindings bindings) {

		return null;
	}
}
