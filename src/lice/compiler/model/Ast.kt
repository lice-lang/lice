/**
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 */
package lice.compiler.model

import lice.compiler.util.SymbolList

class Value(val o: Any?, val type: Class<*>) {
	constructor(o: Any) : this(o, o.javaClass)

	companion object Factory
}

interface Node {
	fun eval(): Value
}

class ValueNode(val value: Value) : Node {
	override fun eval() = value
}

class ExpressionNode(
		val function: (List<Value>) -> Value,
		val params: List<Node>) : Node {
	constructor(function: (List<Value>) -> Value, param: Node) : this(function, listOf(param))

	override fun eval() = function(params.map(Node::eval))
}

object EmptyNode : Node {
	val nullptr = Value(null, Any::class.java)

	override fun eval() = nullptr
}

class Ast(val root: Node, val symbolList: SymbolList)
