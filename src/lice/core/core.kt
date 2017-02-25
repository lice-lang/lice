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
import lice.compiler.model.Node.Objects.EmptyNode
import lice.compiler.model.Value.Objects.Nullptr
import lice.compiler.model.ValueNode
import lice.compiler.parse.buildNode
import lice.compiler.parse.createAst
import lice.compiler.parse.mapAst
import lice.compiler.util.InterpretException
import lice.compiler.util.InterpretException.Factory.typeMisMatch
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
}

inline fun SymbolList.addNumberFunctions() {
	addFunction("int->double", { ls ->
		ValueNode((ls[0].eval().o as Int).toDouble())
	})
	addFunction("+", { list ->
		ValueNode(list.fold(0) { sum, value ->
			val res = value.eval()
			when (res.o) {
				is Int -> res.o + sum
				else -> typeMisMatch("Int", res)
			}
		})
	})
	addFunction("-", { ls ->
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
	addFunction("/", { ls ->
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
	addFunction("%", { ls ->
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
	addFunction("*", { ls ->
		ValueNode(ls.fold(1) { sum, value ->
			val res = value.eval()
			when (res.o) {
				is Int -> res.o * sum
				else -> InterpretException.typeMisMatch("Int", res)
			}
		})
	})
	addFunction("==", { list ->
		val ls = list.map(Node::eval)
		ValueNode((1..ls.size - 1).none {
			ls[it].o != ls[it - 1].o
		})
	})
	addFunction("!=", { list ->
		val ls = list.map(Node::eval)
		ValueNode((1..ls.size - 1).none {
			ls[it].o == ls[it - 1].o
		})
	})
	addFunction("<", { list ->
		val ls = list.map(Node::eval)
		ValueNode((1..ls.size - 1).none {
			ls[it].o as Int <= ls[it - 1].o as Int
		})
	})
	addFunction(">", { list ->
		val ls = list.map(Node::eval)
		ValueNode((1..ls.size - 1).none {
			(ls[it].o as Int) >= ls[it - 1].o as Int
		})
	})
	addFunction(">=", { list ->
		val ls = list.map(Node::eval)
		ValueNode((1..ls.size - 1).none {
			ls[it].o as Int > ls[it - 1].o as Int
		})
	})
	addFunction("<=", { ls ->
		val list = ls.map(Node::eval)
		ValueNode((1..list.size - 1).none {
			(list[it].o as Int) < list[it - 1].o as Int
		})
	})
}

inline fun SymbolList.addBoolFunctions() {
	addFunction("&&", { ls ->
		ValueNode(ls.fold(true) { sum, value ->
			val o = value.eval()
			when {
				o.o is Boolean -> o.o && sum
				else -> InterpretException.typeMisMatch("Boolean", o)
			}
		})
	})
	addFunction("||", { ls ->
		ValueNode(ls.fold(false) { sum, value ->
			val o = value.eval()
			when {
				o.o is Boolean -> o.o || sum
				else -> InterpretException.typeMisMatch("Boolean", o)
			}
		})
	})
	addFunction("!", { ls ->
		ValueNode(!(ls[0].eval().o as Boolean))
	})
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

inline fun SymbolList.addCollectionsFunctions() {
	addFunction("[]", { ls ->
		ValueNode(ls.map { it.eval().o })
	})
	addFunction("..", { ls ->
		if (ls.size < 2)
			InterpretException.tooFewArgument(2, ls.size)
		val begin = ls[0].eval().o as Int
		val end = ls[1].eval().o as Int
		val progression = when {
			begin <= end -> begin..end
			else -> (begin..end).reversed()
		}
		ValueNode(progression.toList())
	})
	addFunction("load-file", { ls ->
		val o = ls[0].eval()
		when (o.o) {
			is File -> ValueNode(createAst(o.o, this).root.eval())
			else -> InterpretException.typeMisMatch("File", o)
		}
	})
	addFunction("for-each", { ls ->
		if (ls.size < 3)
			InterpretException.tooFewArgument(3, ls.size)
		val i = ls[0].eval()
		if (i.o !is String) InterpretException.typeMisMatch("String", i)
		val a = ls[1].eval()
		when (a.o) {
			is List<*> -> {
				var ret: Any? = null
				a.o.forEach {
					setVariable(i.o, ValueNode(it ?: Nullptr))
					ret = ls[2].eval().o
				}
				ValueNode(ret ?: Nullptr)
			}
			else -> InterpretException.typeMisMatch("List", a)
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
