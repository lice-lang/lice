/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 * @since 1.0.0
 */
package org.lice.core

import org.lice.lang.Echoer
import org.lice.model.*
import org.lice.util.*
import java.util.function.Consumer
import javax.script.Bindings

typealias Entry = MutableMap.MutableEntry<String, Any?>

abstract class AbstractBindings : Bindings {
	abstract val variables: MutableMap<String, Any?>

	abstract fun provideFunctionWithMeta(name: String, node: ProvidedFuncWithMeta): Any?
	abstract fun provideFunction(name: String, node: ProvidedFunc): Any?
	abstract fun defineFunction(name: String, node: Func): Any?
	abstract fun defineVariable(name: String, value: Node): Any?
	abstract fun isVariableDefined(name: String): Boolean
	abstract fun removeVariable(name: String): Any?
	abstract fun getVariable(name: String): Any?

	override fun containsValue(value: Any?): Boolean {
		for ((_, u) in variables) forceRun {
			if (u == value)
				return true
		}

		return false
	}

	override fun clear() = variables.clear()
	override fun putAll(from: Map<out String, Any>) = from.forEach { k, v -> provideFunction(k) { v } }
	override fun containsKey(key: String) = variables.containsKey(key)
	override fun get(key: String): Any? = cast<Node>(variables[key]).eval()
	override fun put(key: String, value: Any?): Any? {
		if (value != null) provideFunction(key) { value }
		return value
	}

	override fun isEmpty() = variables.isEmpty()
	override fun remove(key: String?) = variables.remove(key)
	override val size: Int get() = variables.size

	override val entries: MutableSet<Entry>
		get() = object : AbstractMutableSet<Entry>() {
			override val size: Int get() = variables.size
			override fun add(element: Entry): Boolean {
				defineVariable(element.key, ValueNode(element.value, MetaData.EmptyMetaData))
				return true
			}

			override operator fun iterator(): MutableIterator<Entry> =
					object : MutableIterator<Entry> {
						val it = variables.iterator()
						override fun hasNext(): Boolean = it.hasNext()
						override fun remove() = it.remove()
						override fun next(): Entry =
								it.next().let { e ->
									object : Entry {
										override val value: Any? get() = e.value
										override val key: String get() = e.key
										override fun setValue(newValue: Any?) =
												e.setValue(ValueNode(e.value, MetaData.EmptyMetaData))
									}
								}
					}

		}

	override val keys: MutableSet<String> get() = variables.keys
	override val values: MutableCollection<Any?>
		get() = mutableListOf<Any?>().apply { variables.values.forEach { forceRun { add(it) } } }
}


class SymbolList
@JvmOverloads
constructor(init: Boolean = true) : AbstractBindings() {
	override val variables: MutableMap<String, Any?> = hashMapOf()

	init {
		defineFunction("") { _, ls ->
			var ret: Any? = null
			ls.forEach {
				val res = it.eval()
				ret = res
				Echoer.replEcholn("${res.toString()} => ${res.className()}")
			}
			ValueNode(ret)
		}
		if (init) initialize()
	}

	private fun initialize() {
		addDefines()
		addLiterals()
		val withMetaHolders = FunctionWithMetaHolders(this)
		withMetaHolders.javaClass.declaredMethods.forEach { method ->
			provideFunctionWithMeta(method.name) { meta, list -> method.invoke(withMetaHolders, meta, list) }
		}
		val holders = FunctionHolders(this)
		holders.javaClass.declaredMethods.forEach { method ->
			provideFunction(method.name) { list -> method.invoke(holders, list) }
		}
		val definedMangledHolder = FunctionDefinedMangledHolder(this)
		definedMangledHolder.javaClass.declaredMethods.forEach { method ->
			defineFunction(method.name.replace('$', '>')) { meta, list ->
				cast(method.invoke(definedMangledHolder, meta, list))
			}
		}
		val mangledHolder = FunctionMangledHolder(this)
		mangledHolder.javaClass.declaredMethods.forEach { method ->
			provideFunctionWithMeta(method.name
					.replace('$', '>')
					.replace('&', '<')
					.replace('{', '[')
					.replace('}', ']')
					.replace('_', '/')) { meta, list -> method.invoke(mangledHolder, meta, list) }
		}
	}

	override fun provideFunctionWithMeta(name: String, node: ProvidedFuncWithMeta) =
			defineFunction(name) { meta, ls ->
				val value = node(meta, ls.map { it.eval() })
				if (value != null) ValueNode(value, meta)
				else EmptyNode(meta)
			}

	override fun provideFunction(name: String, node: ProvidedFunc) =
			defineFunction(name) { meta, ls ->
				val value = node(ls.map { it.eval() })
				if (value != null) ValueNode(value, meta)
				else EmptyNode(meta)
			}

	override fun defineVariable(name: String, value: Node) = variables.put(name, value)
	override fun defineFunction(name: String, node: Func) = variables.put(name, node)
	override fun isVariableDefined(name: String): Boolean = variables.containsKey(name)
	override fun removeVariable(name: String) = variables.remove(name)
	override fun getVariable(name: String) = variables[name]

	fun addLiterals() {
		defineVariable("true", ValueNode(true))
		defineVariable("false", ValueNode(false))
		defineVariable("null", ValueNode(null))
	}

	companion object {
		@JvmStatic
		fun with(init: Consumer<SymbolList>) = SymbolList().also { init.accept(it) }

		@JvmStatic
		fun with(init: SymbolList.() -> Unit) = SymbolList().also { init(it) }

		val pathSeperator: String = System.getProperty("path.separator")
		val classPath: String = System.getProperty("java.class.path")

		private val initMethods: MutableSet<(SymbolList) -> Unit> = mutableSetOf()

		fun addInitMethod(f: SymbolList.() -> Unit): Unit {
			initMethods.add(f)
		}
	}
}
