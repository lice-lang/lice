/**
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 */
package lice.compiler.ast

fun parseNodeRec(
		code: String,
		variableMap: MutableMap<String, Value>,
		functionMap: MutableMap<String, (Value) -> Value>): Node {
	var begin = 0
	val nodes = mutableListOf<Node>()
	val elements = mutableListOf<String>()
	var elementStart = 0
	for (i in 0..code.length - 1) when (code[i]) {
		'(' -> begin = i
		')' -> {
			nodes.add(parseNodeRec(code
					.substring(begin + 1, i)
					.trim(), variableMap, functionMap
			))
		}
		' ' -> elementStart = i
		else -> {
			elements.add(code
					.substring(elementStart + 1, i)
					.trim()
			)
		}
	}
	return when (elements.size) {
		0 -> EmptyNode
		1 -> ValueNode(Value(IntType, elements[0].toInt()))
		else -> {
			val function = functionMap[elements[0]] ?: TODO()
			val last = ValueNode(Value(IntType, elements.last().toInt()))
			val lastClosure = ExpressionNode(
					function,
					ValueNode(Value(IntType, elements.last().toInt()))
			)
			elements.subList(1, elements.size - 2).foldRight(
					ExpressionNode(
							function,
							ValueNode(Value(IntType, elements.last().toInt()))
					),
					{ param, node ->
						ExpressionNode(
								function,
								ValueNode(Value(IntType, elements.last().toInt()))
						)
					}
			)
		}
	}
}

fun createAst(code: String): Ast {
	val variableMap = mutableMapOf<String, Value>()
	val functionMap = mutableMapOf(
			Pair("+", { a: Value ->
				ExpressionNode({ b ->
					Value(IntType, a.value + b.value)
				}, ValueNode(a)).eval()
			}),
			Pair("*", { a: Value ->
				ExpressionNode({ b ->
					Value(IntType, a.value * b.value)
				}, ValueNode(a)).eval()
			}),
			Pair("-", { a: Value ->
				ExpressionNode({ b ->
					Value(IntType, a.value - b.value)
				}, ValueNode(a)).eval()
			})
	)
	val root = parseNodeRec(code, variableMap, functionMap)
	return Ast(root, variableMap, functionMap)
}

