/**
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 */
package lice.compiler.ast

import java.io.File

fun parseNodeRec(
		code: String,
		variableMap: MutableMap<String, Value>,
		functionMap: MutableMap<String, (List<Node>) -> Node>): Node {
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
		else -> ExpressionNode(
				functionMap[elements[0]] ?: throw RuntimeException("undefined method: ${elements[0]}"),
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
			last + node.eval().value as Int
		}))
	}
	val product: (List<Node>) -> Node = { list: List<Node> ->
		ValueNode(list.fold(1, { last, node ->
			last * node.eval().value as Int
		}))
	}
	val minus: (List<Node>) -> Node = { list: List<Node> ->
		ValueNode(list.fold(list[0].eval().value as Int * 2, { last, node ->
			last - node.eval().value as Int
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

