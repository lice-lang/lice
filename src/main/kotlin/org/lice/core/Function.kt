package org.lice.core

import org.lice.ast.MetaData
import org.lice.ast.Node

/**
 * Created by Glavo on 17-8-28.
 *
 * @author Glavo
 * @since 0.1.0
 */
interface Function : Func {
	companion object Functions {
		operator fun invoke(f: Func): Function =
				f as? Function ?: object : Function {
					override fun invoke(meta: MetaData, args: List<Node>): Node =
							f(meta, args)
				}
	}

	override operator fun invoke(meta: MetaData, args: List<Node>): Node
}

