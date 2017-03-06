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
import lice.compiler.util.SymbolList

inline fun SymbolList.addMathFunctions() {
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
	defineFunction("rand", { ln, _ -> ValueNode(rand.nextInt(), ln) })
}
