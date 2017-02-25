/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 */
package lice.compiler.util

import lice.compiler.model.Node
import lice.compiler.model.Node.Objects.EmptyNode
import lice.compiler.model.Value.Objects.Nullptr
import lice.compiler.model.ValueNode
import lice.compiler.util.InterpretException.Factory.typeMisMatch
import lice.core.*
import java.util.*

@Suppress("NOTHING_TO_INLINE")

class SymbolList(init: Boolean = true) {
	val functionMap = mutableMapOf<String, Int>()
	val functionList = mutableListOf<(List<Node>) -> Node>()

	val variableMap = mutableMapOf<String, Node>()
	val rand = Random(System.currentTimeMillis())
	val loadedModules = mutableListOf<String>()

	init {
		if (init) initialize()
	}

	fun initialize() {
		addGetSetFunction()
		addControlFlowFunctions()
		addNumberFunctions()
		addStringFunctions()
		addBoolFunctions()
		addCollectionsFunctions()
		addFunction("import", { ls ->
			var ret = ValueNode(true)
			ls.forEach { node ->
				val res = node.eval()
				if (res.o is String && res.o !in loadedModules) {
					loadedModules.add(res.o)
					when (res.o) {
						"lice.io" -> addFileFunctions()
						"lice.gui" -> addGUIFunctions()
						else -> {
							serr("${res.o} not found!")
							ret = EmptyNode
						}
					}
				}
			}
			ret
		})
		addFunction("new", { ls ->
			val a = ls[0].eval()
			when (a.o) {
				is String -> ValueNode(Class.forName(a.o).newInstance())
				else -> typeMisMatch("String", a)
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
	}

	fun addFunction(name: String, node: (List<Node>) -> Node): Int {
		functionMap.put(name, functionList.size)
		functionList.add(node)
		return functionList.size - 1
	}

	fun setVariable(name: String, value: Node) {
		variableMap[name] = value
	}

	fun getVariable(name: String) = variableMap[name]

	fun getFunctionId(name: String) = functionMap[name]

	fun getFunction(id: Int) = functionList[id]
}
