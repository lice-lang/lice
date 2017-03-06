/**
 * Created by ice1000 on 2017/2/26.
 *
 * @author ice1000
 */
@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("Standard")
@file:JvmMultifileClass

package lice.core

import lice.compiler.model.Node
import lice.compiler.model.Node.Objects.getNullNode
import lice.compiler.model.ValueNode
import lice.compiler.util.InterpretException
import lice.compiler.util.SymbolList
import java.lang.Thread.sleep
import kotlin.concurrent.thread

inline fun SymbolList.addConcurrentFunctions() {
	defineFunction("thread|>", { ln, ls ->
		var ret: Node = getNullNode(ln)
		thread { ls.forEach { node -> ret = ValueNode(node.eval()) } }
		ret
	})
	defineFunction("sleep", { ln, ls ->
		val a = ls[0].eval()
		when {
			a.o is Number -> sleep(a.o.toLong())
			else -> InterpretException.typeMisMatch("Number", a, ln)
		}
		getNullNode(ln)
	})
}
