/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 * @since 1.0.0
 */
package org.lice.core

import org.lice.Lice
import org.lice.compiler.model.MetaData
import org.lice.compiler.model.Node
import org.lice.compiler.model.Node.Objects.getNullNode
import org.lice.compiler.model.ValueNode
import org.lice.compiler.util.serr
import java.io.File
import java.util.*

@SinceKotlin("1.1")
typealias Func = (MetaData, List<Node>) -> Node

@SinceKotlin("1.1")
typealias ProvidedFunc = (MetaData, List<Any?>) -> Any

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

	fun provideFunction(name: String, node: ProvidedFunc) =
			defineFunction(name, { meta, ls ->
				ValueNode(node(meta, ls.map { it.eval().o }), meta)
			})

	internal fun defineFunction(name: String, node: Func) =
			functions.put(name, node)

	fun isFunctionDefined(name: String?) =
			functions.containsKey(name)

	fun removeFunction(name: String?) =
			functions.remove(name)

	fun getFunction(name: String?) =
			functions[name]
}
