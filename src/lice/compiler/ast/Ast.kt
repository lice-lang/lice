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

open class Value(val type: Type, val value: Int)

interface Node {
	fun eval(): Value
}

data class ValueNode(
		val value: Value) : Node {
	override fun eval() = value
}

data class ExpressionNode(
		val function: (Value) -> Value,
		val param: Node) : Node {
	override fun eval() = function(param.eval())
}

object EmptyNode : Node {
	val value = Value(Type(Unit.javaClass), 0)
	override fun eval() = value
}

class Ast(
		val root: Node,
		val variableMap: Map<String, Value>,
		val functionMap: Map<String, (Value) -> Value>
)

fun <A, B, C> ((a: A, b: B) -> C).curry() = { a: A -> { b: B -> invoke(a, b) } }

fun main(args: Array<String>) {
	{ a: Value, b: Value -> Value(IntType, a.value + b.value) }.curry()
//	{ a: Value ->
//		ExpressionNode({ b ->
//			Value(IntType, a.value + b.value)
//		}, ValueNode(a)).eval()
//	}
}
