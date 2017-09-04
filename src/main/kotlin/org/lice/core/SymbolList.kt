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
import org.lice.compiler.util.forceRun
import java.util.*
import javax.script.Bindings

@SinceKotlin("1.1")
typealias Func = (MetaData, List<Node>) -> Node

@SinceKotlin("1.1")
typealias ProvidedFuncWithMeta = (MetaData, List<Any?>) -> Any?

@SinceKotlin("1.1")
typealias ProvidedFunc = (List<Any?>) -> Any?

@JvmOverloads
operator fun Func.invoke(e: MetaData = MetaData.EmptyMetaData) = invoke(e, emptyList())

abstract class AbstractBindings : Bindings {
	abstract val functions: MutableMap<String, Func>

	abstract fun provideFunctionWithMeta(name: String, node: ProvidedFuncWithMeta): Func?

	abstract fun provideFunction(name: String, node: ProvidedFunc): Func?

	abstract fun defineFunction(name: String, node: Func): Func?

	abstract fun isFunctionDefined(name: String?): Boolean

	abstract fun removeFunction(name: String?): Func?

	abstract fun getFunction(name: String?): Func?

	override fun containsValue(value: Any?): Boolean {
		for ((_, u) in functions) forceRun {
			if (u() == value)
				return true
		}

		return false
	}

	override fun clear() {
		functions.clear()
	}

	override fun putAll(from: Map<out String, Any>) {
		from.forEach { k, v -> provideFunction(k) { v } }
	}

	override fun containsKey(key: String?): Boolean {
		return functions.containsKey(key)
	}

	override fun get(key: String?): Any? =
			functions[key]?.invoke()


	override fun put(key: String?, value: Any?): Any? {
		if (key != null && value != null) provideFunction(key) { value }

		return value
	}

	override fun isEmpty(): Boolean =
			functions.isEmpty()


	override fun remove(key: String?): Any? = functions.remove(key)

	override val size: Int
		get() = functions.size

	override val entries: MutableSet<MutableMap.MutableEntry<String, Any>>
		get() = object : AbstractMutableSet<MutableMap.MutableEntry<String, Any>>() {
			override val size: Int
				get() = functions.size

			override fun add(element: MutableMap.MutableEntry<String, Any>): Boolean {
				provideFunction(element.key) {
					element.value
				}

				return true
			}

			override operator fun iterator(): MutableIterator<MutableMap.MutableEntry<String, Any>> =
					object : MutableIterator<MutableMap.MutableEntry<String, Any>> {
						val it = functions.iterator()

						override fun hasNext(): Boolean =
								it.hasNext()

						override fun next(): MutableMap.MutableEntry<String, Any> =
								it.next().let { e ->
									object : MutableMap.MutableEntry<String, Any> {
										override val value: Any
											get() = e.value()

										override fun setValue(newValue: Any): Any {
											return e.setValue { meta, _ ->
												ValueNode(e.value, meta)
											}
										}

										override val key: String
											get() = e.key

									}
								}

						override fun remove() {
							it.remove()
						}

					}

		}

	override val keys: MutableSet<String>
		get() = functions.keys

	override val values: MutableCollection<Any>
		get() = mutableListOf<Any>().apply {
			functions.values.forEach {
				forceRun { add(it()) }
			}
		}
}

class SymbolList
@JvmOverloads
constructor(init: Boolean = true) : AbstractBindings() {
	companion object ClassPathHolder {
		val pathSeperator: String = System.getProperty("path.separator")
		val classPath: String = System.getProperty("java.class.path")

		private val initMethods: MutableSet<(SymbolList) -> Unit> = mutableSetOf()

		fun addInitMethod(f: SymbolList.() -> Unit): Unit {
			initMethods.add(f)
		}
	}

	override val functions: MutableMap<String, Func> = mutableMapOf()

	val rand = Random(System.currentTimeMillis())
//	val loadedModules = mutableListOf<String>()

	init {
		if (init) {
			initialize()
//			loadLibrary()
		}
	}

	private fun initialize() {
		initMethods.forEach { it(this) }
		addFileFunctions()
		addGUIFunctions()
		addMathFunctions()
		addConcurrentFunctions()
		addStandard()
		provideFunction("load", { param ->
			(param.firstOrNull() as? String)?.let { loadLibrary(it) }
			true
		})

	}

	@JvmOverloads
	fun loadLibrary(cp: String = """
(def println str (print str "\n"))

(def println-err str (print-err str "\n"))

(def ! a (if a false true))
(def null? a (=== null a))
(def !null a (! (null? a)))

(def empty? a (> (size a) 0))
(def !empty? a (! (empty? a)))

(def in? ls a (> (count ls a) 0))
(def !in? ls a (! (in? ls a)))
(alias in? contains?)
(alias !in? !contains?)

(deflazy unless condition a b (if condition b a))

(deflazy until condition a (while (! condition) a))

"""): Boolean {
//		if (cp in loadedModules) return false
//		loadedModules.add(cp)
		Lice.run(cp, this@SymbolList)
		return true
	}

	override fun provideFunctionWithMeta(name: String, node: ProvidedFuncWithMeta): Func? =
			defineFunction(name) { meta, ls ->
				val value = node(meta, ls.map { it.eval().o })
				if (value != null) ValueNode(value, meta)
				else EmptyNode(meta)
			}

	override fun provideFunction(name: String, node: ProvidedFunc): Func? =
			defineFunction(name) { meta, ls ->
				val value = node(ls.map { it.eval().o })
				if (value != null) ValueNode(value, meta)
				else EmptyNode(meta)
			}

	override fun defineFunction(name: String, node: Func): Func? =
			functions.put(name, node)

	override fun isFunctionDefined(name: String?): Boolean =
			functions.containsKey(name)

	override fun removeFunction(name: String?): Func? =
			functions.remove(name)

	override fun getFunction(name: String?): Func? =
			functions[name]
}
