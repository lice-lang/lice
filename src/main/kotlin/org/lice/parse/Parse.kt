/**
 * Codes that parse the code into a string tree
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 * @since 1.0.0
 */
@file:JvmName("Parse")
@file:JvmMultifileClass

package org.lice.parse

import org.lice.model.*
import org.lice.util.ParseException
import java.util.*

fun buildNode(originalCode: String): StringNode {
	if (originalCode.isEmpty() || originalCode.isBlank())
		return EmptyStringNode(MetaData(1))
	val code = " $originalCode "
	var beginIndex = 0
	val currentNodeStack = Stack<StringMiddleNode>()
	currentNodeStack.push(StringMiddleNode(MetaData(1)))
	var elementStarted = true
	var lineNumber = 1
	var lastQuoteIndex = 0
	var quoteStarted = false
	var commentStarted = false
	fun check(index: Int) {
		if (elementStarted) {
			elementStarted = false
			currentNodeStack.peek()
					.add(StringLeafNode(MetaData(lineNumber), code.substring(startIndex = beginIndex, endIndex = index)))
		}
	}
	code.forEachIndexed { index, c ->
		if (c == '\n') commentStarted = false
		if (!commentStarted) when (c) {
			';' -> if (!quoteStarted) commentStarted = true
			'(' -> if (!quoteStarted) {
				check(index)
				currentNodeStack.push(StringMiddleNode(MetaData(lineNumber)))
				++beginIndex
			}
			')' -> if (!quoteStarted) {
				check(index)
				if (currentNodeStack.size <= 1) {
					val string = "Braces not match at line $lineNumber: Unexpected \')\'."
					throw ParseException(string)
				}
				val son = if (currentNodeStack.peek().empty) EmptyStringNode(MetaData(lineNumber))
				else currentNodeStack.peek()
				currentNodeStack.pop()
				currentNodeStack.peek().add(son)
			}
			' ', '\n', '\t', '\r', ',', '，', '　' -> {
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
				currentNodeStack.peek().add(StringLeafNode(MetaData(lineNumber), code
						.substring(startIndex = lastQuoteIndex, endIndex = index + 1)))
			}
			else -> if (!quoteStarted) elementStarted = true
		}
	}
	check(code.length - 1)
	if (currentNodeStack.size > 1) {
		val string = "Braces not match at line $lineNumber: Expected \')\'."
		throw ParseException(string)
	}
	return currentNodeStack.peek()
}
