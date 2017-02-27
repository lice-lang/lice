/**
 * Created by ice1000 on 2017/2/26.
 *
 * @author ice1000
 */
@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("Standard")
@file:JvmMultifileClass

package lice.core

import lice.compiler.model.ValueNode
import lice.compiler.util.InterpretException.Factory.typeMisMatch
import lice.compiler.util.SymbolList

inline fun SymbolList.addMathFunctions() {
	defineFunction("sqrt", { ln, ls ->
		ValueNode(Math.sqrt((ls[0].eval().o as Int).toDouble()), ln)
	})
	defineFunction("rand", { ln, ls -> ValueNode(rand.nextInt(), ln) })
	defineFunction("abs", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is Int -> ValueNode(Math.abs(a.o), ln)
			is Double -> ValueNode(Math.abs(a.o), ln)
			is Long -> ValueNode(Math.abs(a.o), ln)
			is Float -> ValueNode(Math.abs(a.o), ln)
			else -> typeMisMatch("Number", a)
		}
	})
	defineFunction("min", { ln, ls ->
		ValueNode(ls.fold(Int.MAX_VALUE) { min, value ->
			val res = value.eval()
			when (res.o) {
				is Int -> if (res.o > min) min else res.o
				else -> typeMisMatch("Int", res)
			}
		}, ln)
	})
	defineFunction("max", { ln, ls ->
		ValueNode(ls.fold(Int.MIN_VALUE) { max, value ->
			val res = value.eval()
			when (res.o) {
				is Int -> if (res.o < max) max else res.o
				else -> typeMisMatch("Int", res)
			}
		}, ln)
	})
}
