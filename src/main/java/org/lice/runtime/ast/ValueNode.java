package org.lice.runtime.ast;

import javax.script.Bindings;
import javax.script.ScriptContext;

/**
 * Created by Glavo on 17-9-21.
 *
 * @author Glavo
 * @since 4.0.0
 */
@SuppressWarnings("WeakerAccess")
public class ValueNode extends Node {
	public Object value;

	public ValueNode(Object value) {
		this.value = value;
	}

	@Override
	public Object eval(Bindings bindings) {
		return value;
	}
}
