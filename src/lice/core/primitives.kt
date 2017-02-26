/**
 * Created by ice1000 on 2017/2/25.
 *
 * @author ice1000
 */
@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("Standard")
@file:JvmMultifileClass

package lice.core

import lice.compiler.model.Node
import lice.compiler.model.ValueNode
import lice.compiler.util.InterpretException
import lice.compiler.util.SymbolList


inline fun SymbolList.addNumberFunctions() {
	defineFunction("int->double", { ls ->
		ValueNode((ls[0].eval().o as Int).toDouble())
	})
	defineFunction("+", { list ->
		ValueNode(list.fold(0) { sum, value ->
			val res = value.eval()
			when (res.o) {
				is Int -> res.o + sum
				else -> InterpretException.typeMisMatch("Int", res)
			}
		})
	})
	defineFunction("-", { ls ->
		when (ls.size) {
			0 -> ValueNode(0)
			1 -> ValueNode(ls[0].eval())
			else -> {
				var res = ls[0].eval().o as Int
				for (i in 1..ls.size - 1)
					res -= ls[i].eval().o as Int
				ValueNode(res)
			}
		}
	})
	defineFunction("/", { ls ->
		when (ls.size) {
			0 -> ValueNode(1)
			1 -> ValueNode(ls[0].eval())
			else -> {
				var res = ls[0].eval().o as Int
				for (i in 1..ls.size - 1)
					res /= ls[i].eval().o as Int
				ValueNode(res)
			}
		}
	})
	defineFunction("%", { ls ->
		when (ls.size) {
			0 -> ValueNode(0)
			1 -> ValueNode(ls[0].eval())
			else -> {
				var res = ls[0].eval().o as Int
				@Suppress("DEPRECATION")
				for (i in 1..ls.size - 1)
					res = res.mod(ls[i].eval().o as Int)
				ValueNode(res)
			}
		}
	})
	defineFunction("*", { ls ->
		ValueNode(ls.fold(1) { sum, value ->
			val res = value.eval()
			when (res.o) {
				is Int -> res.o * sum
				else -> InterpretException.typeMisMatch("Int", res)
			}
		})
	})
	defineFunction("==", { list ->
		val ls = list.map(Node::eval)
		ValueNode((1..ls.size - 1).all {
			ls[it].o == ls[it - 1].o
		})
	})
	defineFunction("!=", { list ->
		val ls = list.map(Node::eval)
		ValueNode((1..ls.size - 1).all {
			ls[it].o != ls[it - 1].o
		})
	})
	defineFunction("<", { list ->
		val ls = list.map(Node::eval)
		ValueNode((1..ls.size - 1).all {
			(ls[it - 1].o as Int) < (ls[it].o as Int)
		})
	})
	defineFunction(">", { list ->
		val ls = list.map(Node::eval)
		ValueNode((1..ls.size - 1).all {
			ls[it - 1].o as Int > (ls[it].o as Int)
		})
	})
	defineFunction(">=", { list ->
		val ls = list.map(Node::eval)
		ValueNode((1..ls.size - 1).all {
			(ls[it - 1].o as Int) >= (ls[it].o as Int)
		})
	})
	defineFunction("<=", { ls ->
		val list = ls.map(Node::eval)
		ValueNode((1..list.size - 1).all {
			(list[it - 1].o as Int) <= (list[it].o as Int)
		})
	})
	defineFunction("&", { list ->
		ValueNode(list.fold(0) { sum, value ->
			val res = value.eval()
			when (res.o) {
				is Int -> res.o and sum
				else -> InterpretException.typeMisMatch("Int", res)
			}
		})
	})
	defineFunction("|", { list ->
		ValueNode(list.fold(0) { sum, value ->
			val res = value.eval()
			when (res.o) {
				is Int -> res.o or sum
				else -> InterpretException.typeMisMatch("Int", res)
			}
		})
	})
	defineFunction("^", { list ->
		ValueNode(list.fold(0) { sum, value ->
			val res = value.eval()
			when (res.o) {
				is Int -> res.o xor sum
				else -> InterpretException.typeMisMatch("Int", res)
			}
		})
	})
}

inline fun SymbolList.addBoolFunctions() {
	defineFunction("&&", { ls ->
		ValueNode(ls.fold(true) { sum, value ->
			val o = value.eval()
			when {
				o.o is Boolean -> o.o && sum
				else -> InterpretException.typeMisMatch("Boolean", o)
			}
		})
	})
	defineFunction("||", { ls ->
		ValueNode(ls.fold(false) { sum, value ->
			val o = value.eval()
			when {
				o.o is Boolean -> o.o || sum
				else -> InterpretException.typeMisMatch("Boolean", o)
			}
		})
	})
	defineFunction("!", { ls ->
		ValueNode(!(ls[0].eval().o as Boolean))
	})
}


