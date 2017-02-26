/**
 * Created by ice1000 on 2017/2/25.
 *
 * @author ice1000
 */
@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("Standard")
@file:JvmMultifileClass

package lice.core

import lice.compiler.model.Node.Objects.EmptyNode
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

	setFunction("def", { ls ->
		val a = ls[0].eval()
		if (a.o is String) setFunction(a.o, { ls[1] })
		ls[1]
	})
	setFunction("call", { ls ->
		val a = ls[0].eval()
		if (a.o is String) getFunction(a.o)(ls.subList(1, ls.size))
		else EmptyNode
	})

	setFunction("eval", { ls ->
		val value = ls[0].eval()
		when (value.o) {
			is String -> ValueNode(mapAst(
					node = buildNode(value.o),
					symbolList = this
			).eval())
			else -> InterpretException.typeMisMatch(
					expected = "String",
					actual = value
			)
		}
	})

	setFunction("print", { ls ->
		ls.forEach { print(it.eval().o) }
		println("")
		ls[0]
	})
	setFunction("print-err", { ls ->
		ls.forEach { System.err.print(it.eval().o.toString()) }
		serr("")
		ls[0]
	})
	setFunction("println-err", { ls ->
		ls.forEach { serr(it.eval().o.toString()) }
		ls[0]
	})
	setFunction("println", { ls ->
		ls.forEach { println(it.eval().o) }
		ls[0]
	})

	setFunction("new", { ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> ValueNode(Class.forName(a.o).newInstance())
			else -> InterpretException.typeMisMatch(
					expected = "String",
					actual = a
			)
		}
	})
	setFunction("", { ls ->
		ls.forEach {
			val res = it.eval()
			println("${res.o.toString()} => ${res.type.name}")
		}
		EmptyNode
	})
	setFunction("type", { ls ->
		ls.forEach { println(it.eval().type.canonicalName) }
		ls[0]
	})
	setFunction("gc", {
		System.gc()
		EmptyNode
	})

	setFunction("|>", { ls ->
		var ret = Nullptr
		ls.forEach { ret = it.eval() }
		ValueNode(ret)
	})
	setFunction("force|>", { ls ->
		var ret = Nullptr
		forceRun { ls.forEach { node -> ret = node.eval() } }
		ValueNode(ret)
	})
	setFunction("no-run|>", { EmptyNode })

	setFunction("load-file", { ls ->
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

	setFunction("exit", {
		System.exit(0)
		EmptyNode
	})

	setFunction("null?", { ls -> ValueNode(null == ls[0].eval().o) })
	setFunction("!null?", { ls -> ValueNode(null != ls[0].eval().o) })
	setFunction("true?", { ls -> ValueNode(true == ls[0].eval().o) })
	setFunction("false?", { ls -> ValueNode(false == ls[0].eval().o) })
}

inline fun SymbolList.addGetSetFunction() {
	setFunction("->", { ls ->
		if (ls.size < 2)
			InterpretException.tooFewArgument(
					expected = 2,
					actual = ls.size
			)
		val str = ls[0].eval()
		val res = ValueNode(ls[1].eval())
		when (str.o) {
			is String -> setVariable(str.o, res)
			else -> InterpretException.typeMisMatch(
					expected = "String",
					actual = str
			)
		}
		res
	})
	setFunction("<-", { ls ->
		if (ls.isEmpty())
			InterpretException.tooFewArgument(1, ls.size)
		val str = ls[0].eval()
		when (str.o) {
			is String -> getVariable(str.o) ?: EmptyNode
			else -> InterpretException.typeMisMatch(
					expected = "String",
					actual = str
			)
		}
	})
	setFunction("<->", { ls ->
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
			else -> InterpretException.typeMisMatch(
					expected = "String",
					actual = str
			)
		}
	})
}


inline fun SymbolList.addControlFlowFunctions() {
	setFunction("if", { ls ->
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
			else -> EmptyNode
		}
	})
	setFunction("while", { ls ->
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
			else -> EmptyNode
		}
	})
}
