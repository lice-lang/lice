/**
 * Created by ice1000 on 2017/2/16.
 *
 * @author ice1000
 */
package lice.compiler.model

import lice.compiler.util.verboseOutput

interface StringNode {
	val strRepr: String
	val lineNumber: Int
}

class StringMiddleNode(
		override val lineNumber: Int,
		val list: MutableList<StringNode> = mutableListOf<StringNode>()) : StringNode {
	val empty: Boolean
		get() = list.isEmpty()

	override val strRepr: String
		get() = list.fold(StringBuilder("{")) { stringBuilder, last ->
			stringBuilder
					.append(" [")
					.append(last.strRepr)
					.append("]")
		}.append(" }").toString()

	fun add(n: StringNode) {
		n.strRepr.verboseOutput()
		list.add(n)
	}
}

class StringLeafNode(
		override val lineNumber: Int,
		val str: String) : StringNode {
	override val strRepr = str
}

class EmptyStringNode(override val lineNumber: Int) : StringNode {
	override val strRepr = ""
}
