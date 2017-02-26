/**
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 */
@file:JvmName("Model")
@file:JvmMultifileClass

package lice.compiler.model

import lice.compiler.model.Value.Objects.Nullptr
import lice.compiler.util.ParseException
import lice.compiler.util.SymbolList

class Value(
		val o: Any?,
		val type: Class<*>) {
	constructor(o: Any) : this(o, o.javaClass)

	companion object Objects {
		val Nullptr = Value(null, Any::class.java)
	}
}

interface Node {
	fun eval(): Value

	companion object Objects {
		val EmptyNode = ValueNode(Nullptr)
	}
}

class ValueNode(val value: Value) : Node {
	constructor(any: Any) : this(Value(any))
	constructor(any: Any?, type: Class<*>) : this(Value(any, type))

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
			symbolList.getFunction(function)(params).eval()
}

object EmptyNode : Node {
	override fun eval() = Nullptr
}

class Ast(
		val root: Node,
		val symbolList: SymbolList
)
