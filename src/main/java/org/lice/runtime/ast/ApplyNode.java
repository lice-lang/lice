package org.lice.runtime.ast;

import org.lice.runtime.Unit;

import javax.script.Bindings;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Glavo on 17-9-22.
 *
 * @author Glavo
 * @since 4.0.0
 */
@SuppressWarnings("WeakerAccess")
public class ApplyNode extends Node {
	public final List<Node> nodes;

	public ApplyNode() {
		this.nodes = new ArrayList<>();
	}

	public ApplyNode(List<Node> nodes) {
		this.nodes = nodes;
	}

	@Override
	public Object eval(Bindings bindings) throws ScriptException {
		if (nodes == null || nodes.isEmpty()) return Unit.unit;
		Node fun = nodes.get(0);
		List<Node> args = nodes.subList(1, nodes.size());


		return null; //TODO
	}
}
