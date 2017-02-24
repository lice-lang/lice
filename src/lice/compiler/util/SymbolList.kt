/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 */
package lice.compiler.util

import lice.compiler.model.Value
import lice.compiler.model.Value.Objects.nullptr
import lice.compiler.parse.buildNode
import lice.compiler.parse.createAst
import lice.compiler.parse.mapAst
import lice.compiler.util.InterpretException.Factory.tooFewArgument
import lice.compiler.util.InterpretException.Factory.typeMisMatch
import java.io.File
import java.net.URL

@Suppress("NOTHING_TO_INLINE")

class SymbolList(init: Boolean = true) {
	val functionMap: MutableMap<String, Int>
	val functionList: MutableList<(List<Value>) -> Value>

	val variableMap: MutableMap<String, Value>

	init {
		functionMap = mutableMapOf()
		functionList = mutableListOf()
		variableMap = mutableMapOf()
		if (init) initialize()
	}

	inline fun addNumberFunctions() {
		addFunction("+", { ls ->
			Value(ls.fold(0) { sum, value ->
				if (value.o is Int) value.o + sum
				else typeMisMatch("Int", value)
			})
		})
		addFunction("-", { ls ->
			Value(ls.fold(ls[0].o as Int shl 1) { delta, value ->
				if (value.o is Int) delta - value.o
				else typeMisMatch("Int", value)
			})
		})
		addFunction("/", { ls ->
			Value(ls.fold((ls[0].o as Int).squared()) { res, value ->
				if (value.o is Int) res / value.o
				else typeMisMatch("Int", value)
			})
		})
		addFunction("*", { ls ->
			Value(ls.fold(1) { sum, value ->
				if (value.o is Int) value.o * sum
				else typeMisMatch("Int", value)
			})
		})
		addFunction("==", { ls ->
			Value((1..ls.size - 1).none { it -> ls[it].o != ls[it - 1].o })
		})
		addFunction("!=", { ls ->
			Value((1..ls.size - 1).none { it -> ls[it].o == ls[it - 1].o })
		})
		addFunction("<", { ls ->
			Value((1..ls.size - 1).none {
				ls[it].o as Int <= ls[it - 1].o as Int
			})
		})
		addFunction(">", { ls ->
			Value((1..ls.size - 1).none {
				(ls[it].o as Int) >= ls[it - 1].o as Int
			})
		})
		addFunction(">=", { ls ->
			Value((1..ls.size - 1).none {
				ls[it].o as Int > ls[it - 1].o as Int
			})
		})
		addFunction("<", { ls ->
			Value((1..ls.size - 1).none {
				(ls[it].o as Int) < ls[it - 1].o as Int
			})
		})
	}

	inline fun addBoolFunctions() {
		addFunction("&&", { ls ->
			Value(ls.fold(true) { sum, value ->
				if (value.o is Boolean) value.o && sum
				else typeMisMatch("Boolean", value)
			})
		})
		addFunction("||", { ls ->
			Value(ls.fold(false) { sum, value ->
				if (value.o is Boolean) value.o || sum
				else typeMisMatch("Boolean", value)
			})
		})
		addFunction("!", { ls -> Value(!(ls[0].o as Boolean)) })
	}

	inline fun addFileFunctions() {
		addFunction("file", { ls ->
			val a = ls[0].o
			if (a is String) Value(File(a).apply { if (!exists()) createNewFile() })
			else typeMisMatch("String", ls[0])
		})
		addFunction("read-file", { ls ->
			val a = ls[0].o
			if (a is File) Value(a.readText())
			else typeMisMatch("File", ls[0])
		})
		addFunction("url", { ls ->
			val a = ls[0].o
			if (a is String) Value(URL(a))
			else typeMisMatch("String", ls[0])
		})
		addFunction("read-url", { ls ->
			val a = ls[0].o
			if (a is URL) Value(a.readText())
			else typeMisMatch("URL", ls[0])
		})
		addFunction("load-file", { ls ->
			val o = ls[0].o
			if (o is File) createAst(o).root.eval()
			else typeMisMatch("File", ls[0])
		})
		addFunction("write-file", { ls ->
			val a = ls[0].o
			val b = ls[1].o
			if (a is File) {
				if (b is String) a.writeText(b)
				else typeMisMatch("String", ls[1])
			} else typeMisMatch("File", ls[0])
			Value(b)
		})
	}

	inline fun addGetSetFunction() {
		addFunction("set", { ls ->
			if (ls.size < 2)
				tooFewArgument(2, ls.size)
			val str = ls[0].o
			if (str is String) addVariable(str, ls[1])
			else typeMisMatch("String", ls[0])
			ls[1]
		})
		addFunction("get", { ls ->
			if (ls.isEmpty())
				tooFewArgument(1, ls.size)
			val str = ls[0].o
			if (str is String) {
				val value = variableMap[str]
				value ?: nullptr
			} else typeMisMatch("String", ls[0])
		})
	}

	inline fun addStringFunctions() {
		addFunction("to-str", { ls -> Value(ls[0].o.toString()) })
		addFunction("str-con", { ls ->
			Value(ls.fold(StringBuilder(ls.size)) { sb, value ->
				if (value.o is String) sb.append(value.o)
				else typeMisMatch("String", value)
			}.toString())
		})
		addFunction("print", { ls ->
			ls.forEach { println(it.o) }
			ls[ls.size - 1]
		})
		addFunction("eval", { ls ->
			val o = ls[0].o
			if (o is String) {
				val symbolList = SymbolList(true)
				val stringTreeRoot = buildNode(o)
				mapAst(stringTreeRoot, symbolList).eval()
			} else typeMisMatch("String", ls[0])
		})
	}

	fun initialize() {
		addNumberFunctions()
		addFileFunctions()
		addGetSetFunction()
		addStringFunctions()
		addBoolFunctions()
		addFunction("[]", { ls -> Value(ls.map { it.o }) })
		addFunction("", { ls ->
			ls.forEach { println("${it.o.toString()} => ${it.type.name}") }
			ls[ls.size - 1]
		})
		addFunction("if", { ls ->
			if (ls.size < 2)
				tooFewArgument(2, ls.size)
			val condition = ls[0].o as? Boolean ?: ls[1].o != null
			val ret = if (condition) ls[1].o else ls[2].o
			if (ret != null) Value(ret) else nullptr
		})
		// TODO loops
		addFunction("type", { ls ->
			ls.forEach { println(it.type.canonicalName) }
			ls[ls.size - 1]
		})
		addFunction("gc", {
			System.gc()
			nullptr
		})
	}

	fun addFunction(name: String, node: (List<Value>) -> Value): Int {
		functionMap.put(name, functionList.size)
		functionList.add(node)
		return functionList.size - 1
	}

	fun addVariable(name: String, value: Value) {
		variableMap[name] = value
	}

	fun getVariable(name: String) = variableMap[name]

	fun getFunctionId(name: String) = functionMap[name]

	fun getFunction(id: Int) = functionList[id]
}
