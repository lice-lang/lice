/**
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 */
package lice.compiler.ast

open class Type(val clazz: Class<*>)

object IntType : Type(Int::class.java)

object StringType : Type(String::class.java)

object CharType : Type(Char::class.java)

open class Value(val type: Type, val value: Any)

interface Node {
	fun eval(): Value
}

data class ValueNode(
		val value: Value) : Node {
	constructor(type: Type, value: Any) : this(Value(type, value))

	constructor(value: Any) : this(Value(Type(value.javaClass), value))

	override fun eval() = value
}

data class ExpressionNode(
		val function: (List<Node>) -> Node,
		val params: List<Node>) : Node {

	constructor(function: (List<Node>) -> Node, param: Node) : this(function, listOf(param)) {
	}

	override fun eval() = function(params).eval()
}

object EmptyNode : Node {
	val value = Value(Type(Unit.javaClass), 0)
	override fun eval() = value
}

class Ast(
		val root: Node,
		val variableMap: Map<String, Value>,
		val functionMap: Map<String, (List<Node>) -> Node>
)

fun <A, B, C> ((a: A, b: B) -> C).curry() = { a: A -> { b: B -> invoke(a, b) } }

fun main(args: Array<String>) {
	{ a: List<Any>, b: List<Any> -> a zip b }.curry()
//	{ a: Value ->
//		ExpressionNode({ b ->
//			Value(IntType, a.value + b.value)
//		}, ValueNode(a)).eval()
//	}
}
