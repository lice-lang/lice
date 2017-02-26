/**
 * Created by ice1000 on 2017/2/26.
 *
 * @author ice1000
 */
@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("Standard")
@file:JvmMultifileClass

package lice.core

import lice.compiler.model.EmptyNode
import lice.compiler.model.Value.Objects.Nullptr
import lice.compiler.model.ValueNode
import lice.compiler.util.InterpretException.Factory.tooFewArgument
import lice.compiler.util.InterpretException.Factory.typeMisMatch
import lice.compiler.util.SymbolList

import lice.util.Pair

inline fun SymbolList.addListFunctions() {
	defineFunction("[|]", { ls ->
		ValueNode(ls.foldRight(null) { value, pairs: Any? ->
			Pair(value.eval().o, pairs)
		}, Pair::class.java)
	})
	defineFunction("head", { ls ->
		val a = ls[0].eval()
		if (a.o is Pair<*, *>) when (a.o.first) {
			null -> EmptyNode
			else -> ValueNode(a.o.first)
		}
		else typeMisMatch("Pair", a)
	})
	defineFunction("tail", { ls ->
		val a = ls[0].eval()
		if (a.o is Pair<*, *>) when (a.o.second) {
			null -> EmptyNode
			else -> ValueNode(a.o.second)
		}
		else typeMisMatch("Pair", a)
	})
}

inline fun SymbolList.addCollectionsFunctions() {
	defineFunction("[]", { ls ->
		ValueNode(ls.map { it.eval().o })
	})
	defineFunction("..", { ls ->
		if (ls.size < 2)
			tooFewArgument(2, ls.size)
		val begin = ls[0].eval().o as Int
		val end = ls[1].eval().o as Int
		val progression = when {
			begin <= end -> begin..end
			else -> (begin..end).reversed()
		}
		ValueNode(progression.toList())
	})
	defineFunction("for-each", { ls ->
		if (ls.size < 3)
			tooFewArgument(3, ls.size)
		val i = ls[0].eval()
		if (i.o !is String) typeMisMatch("String", i)
		val a = ls[1].eval()
		when (a.o) {
			is Collection<*> -> {
				var ret: Any? = null
				a.o.forEach {
					setVariable(i.o, ValueNode(it ?: Nullptr))
					ret = ls[2].eval().o
				}
				ValueNode(ret ?: Nullptr)
			}
			else -> typeMisMatch("List", a)
		}
	})
	defineFunction("size", { ls ->
		val i = ls[0].eval()
		when (i.o) {
			is Collection<*> -> ValueNode(i.o.size)
			else -> ValueNode(ls.size)
		}
	})
	defineFunction("reverse", { ls ->
		val i = ls[0].eval()
		when (i.o) {
			is Collection<*> -> ValueNode(i.o.reversed())
			else -> ValueNode(ls.size)
		}
	})
	defineFunction("count", { ls ->
		val i = ls[0].eval()
		val e = ls[1].eval()
		when (i.o) {
			is Collection<*> -> ValueNode(i.o.count { e.o == it })
			else -> ValueNode(0)
		}
	})
	defineFunction("empty?", { ls ->
		ValueNode((ls[0].eval().o as? Collection<*>)?.isEmpty() ?: true)
	})
	defineFunction("in?", { ls ->
		val i = ls[0].eval()
		val e = ls[1].eval()
		when (i.o) {
			is Collection<*> -> ValueNode(e.o in i.o)
			else -> ValueNode(false)
		}
	})
}
