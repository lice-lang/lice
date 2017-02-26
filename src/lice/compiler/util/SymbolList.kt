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
	val function = mutableMapOf<String, (List<Node>) -> Node>()
	val variables = mutableMapOf<String, Node>()

	val rand = Random(System.currentTimeMillis())
	val loadedModules = mutableListOf<String>()

	init {
		if (init) initialize()
	}

	fun initialize() {
		setFunction("import", { ls ->
			ls.forEach { node ->
				val res = node.eval()
				if (res.o is String) {
					if (res.o in loadedModules) return@setFunction ValueNode(false)
					loadedModules.add(res.o)
					when (res.o) {
						"lice.io" -> addFileFunctions()
						"lice.gui" -> addGUIFunctions()
						"lice.math" -> addMathFunctions()
						"lice.str" -> addStringFunctions()
						"lice.thread" -> addConcurrentFunctions()
						else -> {
							serr("${res.o} not found!")
							return@setFunction EmptyNode
						}
					}
				}
			}
			ValueNode(true)
		})
		addStandard()
	}

	fun setFunction(name: String, node: (List<Node>) -> Node) {
		function.put(name, node)
	}

	fun setVariable(name: String, value: Node) {
		variables[name] = value
	}

	fun getVariable(name: String) =
			variables[name]

	fun getFunction(name: String) =
			function[name]
					?: throw ParseException("function not found: $function")
}
