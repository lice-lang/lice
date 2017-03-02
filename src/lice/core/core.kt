/**
 * Created by ice1000 on 2017/2/25.
 *
 * @author ice1000
 */
@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("Standard")
@file:JvmMultifileClass

package lice.core

import lice.compiler.model.EmptyNode
import lice.compiler.model.Node.Objects.getNullNode
import lice.compiler.model.Value.Objects.Nullptr
import lice.compiler.model.ValueNode
import lice.compiler.parse.buildNode
import lice.compiler.parse.createAst
import lice.compiler.parse.mapAst
import lice.compiler.util.InterpretException
import lice.compiler.util.InterpretException.Factory.tooFewArgument
import lice.compiler.util.InterpretException.Factory.typeMisMatch
import lice.compiler.util.ParseException.Factory.undefinedFunction
import lice.compiler.util.SymbolList
import lice.compiler.util.forceRun
import lice.compiler.util.serr
import lice.lang.Symbol
import java.io.File

inline fun SymbolList.addStandard() {
	addGetSetFunction()
	addControlFlowFunctions()
	addNumberFunctions()
	addStringFunctions()
	addBoolFunctions()
	addCollectionsFunctions()
	addListFunctions()

	defineFunction("def", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is Symbol -> {
				defineFunction(a.o, { ln, ignored -> ls[1] })
				ValueNode(a.o, ln)
			}
			is String -> {
				defineFunction(a.o, { ln, ignored -> ls[1] })
				ValueNode(a.o, ln)
			}
			else -> getNullNode(ln)
		}
	})
	defineFunction("def?", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is Symbol -> ValueNode(isFunctionDefined(a.o), ln)
			is String -> ValueNode(isFunctionDefined(a.o), ln)
			else -> ValueNode(false, ln)
		}
	})
	defineFunction("undef", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> removeFunction(a.o)
			is Symbol -> removeFunction(a.o)
		}
		getNullNode(ln)
	})
	defineFunction("call", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> (getFunction(a.o) ?: undefinedFunction(a.o, ln))
					.invoke(ln, ls.subList(1, ls.size))
			is Symbol -> (getFunction(a.o) ?: undefinedFunction(a.o.name, ln))
					.invoke(ln, ls.subList(1, ls.size))
			else -> getNullNode(ln)
		}
	})

	defineFunction("eval", { ln, ls ->
		val value = ls[0].eval()
		when (value.o) {
			is String -> ValueNode(mapAst(
					node = buildNode(value.o),
					symbolList = this
			).eval(), ln)
			else -> typeMisMatch("String", value, ln)
		}
	})

	defineFunction("print", { ln, ls ->
		ls.forEach { print(it.eval().o) }
		if (ls.isNotEmpty()) ls.last() else EmptyNode(ln)
	})
	defineFunction("print-err", { ln, ls ->
		ls.forEach { System.err.print(it.eval().o.toString()) }
		if (ls.isNotEmpty()) ls.last() else EmptyNode(ln)
	})
	defineFunction("println-err", { ln, ls ->
		ls.forEach { System.err.print(it.eval().o.toString()) }
		System.err.println()
		if (ls.isNotEmpty()) ls.last() else EmptyNode(ln)
	})
	defineFunction("println", { ln, ls ->
		ls.forEach { print(it.eval().o) }
		println()
		if (ls.isNotEmpty()) ls.last() else EmptyNode(ln)
	})

	defineFunction("new", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> ValueNode(Class.forName(a.o).newInstance(), ln)
			is Symbol -> ValueNode(Class.forName(a.o.name).newInstance(), ln)
			else -> typeMisMatch(
					expected = "String or Symbol",
					actual = a,
					lineNumber = ln
			)
		}
	})
	defineFunction("", { ln, ls ->
		ls.forEach {
			val res = it.eval()
			println("${res.o.toString()} => ${res.type.name}")
		}
		getNullNode(ln)
	})
	defineFunction("type", { ln, ls ->
		ls.forEach { println(it.eval().type.canonicalName) }
		ls.last()
	})
	defineFunction("gc", { ln, ls ->
		System.gc()
		getNullNode(ln)
	})

	defineFunction("|>", { ln, ls ->
		var ret = Nullptr
		ls.forEach { ret = it.eval() }
		ValueNode(ret, ln)
	})
	defineFunction("force|>", { ln, ls ->
		var ret = Nullptr
		forceRun { ls.forEach { node -> ret = node.eval() } }
		ValueNode(ret, ln)
	})
	defineFunction("no-run|>", { ln, ls -> getNullNode(ln) })

	defineFunction("load-file", { ln, ls ->
		val o = ls[0].eval()
		when (o.o) {
			is File -> ValueNode(createAst(
					file = o.o,
					symbolList = this
			).root.eval(), ln)
			is String -> ValueNode(createAst(
					file = File(o.o),
					symbolList = this
			).root.eval(), ln)
			else -> typeMisMatch(
					expected = "File",
					actual = o,
					lineNumber = ln
			)
		}
	})

	defineFunction("exit", { ln, ls ->
		System.exit(0)
		getNullNode(ln)
	})

	defineFunction("null?", { ln, ls -> ValueNode(null == ls[0].eval().o, ln) })
	defineFunction("!null?", { ln, ls -> ValueNode(null != ls[0].eval().o, ln) })
	defineFunction("true?", { ln, ls -> ValueNode(true == ls[0].eval().o, ln) })
	defineFunction("false?", { ln, ls -> ValueNode(false == ls[0].eval().o, ln) })

	defineFunction("str->sym", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> ValueNode(Symbol(a.o), ln)
			else -> typeMisMatch("String", a, ln)
		}
	})

	defineFunction("sym->str", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is Symbol -> ValueNode(a.o.name, ln)
			else -> typeMisMatch("Symbol", a, ln)
		}
	})
}

