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
	setVariable("PI", ValueNode(Math.PI, -1))
	setVariable("E", ValueNode(Math.E, -1))
	defineFunction("sqrt", { ln, ls ->
		ValueNode(Math.sqrt((ls[0].eval().o as Number).toDouble()), ln)
	})
	defineFunction("sin", { ln, ls ->
		ValueNode(Math.sin((ls[0].eval().o as Number).toDouble()), ln)
	})
	defineFunction("sinh", { ln, ls ->
		ValueNode(Math.sinh((ls[0].eval().o as Number).toDouble()), ln)
	})
	defineFunction("cos", { ln, ls ->
		ValueNode(Math.cos((ls[0].eval().o as Number).toDouble()), ln)
	})
	defineFunction("cosh", { ln, ls ->
		ValueNode(Math.cosh((ls[0].eval().o as Number).toDouble()), ln)
	})
	defineFunction("tan", { ln, ls ->
		ValueNode(Math.tan((ls[0].eval().o as Number).toDouble()), ln)
	})
	defineFunction("tanh", { ln, ls ->
		ValueNode(Math.tanh((ls[0].eval().o as Number).toDouble()), ln)
	})
	defineFunction("rand", { ln, ls -> ValueNode(rand.nextInt(), ln) })
	defineFunction("abs", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is Int -> ValueNode(Math.abs(a.o), ln)
			is Double -> ValueNode(Math.abs(a.o), ln)
			is Long -> ValueNode(Math.abs(a.o), ln)
			is Float -> ValueNode(Math.abs(a.o), ln)
			else -> typeMisMatch("Number", a, ln)
		}
	})
	defineFunction("min", { ln, ls ->
		ValueNode(ls.fold(Int.MAX_VALUE as Number) { min, value ->
			val res = value.eval()
			when (res.o) {
				is Number -> if (res.o.toDouble() > min.toDouble()) min else res.o
				else -> typeMisMatch("Int", res, ln)
			}
		}, ln)
	})
	defineFunction("max", { ln, ls ->
		ValueNode(ls.fold(Int.MIN_VALUE as Number) { max, value ->
			val res = value.eval()
			when (res.o) {
				is Number -> if (res.o.toDouble() < max.toDouble()) max else res.o
				else -> typeMisMatch("Int", res, ln)
			}
		}, ln)
	})
}
