package org.lice.runtime;

import org.lice.runtime.ast.Node;

import javax.script.Bindings;
import java.util.List;

/**
 * Created by Glavo on 17-9-21.
 *
 * @author Glavo
 * @since 4.0.0
 */
@FunctionalInterface
public interface LiceFunction {
	Node apply(Bindings env, List<? extends Node> args);
}
