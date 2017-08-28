/**
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 * @since 1.0.0
 */
@file:JvmName("Model")
@file:JvmMultifileClass

package org.lice.ast

import javax.script.Bindings


data class MetaData(
		val beginLine: Int = -1,
		val beginColumn: Int = -1,
		val endLine: Int = -1,
		val endColumn: Int = -1)

abstract class Token(val image: String, val meta: MetaData? = null)


sealed class Node {
	var env: Bindings? = null

	abstract val meta: MetaData?

	abstract fun eval(bindings: Bindings? = null): Any?
}

data class ValueNode(val value: Any?, override val meta: MetaData? = null) : Node() {
	override fun eval(bindings: Bindings?): Any? =
			value
}

data class ApplyNode(val nodes: List<Node>, override val meta: MetaData? = null) : Node {
	override fun eval(bindings: Bindings?): Any? {
		TODO("Function eval is not implemented")
	}
}