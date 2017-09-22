package org.lice.runtime;

import org.lice.runtime.ast.Node;

import javax.script.Bindings;
import javax.script.ScriptContext;
import java.util.List;

/**
 * Created by Glavo on 17-9-21.
 *
 * @author Glavo
 * @since 4.0.0
 */
@FunctionalInterface
public interface LiceFunction {
	Object apply(ScriptContext env, List<? extends Node> args);

	default boolean newScope() {
		return true;
	}
}
