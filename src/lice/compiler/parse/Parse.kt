/**
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 */
package lice.compiler.parse

import lice.compiler.model.*
import lice.compiler.util.ParseException
import lice.compiler.util.SymbolList
import lice.compiler.util.debugApply
import lice.compiler.util.debugOutput
import java.io.File
import java.util.*

fun buildNode(code: String): StringNode {
	var beginIndex = 0
	val currentNodeStack = Stack<StringMiddleNode>()
	currentNodeStack.push(StringMiddleNode())
	var elementStarted = true
	var lineNumber = 0
	var lastQuoteIndex = 0
	var quoteStarted = false
	var commentStarted = false
	fun check(index: Int) {
		if (elementStarted) {
			elementStarted = false
			currentNodeStack
					.peek()
					.add(StringLeafNode(code
							.substring(startIndex = beginIndex, endIndex = index)
							.debugApply { println("found token: $this") }
					))
		}
	}
	code.forEachIndexed { index, c ->
		if (!commentStarted) when (c) {
			';' -> commentStarted = true
			'(' -> {
				if (!quoteStarted) {
					check(index)
					currentNodeStack.push(StringMiddleNode())
					++beginIndex
				}
			}
			')' -> {
				if (!quoteStarted) {
					check(index)
					if (currentNodeStack.size <= 1) {
						println("Braces not match at line $lineNumber: Unexpected \')\'.")
						return EmptyStringNode
					}
					val son =
							if (currentNodeStack.peek().empty) EmptyStringNode
							else currentNodeStack.peek()
					currentNodeStack.pop()
					currentNodeStack.peek().add(son)
				}
			}
			' ', '\n', '\t' -> {
				if (!quoteStarted) {
					check(index)
					beginIndex = index + 1
				}
				if (c == '\n') {
					++lineNumber
					commentStarted = false
				}
			}
			'\"' -> {
				if (!quoteStarted) {
					quoteStarted = true
					lastQuoteIndex = index
				} else {
					quoteStarted = false
					currentNodeStack.peek().add(StringLeafNode(code
							.substring(startIndex = lastQuoteIndex, endIndex = index + 1)
							.debugApply { println("Found String: $this") }
					))
				}
			}
			else -> {
				if (!quoteStarted) elementStarted = true
			}
		}
	}
	check(code.length - 1)
	if (currentNodeStack.size > 1) {
		println("Braces not match at line $lineNumber: Expected \')\'.")
	}
	return currentNodeStack.peek()
}

fun parseValue(str: String, symbolList: SymbolList): Node {
	str.debugApply { println("str = $str, ${str.isInt()}") }
	return if (str.isEmpty() || str.isBlank()) EmptyNode
	else if (str[0] == '\"' && str[str.length - 1] == '\"') ValueNode(Value(str
			.substring(1, str.length - 2)
			.apply {
				// TODO replace \n, \t, etc.
			}))
	else if (str.isInt()) ValueNode(str.toInt())
	// TODO() is hex
	// TODO() is bin
	// TODO() is float
	// TODO() is double
	else VariableNode(
			symbolList,
			str
	)
}

fun mapAst(symbolList: SymbolList, node: StringNode): Node {
	return when (node) {
		is StringMiddleNode -> {
			val ls: List<Node> = node
					.list
					.subList(1, node.list.size)
					.map { strNode ->
						mapAst(symbolList, strNode)
					}
			ls.size.debugOutput()
			ExpressionNode(
					symbolList,
					symbolList.getFunctionId(node.list[0].strRepr)
							?: throw ParseException("Undefined Function: ${node.list[0].strRepr}"),
					ls.subList(1, ls.size)
			)
			// TODO return the mapped node
		}
		is StringLeafNode ->
			parseValue(node.str, symbolList)
		else -> // empty
			EmptyNode
	}
}

fun createAst(file: File): Ast {
	val code = file.readText()
	val symbolList = SymbolList()
	symbolList.initialize()
	symbolList.addVariable("FILE_PATH", Value(file.absolutePath))
	val stringTreeRoot = buildNode(code)
	(stringTreeRoot as StringMiddleNode).list.debugApply {
		forEach { println(it.strRepr) }
	}
	return Ast(
			mapAst(
					symbolList,
					stringTreeRoot
			),
			symbolList
	)
}
