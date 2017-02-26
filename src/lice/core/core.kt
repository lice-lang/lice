/**
 * Created by ice1000 on 2017/2/25.
 *
 * @author ice1000
 */
@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("Standard")
@file:JvmMultifileClass

package lice.core

import lice.compiler.model.Node.Objects.NullNode
import lice.compiler.model.Value.Objects.Nullptr
import lice.compiler.model.ValueNode
import lice.compiler.parse.buildNode
import lice.compiler.parse.createAst
import lice.compiler.parse.mapAst
import lice.compiler.util.InterpretException
import lice.compiler.util.SymbolList
import lice.compiler.util.forceRun
import lice.compiler.util.serr
import java.io.File

inline fun SymbolList.addStandard() {
	addGetSetFunction()
	addControlFlowFunctions()
	addNumberFunctions()
	addStringFunctions()
	addBoolFunctions()
	addCollectionsFunctions()
	addListFunctions()

	defineFunction("def", { ls ->
		val a = ls[0].eval()
		if (a.o is String) defineFunction(a.o, { ls[1] })
		NullNode
	})
	defineFunction("undef", { ls ->
		val a = ls[0].eval()
		if (a.o is String) removeFunction(a.o)
		NullNode
	})
	defineFunction("call", { ls ->
		val a = ls[0].eval()
		if (a.o is String) getFunction(a.o)(ls.subList(1, ls.size))
		else NullNode
	})

	defineFunction("eval", { ls ->
		val value = ls[0].eval()
		when (value.o) {
			is String -> ValueNode(mapAst(
					node = buildNode(value.o),
					symbolList = this
			).eval())
			else -> InterpretException.typeMisMatch("String", value)
		}
	})

	defineFunction("print", { ls ->
		ls.forEach { print(it.eval().o) }
		println("")
		ls.last()
	})
	defineFunction("print-err", { ls ->
		ls.forEach { System.err.print(it.eval().o.toString()) }
		serr("")
		ls.last()
	})
	defineFunction("println-err", { ls ->
		ls.forEach { serr(it.eval().o.toString()) }
		ls.last()
	})
	defineFunction("println", { ls ->
		ls.forEach { println(it.eval().o) }
		ls.last()
	})

	defineFunction("new", { ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> ValueNode(Class.forName(a.o).newInstance())
			else -> InterpretException.typeMisMatch(
					expected = "String",
					actual = a
			)
		}
	})
	defineFunction("", { ls ->
		ls.forEach {
			val res = it.eval()
			println("${res.o.toString()} => ${res.type.name}")
		}
		NullNode
	})
	defineFunction("type", { ls ->
		ls.forEach { println(it.eval().type.canonicalName) }
		ls.last()
	})
	defineFunction("gc", {
		System.gc()
		NullNode
	})

	defineFunction("|>", { ls ->
		var ret = Nullptr
		ls.forEach { ret = it.eval() }
		ValueNode(ret)
	})
	defineFunction("force|>", { ls ->
		var ret = Nullptr
		forceRun { ls.forEach { node -> ret = node.eval() } }
		ValueNode(ret)
	})
	defineFunction("no-run|>", { NullNode })

	defineFunction("load-file", { ls ->
		val o = ls[0].eval()
		when (o.o) {
			is File -> ValueNode(createAst(
					file = o.o,
					symbolList = this
			).root.eval())
			else -> InterpretException.typeMisMatch(
					expected = "File",
					actual = o
			)
		}
	})

	defineFunction("exit", {
		System.exit(0)
		NullNode
	})

	defineFunction("null?", { ls -> ValueNode(null == ls[0].eval().o) })
	defineFunction("!null?", { ls -> ValueNode(null != ls[0].eval().o) })
	defineFunction("true?", { ls -> ValueNode(true == ls[0].eval().o) })
	defineFunction("false?", { ls -> ValueNode(false == ls[0].eval().o) })
}

inline fun SymbolList.addGetSetFunction() {
	defineFunction("->", { ls ->
		if (ls.size < 2)
			InterpretException.tooFewArgument(2, ls.size)
		val str = ls[0].eval()
		val res = ValueNode(ls[1].eval())
		when (str.o) {
			is String -> setVariable(str.o, res)
			else -> InterpretException.typeMisMatch("String", str)
		}
		res
	})
	defineFunction("<-", { ls ->
		if (ls.isEmpty())
			InterpretException.tooFewArgument(1, ls.size)
		val str = ls[0].eval()
		when (str.o) {
			is String -> getVariable(str.o) ?: NullNode
			else -> InterpretException.typeMisMatch("String", str)
		}
	})
	defineFunction("<->", { ls ->
		if (ls.size < 2)
			InterpretException.tooFewArgument(2, ls.size)
		val str = ls[0].eval()
		when (str.o) {
			is String -> {
				if (getVariable(name = str.o) == null)
					setVariable(
							name = str.o,
							value = ValueNode(ls[1].eval())
					)
				getVariable(name = str.o)!!
			}
			else -> InterpretException.typeMisMatch("String", str)
		}
	})
}


inline fun SymbolList.addControlFlowFunctions() {
	defineFunction("if", { ls ->
		if (ls.size < 2)
			InterpretException.tooFewArgument(2, ls.size)
		val a = ls[0].eval().o
		val condition = a as? Boolean ?: (a != null)
		val ret = when {
			condition -> ls[1].eval().o
			ls.size >= 3 -> ls[2].eval().o
			else -> null
		}
		when {
			ret != null -> ValueNode(ret)
			else -> NullNode
		}
	})
	defineFunction("while", { ls ->
		if (ls.size < 2)
			InterpretException.tooFewArgument(2, ls.size)
		var a = ls[0].eval().o
		var ret: Any? = null
		while (a as? Boolean ?: (a != null)) {
			// execute loop
			ret = ls[1].eval().o
			// update a
			a = ls[0].eval().o
		}
		when {
			ret != null -> ValueNode(ret)
			else -> NullNode
		}
	})
}
