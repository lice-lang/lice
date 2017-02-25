/**
 * Codes that parse the code into a string tree
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 */
@file:JvmName("Parse")
@file:JvmMultifileClass

package lice.compiler.parse

import lice.compiler.model.EmptyStringNode
import lice.compiler.model.StringLeafNode
import lice.compiler.model.StringMiddleNode
import lice.compiler.model.StringNode
import lice.compiler.util.showError
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
	fun check(index: Int) {
		if (elementStarted) {
			elementStarted = false
			currentNodeStack
					.peek()
					.add(StringLeafNode(lineNumber, code
							.substring(startIndex = beginIndex, endIndex = index)
					))
		}
	}
	code.forEachIndexed { index, c ->
		if (c == '\n') commentStarted = false
		if (!commentStarted) when (c) {
			';' -> if (!quoteStarted) commentStarted = true
			'(' -> if (!quoteStarted) {
				check(index)
				currentNodeStack.push(StringMiddleNode(lineNumber))
				++beginIndex
			}
			')' -> if (!quoteStarted) {
				check(index)
				if (currentNodeStack.size <= 1) {
					showError("Braces not match at line $lineNumber: Unexpected \')\'.", true)
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
			' ', '\n', '\t', '\r', ',' -> {
				if (!quoteStarted) {
					check(index)
					beginIndex = index + 1
				}
				if (c == '\n') {
					++lineNumber
					commentStarted = false
				}
			}
			'\"' -> if (!quoteStarted) {
				quoteStarted = true
				lastQuoteIndex = index
			} else {
				quoteStarted = false
				currentNodeStack.peek().add(StringLeafNode(lineNumber, code
						.substring(startIndex = lastQuoteIndex, endIndex = index + 1)
				))
			}
			else -> if (!quoteStarted) elementStarted = true
		}
	}
	check(code.length - 1)
	if (currentNodeStack.size > 1) {
		showError("Braces not match at line $lineNumber: Expected \')\'.", true)
	}
	return currentNodeStack.peek()
}
