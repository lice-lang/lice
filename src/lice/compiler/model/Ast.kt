/**
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 */
package lice.compiler.model

import lice.compiler.util.SymbolList

class Value(
		val o: Any?,
		val type: Class<*>) {
	constructor(o: Any) : this(o, o.javaClass)

//	companion object Factory
}

interface Node {
	fun eval(): Value
}

class ValueNode(val value: Value) : Node {
	constructor(any: Any) : this(Value(any))

	override fun eval() =
			value
}

class VariableNode(
		val symbolList: SymbolList,
		val id: Int) : Node {
	override fun eval() =
			symbolList.getVariable(id)
}

class ExpressionNode(
		val symbolList: SymbolList,
		val function: Int,
		val params: List<Node>) : Node {
	constructor(
			symbolList: SymbolList,
			function: Int,
			param: Node) : this(symbolList, function, listOf(param))

	override fun eval() =
			symbolList.getFunction(function)(params.map(Node::eval))
}

object EmptyNode : Node {
	val nullptr = Value(null, Any::class.java)

	override fun eval() = nullptr
}

class Ast(val root: Node, val symbolList: SymbolList)
