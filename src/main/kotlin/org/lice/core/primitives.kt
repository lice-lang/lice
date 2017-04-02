/**
 * Primitives
 * Created by ice1000 on 2017/2/25.
 *
 * @author ice1000
 * @since 1.0.0
 */
@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("Standard")
@file:JvmMultifileClass

package org.lice.core

import org.lice.compiler.model.Node
import org.lice.compiler.model.ValueNode
import org.lice.compiler.util.InterpretException.Factory.typeMisMatch
import org.lice.compiler.util.SymbolList
import org.lice.lang.NumberOperator
import org.lice.lang.NumberOperator.Leveler.compare

inline fun SymbolList.addNumberFunctions() {
	defineFunction("->double", { ln, ls ->
		ValueNode((ls[0].eval().o as Number).toDouble(), ln)
	})
	defineFunction("->int", { ln, ls ->
		ValueNode((ls[0].eval().o as Number).toInt(), ln)
	})
	defineFunction("+", { ln, list ->
		ValueNode(list.fold(NumberOperator(0)) { sum, value ->
			val res = value.eval()
			when (res.o) {
				is Number -> sum.plus(res.o, ln)
				else -> typeMisMatch("Number", res, ln)
			}
		}.result, ln)
	})
	defineFunction("-", { ln, ls ->
		when (ls.size) {
			0 -> ValueNode(0, ln)
			1 -> ValueNode(ls[0].eval(), ln)
			else -> ValueNode(ls
					.subList(1, ls.size)
					.fold(NumberOperator(ls[0].eval().o as Number)) { sum, value ->
						val res = value.eval()
						when (res.o) {
							is Number -> sum.minus(res.o, ln)
							else -> typeMisMatch("Number", res, ln)
						}
					}.result, ln)
		}
	})
	defineFunction("/", { ln, ls ->
		when (ls.size) {
			0 -> ValueNode(0, ln)
			1 -> ValueNode(ls[0].eval(), ln)
			else -> ValueNode(ls
					.subList(1, ls.size)
					.fold(NumberOperator(ls[0].eval().o as Number)) { sum, value ->
						val res = value.eval()
						when (res.o) {
							is Number -> sum.div(res.o, ln)
							else -> typeMisMatch("Number", res, ln)
						}
					}.result, ln)
		}
	})
	defineFunction("%", { ln, ls ->
		when (ls.size) {
			0 -> ValueNode(0, ln)
			1 -> ValueNode(ls[0].eval(), ln)
			else -> ValueNode(ls
					.subList(1, ls.size)
					.fold(NumberOperator(ls[0].eval().o as Number)) { sum, value ->
						val res = value.eval()
						when (res.o) {
							is Number -> sum.rem(res.o, ln)
							else -> typeMisMatch("Number", res, ln)
						}
					}.result, ln)
		}
	})
	defineFunction("*", { ln, ls ->
		ValueNode(ls.fold(NumberOperator(1)) { sum, value ->
			val res = value.eval()
			when (res.o) {
				is Number -> sum.times(res.o, ln)
				else -> typeMisMatch("Number", res, ln)
			}
		}.result, ln)
	})
	defineFunction("===", { ln, list ->
		val ls = list.map(Node::eval)
		ValueNode((1..ls.size - 1).all {
			ls[it].o == ls[it - 1].o
		}, ln)
	})
	defineFunction("==", { ln, list ->
		val ls = list.map { it.eval().o as Number }
		ValueNode((1..ls.size - 1).all {
			compare(ls[it - 1], ls[it], ln) == 0
		}, ln)
	})
	defineFunction("!==", { ln, list ->
		val ls = list.map(Node::eval)
		ValueNode((1..ls.size - 1).all {
			ls[it].o != ls[it - 1].o
		}, ln)
	})
	defineFunction("!=", { ln, list ->
		val ls = list.map { it.eval().o as Number }
		ValueNode((1..ls.size - 1).all {
			compare(ls[it - 1], ls[it], ln) != 0
		}, ln)
	})
	defineFunction("<", { ln, list ->
		val ls = list.map { it.eval().o as Number }
		ValueNode((1..ls.size - 1).all {
			compare(ls[it - 1], ls[it], ln) < 0
		}, ln)
	})
	defineFunction(">", { ln, list ->
		val ls = list.map { it.eval().o as Number }
		ValueNode((1..ls.size - 1).all {
			compare(ls[it - 1], ls[it], ln) > 0
		}, ln)
	})
	defineFunction(">=", { ln, list ->
		val ls = list.map { it.eval().o as Number }
		ValueNode((1..ls.size - 1).all {
			compare(ls[it - 1], ls[it], ln) >= 0
		}, ln)
	})
	defineFunction("<=", { ln, list ->
		val ls = list.map { it.eval().o as Number }
		ValueNode((1..ls.size - 1).all {
			compare(ls[it - 1], ls[it], ln) <= 0
		}, ln)
	})
	defineFunction("&", { ln, ls ->
		val list = ls.map(Node::eval)
		ValueNode((1..list.size - 1).fold(list[0].o as Int) { last, self ->
			last and list[self].o as Int
		}, ln)
	})
	defineFunction("|", { ln, ls ->
		val list = ls.map(Node::eval)
		ValueNode((1..list.size - 1).fold(list[0].o as Int) { last, self ->
			last or list[self].o as Int
		}, ln)
	})
	defineFunction("^", { ln, ls ->
		val list = ls.map(Node::eval)
		ValueNode((1..list.size - 1).fold(list[0].o as Int) { last, self ->
			last xor list[self].o as Int
		}, ln)
	})
}

inline fun SymbolList.addBoolFunctions() {
	defineFunction("&&", { ln, ls ->
		ValueNode(ls.all {
			val o = it.eval()
			o.o as? Boolean ?: typeMisMatch("Boolean", o, ln)
		}, ln)
	})
	defineFunction("||", { ln, ls ->
		ValueNode(ls.any {
			val o = it.eval()
			o.o as? Boolean ?: typeMisMatch("Boolean", o, ln)
		}, ln)
	})
}


