package org.lice.runtime.ast;

import javax.script.Bindings;

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
	public Node eval(Bindings bindings) {
		ValueNode node = new ValueNode(value);
		node.metaData = this.metaData;
		return node;
	}
}
