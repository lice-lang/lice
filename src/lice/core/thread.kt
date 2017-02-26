/**
 * Created by ice1000 on 2017/2/26.
 *
 * @author ice1000
 */
@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("Standard")
@file:JvmMultifileClass

package lice.core

import lice.compiler.model.Node.Objects.EmptyNode
import lice.compiler.util.SymbolList
import java.lang.Thread.sleep
import kotlin.concurrent.thread

inline fun SymbolList.addConcurrentFunctions() {
	addFunction("thread|>", { ls ->
		thread { ls.forEach { node -> node.eval() } }
		EmptyNode
	})
	addFunction("sleep", { ls ->
		val a = ls[0].eval()
		when (a.o) {
			is Long -> sleep(a.o)
			is Int -> sleep(a.o.toLong())
			is Short -> sleep(a.o.toLong())
		}
		EmptyNode
	})
}
