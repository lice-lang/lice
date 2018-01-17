package org.lice.core

import org.lice.model.*
import org.lice.util.cast
import org.lice.util.forceRun
import javax.script.Bindings

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
		for ((_, u) in variables) if (u == value) return true
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

	override val entries: MutableSet<LiceEntry>
		get() = object : AbstractMutableSet<LiceEntry>() {
			override val size: Int get() = variables.size
			override fun add(element: LiceEntry): Boolean {
				defineVariable(element.key, ValueNode(element.value, MetaData.EmptyMetaData))
				return true
			}

			override operator fun iterator(): MutableIterator<LiceEntry> =
					object : MutableIterator<LiceEntry> {
						val it = variables.iterator()
						override fun hasNext(): Boolean = it.hasNext()
						override fun remove() = it.remove()
						override fun next(): LiceEntry =
								it.next().let { e ->
									object : LiceEntry {
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