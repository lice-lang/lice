/**
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 */
package lice.compiler.parse

import lice.compiler.model.*
import lice.compiler.util.*
import java.io.File
import java.util.*

fun buildNode(code: String): StringNode {
	var beginIndex = 0
	val currentNodeStack = Stack<StringMiddleNode>()
	currentNodeStack.push(StringMiddleNode(1))
	var elementStarted = true
	var lineNumber = 1
	var lastQuoteIndex = 0
	var quoteStarted = false
	var commentStarted = false
	var lastElement: Char = '\n'
	fun check(index: Int) {
		if (elementStarted) {
			elementStarted = false
			currentNodeStack
					.peek()
					.add(StringLeafNode(lineNumber, code
							.substring(startIndex = beginIndex, endIndex = index)
							.debugApply { println("found token: $this") }
					))
		}
	}
	code.forEachIndexed { index, c ->
		if (c == '\n') commentStarted = false
		if (!commentStarted or (lastElement == '\\')) when (c) {
			';' -> {
				if (!quoteStarted) {
					commentStarted = true
				}
			}
			'(' -> {
				if (!quoteStarted) {
					check(index)
					currentNodeStack.push(StringMiddleNode(lineNumber))
					++beginIndex
				}
			}
			')' -> {
				if (!quoteStarted) {
					check(index)
					if (currentNodeStack.size <= 1) {
						showError("Braces not match at line $lineNumber: Unexpected \')\'.")
						return EmptyStringNode(lineNumber)
					}
					val son =
							if (currentNodeStack.peek().empty) EmptyStringNode(lineNumber)
							else currentNodeStack.peek()
					currentNodeStack.pop()
					currentNodeStack
							.peek()
							.add(son)
				}
			}
			' ', '\n', '\t', '\r' -> {
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
					currentNodeStack.peek().add(StringLeafNode(lineNumber, code
							.substring(startIndex = lastQuoteIndex, endIndex = index + 1)
							.verboseApply { println("Found String: $this") }
					))
				}
			}
			else -> {
				if (!quoteStarted) elementStarted = true
			}
		}
		lastElement = c
	}
	check(code.length - 1)
	if (currentNodeStack.size > 1) {
		showError("Braces not match at line $lineNumber: Expected \')\'.")
	}
	return currentNodeStack.peek()
}

/**
 * This is the core implementation of mapAst
 *
 * @param symbolList the symbol list
 * @param str the string to parse
 * @return parsed node
 */
fun parseValue(str: String, symbolList: SymbolList): Node {
//	str.debugApply { println("str = $str, ${str.isInt()}") }
	if (str.isEmpty() || str.isBlank())
		return EmptyNode
	if ((str[0] == '\"') and (str[str.length - 1] == '\"'))
		return ValueNode(Value(str
				.substring(1, str.length - 2)
				.apply {
					// TODO replace \n, \t, etc.
				}))
	if (str.isInt())
		return ValueNode(str.toInt())
	if (str.isHexInt())
		return ValueNode(str.toHexInt())
	// TODO() is bin
	// TODO() is float
	// TODO() is double
//	str.debugOutput()
	try {
		return VariableNode(
				symbolList,
				str
		)
	} catch (e: Exception) {
//		e.debugApply { printStackTrace() }
//		str.debugApply { println("str = $str") }
//		e.message.debugOutput()
		println("error token: $str")
		return EmptyNode // do nothing
	}
}

/**
 * map the string tree, making it a real ast
 *
 * @param symbolList the symbol list
 * @param node the node to parse
 * @return the parsed node
 */
fun mapAst(
		node: StringNode,
		symbolList: SymbolList = SymbolList()): Node {
	return when (node) {
		is StringMiddleNode -> {
			val ls: List<Node> = node
					.list
					.subList(1, node.list.size)
					.map { strNode ->
						mapAst(
								node = strNode,
								symbolList = symbolList
						)
					}
//			ls.size.verboseOutput()
//			node
//					.list[0]
//					.strRepr
//					.verboseOutput()
			ExpressionNode(
					symbolList,
					symbolList.getFunctionId(node.list[0].strRepr)
							?: throw ParseException("Undefined Function: ${node.list[0].strRepr}"),
					ls.subList(1, ls.size)
			)
		}
		is StringLeafNode ->
			parseValue(
					str = node.str,
					symbolList = symbolList
			)
//					.verboseApply {
//						println("value: [${eval().o}], type: [${eval().type.simpleName}] parsed")
//					}
		else -> // empty
			EmptyNode
	}
}

fun createAst(file: File): Ast {
	val code = file.readText()
	val symbolList = SymbolList(true)
//	symbolList.initialize()
	symbolList.addVariable("FILE_PATH", Value(file.absolutePath))
	val stringTreeRoot = buildNode(code)
//	(stringTreeRoot as StringMiddleNode).list.debugApply {
//		forEach { println(it.strRepr) }
//	}
	return Ast(
			mapAst(
					node = stringTreeRoot,
					symbolList = symbolList
			),
			symbolList
	)
}
