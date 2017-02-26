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
import java.io.File
import kotlin.concurrent.thread

inline fun SymbolList.addStandard() {
	addGetSetFunction()
	addControlFlowFunctions()
	addNumberFunctions()
	addStringFunctions()
	addBoolFunctions()
	addCollectionsFunctions()

	addFunction("def", { ls ->
		val a = ls[0].eval()
		if (a.o is String) addFunction(a.o, { ls[1] })
		ls[1]
	})
	addFunction("call", { ls ->
		val a = ls[0].eval()
		if (a.o is String) getFunction(getFunctionId(a.o)
				?: throw InterpretException("function not found: ${a.o}"))(ls.subList(1, ls.size))
		else EmptyNode
	})

	addFunction("eval", { ls ->
		val value = ls[0].eval()
		when (value.o) {
			is String -> ValueNode(mapAst(buildNode(value.o), this).eval())
			else -> InterpretException.typeMisMatch("String", value)
		}
	})

	addFunction("print", { ls ->
		ls.forEach { print(it.eval().o) }
		println("")
		ls[0]
	})
	addFunction("print-err", { ls ->
		ls.forEach { System.err.print(it.eval().o.toString()) }
		ls[0]
	})
	addFunction("println", { ls ->
		ls.forEach { println(it.eval().o) }
		ls[0]
	})

	addFunction("new", { ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> ValueNode(Class.forName(a.o).newInstance())
			else -> InterpretException.typeMisMatch("String", a)
		}
	})
	addFunction("", { ls ->
		ls.forEach {
			val res = it.eval()
			println("${res.o.toString()} => ${res.type.name}")
		}
		EmptyNode
	})
	addFunction("type", { ls ->
		ls.forEach { println(it.eval().type.canonicalName) }
		ls[0]
	})
	addFunction("gc", {
		System.gc()
		EmptyNode
	})

	addFunction("|>", { ls ->
		var ret = Nullptr
		ls.forEach { ret = it.eval() }
		ValueNode(ret)
	})
	addFunction("force|>", { ls ->
		var ret = Nullptr
		forceRun { ls.forEach { node -> ret = node.eval() } }
		ValueNode(ret)
	})
	addFunction("thread|>", { ls ->
		thread { ls.forEach { node -> node.eval() } }
		EmptyNode
	})

	addFunction("load-file", { ls ->
		val o = ls[0].eval()
		when (o.o) {
			is File -> ValueNode(createAst(o.o, this).root.eval())
			else -> InterpretException.typeMisMatch("File", o)
		}
	})

	addFunction("null?", { ls -> ValueNode(null == ls[0].eval().o) })
	addFunction("!null?", { ls -> ValueNode(null != ls[0].eval().o) })
	addFunction("true?", { ls -> ValueNode(true == ls[0].eval().o) })
	addFunction("false?", { ls -> ValueNode(false == ls[0].eval().o) })
}

inline fun SymbolList.addGetSetFunction() {
	addFunction("->", { ls ->
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
	addFunction("<-", { ls ->
		if (ls.isEmpty())
			InterpretException.tooFewArgument(1, ls.size)
		val str = ls[0].eval()
		when (str.o) {
			is String -> getVariable(str.o) ?: EmptyNode
			else -> InterpretException.typeMisMatch("String", str)
		}
	})
	addFunction("<->", { ls ->
		if (ls.size < 2)
			InterpretException.tooFewArgument(2, ls.size)
		val str = ls[0].eval()
		when (str.o) {
			is String -> {
				if (getVariable(str.o) == null) setVariable(str.o, ValueNode(ls[1].eval()))
				getVariable(str.o)!!
			}
			else -> InterpretException.typeMisMatch("String", str)
		}
	})
}


inline fun SymbolList.addControlFlowFunctions() {
	addFunction("if", { ls ->
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
	addFunction("while", { ls ->
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
