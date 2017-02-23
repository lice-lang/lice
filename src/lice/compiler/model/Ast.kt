/**
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 */
@file:JvmName("Model")
@file:JvmMultifileClass

package lice.compiler.model

import lice.compiler.util.*

class Value(
		val o: Any?,
		val type: Class<*>) {
	constructor(o: Any) : this(o, o.javaClass)

	companion object Objects {
		val nullptr = Value(null, Any::class.java)
	}
}

interface Node {
	fun eval(): Value
}

class ValueNode(val value: Value) : Node {
	constructor(any: Any) : this(Value(any))

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
		val function: Int,
		val params: List<Node>) : Node {

	constructor(
			symbolList: SymbolList,
			function: Int,
			vararg params: Node
	) : this(
			symbolList,
			function,
			params.toList()
	)

	constructor(
			symbolList: SymbolList,
			function: String,
			params: List<Node>
	) : this(
			symbolList,
			symbolList.getFunctionId(function)
					?: throw ParseException("function not found: $function"),
			params
	)

	override fun eval() =
			symbolList.getFunction(function)(params.map(Node::eval))
}

object EmptyNode : Node {
	override fun eval() = Value.nullptr
}

class Ast(
		val root: Node,
		val symbolList: SymbolList
)
