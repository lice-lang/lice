package lice.compiler.util

import lice.compiler.model.EmptyNode
import lice.compiler.model.Value

/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 */
class SymbolList {
	val functionMap: MutableMap<String, Int>
	val functionList: MutableList<(List<Value>) -> Value>
	val variableMap: MutableMap<String, Int>
	val variableList: MutableList<Value>

	init {
		functionMap = mutableMapOf()
		functionList = mutableListOf()
		variableMap = mutableMapOf()
		variableList = mutableListOf()
	}

	fun initialize() {
		addFunction("+", { ls: List<Value> ->
			Value(ls.fold(0) { sum, value ->
				if (value.o is Int) value.o + sum
				else throw ParseException("type mismatch : expected: Int, found: ${value.type.name}")
			})
		})
		addFunction("*", { ls: List<Value> ->
			Value(ls.fold(1) { sum, value ->
				if (value.o is Int) value.o * sum
				else throw ParseException("type mismatch : expected: Int, found: ${value.type.name}")
			})
		})
		addFunction("", { ls: List<Value> ->
			ls.forEach { println("type: ${it.type.name}, value: ${it.o.toString()}") }
			EmptyNode.nullptr
		})
	}

	fun addFunction(name: String, node: (List<Value>) -> Value): Int {
		functionMap.put(name, functionList.size)
		functionList.add(node)
		return functionList.size - 1
	}

	fun addVariable(name: String, value: Value) {
		variableMap.put(name, variableList.size)
		variableList.add(value)
	}

	fun getVariableId(name: String) = variableMap[name]

	fun getFunctionId(name: String) = functionMap[name]

	fun getVariable(id: Int) = variableList[id]

	fun setVariable(id: Int, newValue: Value) {
		variableList[id] = newValue
	}

	fun getFunction(id: Int) = functionList[id]
}
