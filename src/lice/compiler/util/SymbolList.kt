package lice.compiler.util

import lice.compiler.model.Ast
import lice.compiler.model.Value
import lice.compiler.model.Value.Objects.nullptr
import lice.compiler.parse.buildNode
import lice.compiler.parse.createAst
import lice.compiler.parse.mapAst
import lice.compiler.util.InterpretException.Factory.typeMisMatch
import java.io.File

/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 */
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

	fun initialize() {
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
		addFunction("[]", { ls -> Value(ls.map { it.o }) })
		addFunction("file", { ls ->
			val a = ls[0].o
			if (a is String) Value(File(a))
			else typeMisMatch("String", ls[0])
		})
		addFunction("read-file", { ls ->
			val a = ls[0].o
			if (a is File) Value(a.readText())
			else typeMisMatch("File", ls[0])
		})
		addFunction("", { ls ->
			ls.forEach { println("${it.o.toString()} => ${it.type.name}") }
			ls[ls.size - 1]
		})
		addFunction("put", { ls ->
			if (ls.size < 2)
				throw InterpretException("Expected 2 arguments, found: ${ls.size}")
			val str = ls[0].o
			if (str is String) addVariable(str, ls[1])
			else typeMisMatch("String", ls[0])
			ls[1]
		})
		addFunction("get", { ls ->
			if (ls.isEmpty())
				throw InterpretException("Expected 1 arguments, found: ${ls.size}")
			val str = ls[0].o
			if (str is String) {
				val value = variableMap[str]
				value ?: nullptr
			} else typeMisMatch("String", ls[0])
		})
		addFunction("if", { ls ->
			if (ls.size < 2)
				throw InterpretException("Expected 2 arguments, found: ${ls.size}")
			val bool = ls[0].o
			val condition = bool as? Boolean ?: ls[1].o != null
			val ret = if (condition) ls[1].o else ls[2].o
			if (ret != null) Value(ret) else nullptr
		})
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
		addFunction("type", { ls ->
			ls.forEach { println(it.type.canonicalName) }
			ls[ls.size - 1]
		})
		addFunction("gc", {
			System.gc()
			nullptr
		})
		addFunction("eval", { ls ->
			val o = ls[0].o
			if (o is String) {
				val symbolList = SymbolList(true)
				val stringTreeRoot = buildNode(o)
				Value(mapAst(stringTreeRoot, symbolList).eval())
			} else typeMisMatch("String", ls[0])
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
