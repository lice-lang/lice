/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 * @since 1.0.0
 */
package org.lice.compiler.util

import org.lice.Lice
import org.lice.compiler.model.MetaData
import org.lice.compiler.model.Node
import org.lice.compiler.model.Node.Objects.getNullNode
import org.lice.compiler.model.ValueNode
import org.lice.core.*
import org.lice.lang.Symbol
import java.io.File
import java.util.*

@SinceKotlin("1.1")
typealias Func = (MetaData, List<Node>) -> Node

class SymbolList
@JvmOverloads
constructor(init: Boolean = true) {
	val functions = mutableMapOf<String, Func>()
//	val variables = mutableMapOf<String, Node>()

	val rand = Random(System.currentTimeMillis())
	val loadedModules = mutableListOf<String>()

	init {
		if (init) initialize()
	}

	fun initialize() {
		addFileFunctions()
		addGUIFunctions()
		addMathFunctions()
		addStringFunctions()
		addConcurrentFunctions()
		addStandard()
		defineFunction("load", { ln, ls ->
			ls.forEach { node ->
				val res = node.eval()
				if (res.o is String) {
					if (res.o in loadedModules)
						return@defineFunction ValueNode(false, ln)
					loadedModules.add(res.o)
					val file = File(res.o)
					when {
						file.exists() -> Lice.run(file, this)
						else -> {
							serr("${res.o} not found!")
							return@defineFunction getNullNode(ln)
						}
					}
				}
			}
			ValueNode(true, ln)
		})
	}

	fun defineFunction(name: Symbol, node: Func) =
			defineFunction(name.name, node)

	fun defineFunction(name: String, node: Func) {
		functions.put(name, node)
	}

	fun isFunctionDefined(name: String) = functions.containsKey(name)

	fun isFunctionDefined(name: Symbol) = isFunctionDefined(name.name)

	fun removeFunction(name: String) {
		functions.remove(name)
	}

	fun removeFunction(name: Symbol) =
			removeFunction(name.name)

//	fun setVariable(name: Symbol, value: Node) =
//			setVariable(name.name, value)

//	fun setVariable(name: String, value: Node) {
//		variables[name] = value
//	}

//	fun removeVariable(name: String) {
//		variables.remove(name)
//	}

//	fun removeVariable(name: Symbol) = removeVariable(name.name)

//	fun getVariable(name: String) =
//			variables[name]

//	fun getVariable(name: Symbol) =
//			getVariable(name.name)

	fun getFunction(name: Symbol) =
			getFunction(name.name)

	fun getFunction(name: String) =
			functions[name]
}
