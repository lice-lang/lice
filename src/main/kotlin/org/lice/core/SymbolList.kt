/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 * @since 1.0.0
 */
package org.lice.core

import org.lice.Lice
import org.lice.ast.EmptyNode
import org.lice.ast.MetaData
import org.lice.ast.Node
import org.lice.ast.ValueNode
import java.util.*
import javax.script.Bindings
import javax.script.SimpleBindings

@SinceKotlin("1.1")
typealias Func = (MetaData, List<Node>) -> Node

@SinceKotlin("1.1")
typealias ProvidedFuncWithMeta = (MetaData, List<Any?>) -> Any?

@SinceKotlin("1.1")
typealias ProvidedFunc = (List<Any?>) -> Any?

@JvmOverloads
operator fun Func.invoke(e: MetaData = MetaData.EmptyMetaData) = invoke(e, emptyList())


@Deprecated("use bindings()")
fun SymbolList(init: Boolean = true): Bindings {
	val b = SimpleBindings()
	if (init) {
		b.initialize()
	}
	return b
}

fun bindings(init: Boolean = true): Bindings {
	val b = SimpleBindings()
	if (init) {
		b.initialize()
	}
	return b
}

fun Bindings.initialize() {
	addFileFunctions()
	addGUIFunctions()
	addMathFunctions()
	addStringFunctions()
	addConcurrentFunctions()
	addStandard()
	provideFunction("load") { param ->
		(param.firstOrNull() as? String)?.let { loadLibrary(it) }
		true
	}

}

@JvmOverloads
fun Bindings.loadLibrary(cp: String = """
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
	Lice.run(cp, this)
	return true
}

val Bindings.rand: Random
	get() =
		this["__rand__"]  as? Random ?: this.let {
			val r = Random()
			this["__rand__"] = r
			r
		}


fun Bindings.provideFunctionWithMeta(name: String, node: ProvidedFuncWithMeta): Function? =
		this.defineFunction(name) { meta, ls ->
			val value = node(meta, ls.map { it.eval().o })
			if (value != null) ValueNode(value, meta)
			else EmptyNode(meta)
		}

fun Bindings.provideFunction(name: String, node: ProvidedFunc): Func? =
		defineFunction(name) { meta, ls ->
			val value = node(ls.map { it.eval().o })
			if (value != null) ValueNode(value, meta)
			else EmptyNode(meta)
		}

fun MutableMap<String, Any>.defineFunction(name: String?, node: Func): Function? {
	return if (name != null)
		this.put(name, Function(node)) as? Function
	else null
}

fun Bindings.isFunctionDefined(name: String?): Boolean {
	return this[name] is Function
}

fun Bindings.removeFunction(name: String?): Function? {
	return if (this.containsKey(name) && this[name] is Function)
		this.remove(name) as? Function
	else null
}

fun Bindings.getFunction(name: String?): Function? =
		this[name] as? Function