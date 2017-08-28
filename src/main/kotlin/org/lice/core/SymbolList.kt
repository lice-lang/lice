/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 * @since 1.0.0
 */
package org.lice.core

import org.lice.ast.MetaData
import org.lice.ast.Node
import java.util.*
import javax.script.Bindings
import javax.script.SimpleBindings

@SinceKotlin("1.1")
typealias Func = (MetaData, List<Node>) -> Node


fun bindings(init: Boolean = true): Bindings {
	val b = SimpleBindings()
	return b
}


val Bindings.rand: Random
	get() =
		this["__rand__"]  as? Random ?: this.let {
			val r = Random()
			this["__rand__"] = r
			r
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