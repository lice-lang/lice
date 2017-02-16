/**
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 */
package lice.compiler.model

interface Node {
	fun eval(): Int
}

class ValueNode(val int: Int) : Node {
	override fun eval() = int
}

class ExpressionNode(
		val function: (List<Int>) -> Int,
		val params: List<Node>) : Node {
	constructor(function: (List<Int>) -> Int, param: Node) : this(function, listOf(param))

	override fun eval() = function(params.map(Node::eval))
}

object EmptyNode : Node {
	override fun eval() = 0
}

class Ast(val root: Node)

fun <A, B, C> ((a: A, b: B) -> C).curry() = { a: A -> { b: B -> invoke(a, b) } }
