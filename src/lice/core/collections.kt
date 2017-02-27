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

import lice.lang.Pair
import lice.lang.Symbol

inline fun SymbolList.addListFunctions() {
	defineFunction("[|]", { ln, ls ->
		ValueNode(ls.foldRight(null) { value, pairs: Any? ->
			Pair(value.eval().o, pairs)
		}, Pair::class.java, ln)
	})
	defineFunction("head", { ln, ls ->
		val a = ls[0].eval()
		if (a.o is Pair<*, *>) when (a.o.first) {
			null -> EmptyNode(ln)
			else -> ValueNode(a.o.first, ln)
		}
		else typeMisMatch("Pair", a)
	})
	defineFunction("tail", { ln, ls ->
		val a = ls[0].eval()
		if (a.o is Pair<*, *>) when (a.o.second) {
			null -> EmptyNode(ln)
			else -> ValueNode(a.o.second, ln)
		}
		else typeMisMatch("Pair", a)
	})
}

inline fun SymbolList.addCollectionsFunctions() {
	defineFunction("cons", { ln, ls ->
		ValueNode(ls.map { it.eval().o }, ln)
	})
	defineFunction("..", { ln, ls ->
		if (ls.size < 2)
			tooFewArgument(2, ls.size)
		val begin = ls[0].eval().o as Int
		val end = ls[1].eval().o as Int
		val progression = when {
			begin <= end -> begin..end
			else -> (begin..end).reversed()
		}
		ValueNode(progression.toList(), ln)
	})
	defineFunction("for-each", { ln, ls ->
		if (ls.size < 3)
			tooFewArgument(3, ls.size)
		val i = ls[0].eval()
		if (i.o !is Symbol) typeMisMatch("Symbol", i)
		val a = ls[1].eval()
		when (a.o) {
			is Collection<*> -> {
				var ret: Any? = null
				a.o.forEach {
					setVariable(i.o, ValueNode(it ?: Nullptr, ln))
					ret = ls[2].eval().o
				}
				ValueNode(ret ?: Nullptr, ln)
			}
			else -> typeMisMatch("List", a)
		}
	})
	defineFunction("size", { ln, ls ->
		val i = ls[0].eval()
		when (i.o) {
			is Collection<*> -> ValueNode(i.o.size, ln)
			else -> ValueNode(ls.size, ln)
		}
	})
	defineFunction("reverse", { ln, ls ->
		val i = ls[0].eval()
		when (i.o) {
			is Collection<*> -> ValueNode(i.o.reversed(), ln)
			else -> ValueNode(ls.size, ln)
		}
	})
	defineFunction("count", { ln, ls ->
		val i = ls[0].eval()
		val e = ls[1].eval()
		when (i.o) {
			is Collection<*> -> ValueNode(i.o.count { e.o == it }, ln)
			else -> ValueNode(0, ln)
		}
	})
	defineFunction("empty?", { ln, ls ->
		ValueNode((ls[0].eval().o as? Collection<*>)?.isEmpty() ?: true, ln)
	})
	defineFunction("in?", { ln, ls ->
		val i = ls[0].eval()
		val e = ls[1].eval()
		when (i.o) {
			is Collection<*> -> ValueNode(e.o in i.o, ln)
			else -> ValueNode(false, ln)
		}
	})
}
