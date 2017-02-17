/**
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 */
package lice.compiler.parse

import lice.compiler.model.*
import lice.compiler.util.SymbolList
import lice.compiler.util.ParseException
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
		when (c) {
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
				if (c == '\n') ++lineNumber
			}
			"\"" -> {
				if (!quoteStarted) {
					quoteStarted = true
					lastQuoteIndex = index
				} else {
					quoteStarted = false
					currentNodeStack.peek().add(StringLeafNode(code
							.substring(startIndex = quoteStarted, endIndex = index + 1)
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

fun parseValue(str: String, symbolList: SymbolList): Value {
	return when {
		str[0] == '\"' && str[str.length - 1] == '\"' -> Value(str.substring(1, str.length - 2).apply {
				// TODO replace \n, \t, etc.
		})
		// TODO is int
		// TODO is hex
		// TODO is bin
		// TODO is float
		// TODO is double
		else -> VariableNode(
				symbolList,
				symbolList.getVariable(name) ?: throw ParseException("Undefinef Variable: $name")
		)
	}
}

fun mapAst(node: StringNode): Node {
	return when (node) {
		is StringMiddleNode -> node.list.map(::mapAst)
		is StringLeafNode -> node.str
	}
}

fun createAst(file: File): Ast {
	val code = file.readText()
	val symbolList = SymbolList()
	symbolList.initialize()
	symbolList.addVariable("FILE_PATH", Value(file.absolutePath))
	val stringTreeRoot = buildNode(code)
	return Ast(mapAst(stringTreeRoot), symbolList)
}
