package org.lice.runtime;

import org.lice.runtime.ast.Node;

import java.util.List;

/**
 * Created by Glavo on 17-9-21.
 *
 * @author Glavo
 * @since 4.0.0
 */
@FunctionalInterface
public interface LiceFunction {
	Node apply(List<? extends Node> args);
}
