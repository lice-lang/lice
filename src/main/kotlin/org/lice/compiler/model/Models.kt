/**
 * Created by ice1000 on 2017/2/16.
 *
 * @author ice1000
 * @since 1.0.0
 */
//@file:JvmName("Model")
//@file:JvmMultifileClass

package org.lice.compiler.model

interface StringNode {
	val strRepr: String
	val meta: MetaData
	override fun toString(): String
}

data class StringMiddleNode(
	override val meta: MetaData,
	val list: MutableList<StringNode> = mutableListOf<StringNode>()) : StringNode {

	val empty: Boolean
		get() = list.isEmpty()

	override val strRepr: String
		get() = list.fold(StringBuilder("{")) { stringBuilder, last ->
			stringBuilder
				.append(" [")
				.append(last.strRepr)
				.append("]")
		}
			.append(" }")
			.toString()

	fun add(n: StringNode) {
		list.add(n)
	}

	override fun toString() = list.first().strRepr
}

data class StringLeafNode(
	override val meta: MetaData,
	val str: String) : StringNode {
	override val strRepr = str
	override fun toString() = strRepr
}

data class EmptyStringNode(
	override val meta: MetaData) : StringNode {
	override val strRepr = ""
	override fun toString() = strRepr
}
