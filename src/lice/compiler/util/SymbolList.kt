package lice.compiler.util

import lice.compiler.model.Value
import lice.compiler.model.Value.Objects.nullptr

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
	val typeMap: MutableMap<String, Int>
	val typeList: MutableList<Class<*>>

	init {
		functionMap = mutableMapOf()
		functionList = mutableListOf()
		variableMap = mutableMapOf()
		variableList = mutableListOf()
		typeMap = mutableMapOf()
		typeList = mutableListOf()
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
			nullptr
		})
		addType("Int", Int::class.java)
		addType("Double", Double::class.java)
		addType("String", String::class.java)
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

	fun addType(name: String, clazz: Class<*>) {
		typeMap.put(name, typeList.size)
		typeList.add(clazz)
	}

	fun getVariableId(name: String) = variableMap[name]

	fun getTypeId(name: String) = typeMap[name]

	fun getFunctionId(name: String) = functionMap[name]

	fun getVariable(id: Int) = variableList[id]

	fun setVariable(id: Int, newValue: Value) {
		variableList[id] = newValue
	}

	fun getFunction(id: Int) = functionList[id]
}
