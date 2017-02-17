package lice.compiler.util

import lice.compiler.model.Value

/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 */
class SymbolList {
	val functionMap: MutableMap<String, Int>
	val functionList: MutableList<(List<Value>) -> Int>
	val variableMap: MutableMap<String, Int>
	val variableList: MutableList<Value>

	init {
		functionMap = mutableMapOf()
		functionList = mutableListOf<(List<Value>) -> Int>()
		variableMap = mutableMapOf()
		variableList = mutableListOf()
	}

	fun initialize() {
		addFunction("+", { ls: List<Value> ->
			ls.fold(0) { sum, value ->
				if (value.o is Int) value.o + sum
				else throw ParseException("type mismatch : expected: Int, found: ${value.type.name}")
			}
		})
		addFunction("*", { ls: List<Value> ->
			ls.fold(1) { sum, value ->
				if (value.o is Int) value.o * sum
				else throw ParseException("type mismatch : expected: Int, found: ${value.type.name}")
			}
		})
	}

	fun addFunction(name: String, node: (List<Value>) -> Int): Int {
		functionMap.put(name, functionList.size)
		functionList.add(node)
		return functionList.size - 1
	}

	fun addVariable(name: String, value: Value) {
		variableMap.put(name, variableList.size)
		variableList.add(value)
	}
}