inline fun SymbolList.addGetSetFunction() {
	defineFunction("->", { ln, ls ->
		if (ls.size < 2)
			tooFewArgument(2, ls.size, ln)
		val str = ls[0].eval()
		val res = ValueNode(ls[1].eval(), ln)
		when (str.o) {
			is Symbol -> setVariable(str.o, res)
			else -> typeMisMatch("Symbol", str, ln)
		}
		res
	})
	defineFunction("<-", { ln, ls ->
		if (ls.isEmpty())
			tooFewArgument(1, ls.size, ln)
		val str = ls[0].eval()
		when (str.o) {
			is Symbol -> getVariable(str.o) ?: getNullNode(ln)
			else -> typeMisMatch("Symbol", str, ln)
		}
	})
	defineFunction("<->", { ln, ls ->
		if (ls.size < 2)
			tooFewArgument(2, ls.size, ln)
		val str = ls[0].eval()
		when (str.o) {
			is Symbol -> {
				if (getVariable(name = str.o) == null) {
					val node = ValueNode(ls[1].eval(), ln)
					setVariable(
							name = str.o,
							value = node
					)
					return@defineFunction node
				}
				getVariable(name = str.o)!!
			}
			else -> typeMisMatch("Symbol", str, ln)
		}
	})
}


inline fun SymbolList.addControlFlowFunctions() {
	defineFunction("if", { ln, ls ->
		if (ls.size < 2)
			tooFewArgument(2, ls.size, ln)
		val a = ls[0].eval().o
		val condition = a as? Boolean ?: (a != null)
		val ret = when {
			condition -> ls[1].eval().o
			ls.size >= 3 -> ls[2].eval().o
			else -> null
		}
		when {
			ret != null -> ValueNode(ret, ln)
			else -> getNullNode(ln)
		}
	})
	defineFunction("while", { ln, ls ->
		if (ls.size < 2)
			tooFewArgument(2, ls.size, ln)
		var a = ls[0].eval().o
		var ret: Any? = null
		while (a as? Boolean ?: (a != null)) {
			// execute loop
			ret = ls[1].eval().o
			// update a
			a = ls[0].eval().o
		}
		when {
			ret != null -> ValueNode(ret, ln)
			else -> getNullNode(ln)
		}
	})
}
