package org.lice.runtime;

import org.lice.ast.Node;
import org.lice.ast.ValueNode;
import org.lice.module.LiceModule;

import javax.script.ScriptContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Glavo on 17-9-21.
 *
 * @author Glavo
 * @since 4.0.0
 */
public abstract class LiceFunction {
	private LiceModule module;

	public abstract Object apply(ScriptContext env, List<? extends Node> args);

	public Object apply(ScriptContext env, Object... args) {
		ArrayList<Node> nodes = new ArrayList<>();
		for (Object obj : args) {
			nodes.add(new ValueNode(obj));
		}

		return apply(env, nodes);
	}
}
