/**
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 */
package lice.compiler.ast

import java.io.File
import java.util.*

fun parseNodeRec(
		code: String,
		variableMap: MutableMap<String, Value>,
		functionMap: MutableMap<String, (List<Node>) -> Node>): Node {
	println(code)
	val stack = Stack<Int>()
	var elementStarted = false
	val nodes = ArrayList<Node>()
	val elements = ArrayList<String>()
	var elementStart = 0
	val check = { i: Int ->
		if (elementStarted) {
			elements.add(code
					.substring(elementStart, i)
					.apply { println(this) }
			)
			elementStarted = false
		}
	}
	loop@ for (i in 0..code.length - 1) {
//		println(code[i])
		when (code[i]) {
			'(' -> {
				if (stack.size == 2) check(i)
				stack.push(i)
			}
			')' -> {
				val begin = stack.peek() + 1
				stack.pop()
				if (stack.size == 2) {
					check(i)
					nodes.add(parseNodeRec(code
							.substring(begin, i)
							.trim(), variableMap, functionMap
					))
				}
			}
			' ' -> if (stack.size == 2) check(i)
			else -> {
				if (stack.size <= 2 && !elementStarted) {
					elementStarted = true
					elementStart = i
				}
			}
		}
	}
	check(code.length - 1)
	if (!stack.empty()) {
		println("braces not match: ${stack.peek()}")
	}
	return when (elements.size) {
		0 -> EmptyNode
		1 -> ValueNode(Value(IntType, elements[0].toInt()))
		else -> ExpressionNode(
				functionMap[elements[0]] ?:
						throw RuntimeException("undefined method: ${elements[0]}"),
				elements
						.subList(1, elements.size - 1)
						.map(::ValueNode)
		)
	}
}

fun createAst(file: File): Ast {
	val code = file.readText()
	val variableMap = mutableMapOf(
			Pair("FILE_PATH", Value(StringType, file.absolutePath))
	)
	val plus: (List<Node>) -> Node = { list: List<Node> ->
		ValueNode(list.fold(0, { last, node ->
			last + (node.eval().value as String).toInt()
		}))
	}
	val product: (List<Node>) -> Node = { list: List<Node> ->
		ValueNode(list.fold(1, { last, node ->
			last * (node.eval().value as String).toInt()
		}))
	}
	val minus: (List<Node>) -> Node = { list: List<Node> ->
		ValueNode(list.fold(list[0].eval().value as Int * 2, { last, node ->
			last - (node.eval().value as String).toInt()
		}))
	}
	val functionMap = mutableMapOf(
			Pair("+", plus),
			Pair("plus", plus),
			Pair("*", product),
			Pair("product", product),
			Pair("-", minus),
			Pair("minus", minus)
	)
	val root = parseNodeRec(code, variableMap, functionMap)
	return Ast(root, variableMap, functionMap)
}

