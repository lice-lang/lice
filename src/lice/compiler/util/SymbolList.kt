/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 */
package lice.compiler.util

import lice.compiler.model.Node
import lice.compiler.model.Node.Objects.EmptyNode
import lice.compiler.model.ValueNode
import lice.core.*
import java.util.*

@Suppress("NOTHING_TO_INLINE")

class SymbolList(init: Boolean = true) {
	val functionMap = mutableMapOf<String, Int>()
	val functionList = mutableListOf<(List<Node>) -> Node>()

	val variables = mutableMapOf<String, Node>()
	val rand = Random(System.currentTimeMillis())
	val loadedModules = mutableListOf<String>()

	init {
		if (init) initialize()
	}

	fun initialize() {
		addFunction("import", { ls ->
			ls.forEach { node ->
				val res = node.eval()
				if (res.o is String) {
					if (res.o in loadedModules) return@addFunction ValueNode(false)
					loadedModules.add(res.o)
					when (res.o) {
						"lice.io" -> addFileFunctions()
						"lice.gui" -> addGUIFunctions()
						"lice.math" -> addMathFunctions()
						"lice.str" -> addStringFunctions()
						"lice.thread" -> addConcurrentFunctions()
						else -> {
							serr("${res.o} not found!")
							return@addFunction EmptyNode
						}
					}
				}
			}
			ValueNode(true)
		})
		addStandard()
	}

	fun addFunction(name: String, node: (List<Node>) -> Node): Int {
		functionMap.put(name, functionList.size)
		functionList.add(node)
		return functionList.size - 1
	}

	fun setVariable(name: String, value: Node) {
		variables[name] = value
	}

	fun getVariable(name: String) = variables[name]

	fun getFunctionId(name: String) = functionMap[name]

	fun getFunction(id: Int) = functionList[id]
}
