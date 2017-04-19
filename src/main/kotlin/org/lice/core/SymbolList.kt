/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 * @since 1.0.0
 */
package org.lice.core

import org.lice.Lice
import org.lice.compiler.model.EmptyNode
import org.lice.compiler.model.MetaData
import org.lice.compiler.model.Node
import org.lice.compiler.model.ValueNode
import java.io.File
import java.util.*

@SinceKotlin("1.1")
typealias Func = (MetaData, List<Node>) -> Node

@SinceKotlin("1.1")
typealias ProvidedFuncWithMeta = (MetaData, List<Any?>) -> Any?

@SinceKotlin("1.1")
typealias ProvidedFunc = (List<Any?>) -> Any?

operator fun Func.invoke(e: MetaData) = invoke(e, emptyList())

class SymbolList
@JvmOverloads
constructor(init: Boolean = true) {
	val functions = mutableMapOf<String, Func>()

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
		provideFunction("load", {
			it.forEach { node ->
				if (node is String) {
					if (node in loadedModules) return@provideFunction false
					loadedModules.add(node)
					val file = File(node)
					when {
						file.exists() -> Lice.run(file, this)
						else -> return@provideFunction null
					}
				}
			}
			true
		})
	}

	fun provideFunctionWithMeta(name: String, node: ProvidedFuncWithMeta) =
			defineFunction(name, { meta, ls ->
				val value = node(meta, ls.map { it.eval().o })
				if (value != null) ValueNode(value, meta)
				else EmptyNode(meta)
			})

	fun provideFunction(name: String, node: ProvidedFunc) =
			defineFunction(name, { meta, ls ->
				val value = node(ls.map { it.eval().o })
				if (value != null) ValueNode(value, meta)
				else EmptyNode(meta)
			})

	fun defineFunction(name: String, node: Func) =
			functions.put(name, node)

	fun isFunctionDefined(name: String?) =
			functions.containsKey(name)

	fun removeFunction(name: String?) =
			functions.remove(name)

	fun getFunction(name: String?) =
			functions[name]
}
