/**
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 */
@file:JvmName("Model")
@file:JvmMultifileClass

package lice.compiler.model

import lice.compiler.model.Value.Objects.Nullptr
import lice.compiler.util.SymbolList

class Value(
		val o: Any?,
		val type: Class<*>) {
	constructor(
			o: Any
	) : this(o, o.javaClass)

	companion object Objects {
		val Nullptr =
				Value(null, Any::class.java)
	}
}

interface Node {
	fun eval(): Value
	val lineNumber: Int

	companion object Objects {
		fun getNullNode(lineNumber: Int) =
				ValueNode(Nullptr, lineNumber)
	}
}

class ValueNode(
		val value: Value,
		override val lineNumber: Int) : Node {

	constructor(
			any: Any,
			lineNumber: Int
	) : this(
			Value(any),
			lineNumber
	)

	constructor(
			any: Any?,
			type: Class<*>,
			lineNumber: Int
	) : this(
			Value(any, type),
			lineNumber
	)

	override fun eval() =
			value
}

//class JvmReflectionNode(
//		val methodName: String,
//		val receiver: Node,
//		val params: List<Node>) : Node {
//	override fun eval() = Value(receiver.eval().type.getMethod(
//			methodName,
//			*params
//					.map { it.eval().type }
//					.toTypedArray()
//	).invoke(
//			receiver,
//			*params
//					.map { it.eval().o }
//					.toTypedArray()
//	))
//}

class ExpressionNode(
		val symbolList: SymbolList,
		val function: String,
		override val lineNumber: Int,
		val params: List<Node>) : Node {

	constructor(
			symbolList: SymbolList,
			function: String,
			lineNumber: Int,
			vararg params: Node
	) : this(
			symbolList,
			function,
			lineNumber,
			params.toList()
	)

	override fun eval() =
			symbolList.getFunction(function)(lineNumber, params).eval()
}

class EmptyNode(override val lineNumber: Int) : Node {
	override fun eval() = Nullptr
}

class Ast(
		val root: Node
)
