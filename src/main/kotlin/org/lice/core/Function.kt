package org.lice.core

import org.lice.ast.Node
import javax.script.Bindings

/**
 * Created by Glavo on 17-8-28.
 *
 * @author Glavo
 * @since 0.1.0
 */
interface Function {
	companion object {

	}

	operator fun invoke(bindings: Bindings, nodes: List<Node>): Node
}