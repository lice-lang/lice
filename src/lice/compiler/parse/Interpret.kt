/**
 * Created by ice1000 on 2017/2/21.
 *
 * @author ice1000
 */
@file:JvmName("Parse")
@file:JvmMultifileClass

package lice.compiler.parse

import lice.compiler.model.*
import lice.compiler.model.Value.Objects.Nullptr
import lice.compiler.util.SymbolList
import lice.compiler.util.forceRun
import java.io.File


/**
 * This is the core implementation of mapAst
 *
 * @param str the string to parse
 * @return parsed node
 */
fun parseValue(
		str: String,
		lineNumber: Int): Node? {
	if (str.isEmpty() or str.isBlank())
		return EmptyNode(lineNumber)
	else if (str.isString()) return ValueNode(str
			.substring(
					startIndex = 1,
					endIndex = str.length - 1
			)
			.apply {
				// TODO replace \n, \t, etc.
			}, lineNumber)
	if (str.isOctInt())
		return ValueNode(str.toOctInt(), lineNumber)
	if (str.isInt())
		return ValueNode(str.toInt(), lineNumber)
	if (str.isHexInt())
		return ValueNode(str.toHexInt(), lineNumber)
	if (str.isBinInt())
		return ValueNode(str.toBinInt(), lineNumber)
	if (str.isBigInt())
		return ValueNode(str.toBigInt(), lineNumber)
	if ("null" == str)
		return ValueNode(Nullptr, lineNumber)
	if ("true" == str)
		return ValueNode(true, lineNumber)
	if ("false" == str)
		return ValueNode(false, lineNumber)
	forceRun {
		return if (str.length < 32)
			ValueNode(str.toFloat(), lineNumber)
		else ValueNode(str.toDouble(), lineNumber)
	}
	return null
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
		symbolList: SymbolList = SymbolList()): Node = when (node) {
	is StringMiddleNode -> {
		val str = node.list[0].strRepr
		ExpressionNode(
				symbolList = symbolList,
				function = str,
				lineNumber = node.lineNumber,
				params = node
						.list
						.subList(
								fromIndex = 1,
								toIndex = node.list.size
						)
						.map { strNode ->
							mapAst(
									node = strNode,
									symbolList = symbolList
							)
						}
		)
	}
	is StringLeafNode ->
		parseValue(
				str = node.str,
				lineNumber = node.lineNumber
		) ?: SymbolNode(
				symbolList = symbolList,
				name = node.str,
				lineNumber = node.lineNumber
		)
	else -> // empty
		EmptyNode(node.lineNumber)
}

fun createAst(
		file: File,
		symbolList: SymbolList = SymbolList(init = true)): Ast {
	val code = file.readText()
	val fp = "FILE_PATH"
	if (symbolList.getVariable(name = fp) == null)
		symbolList.setVariable(
				name = fp,
				value = ValueNode(
						any = file.absolutePath,
						lineNumber = -1
				)
		)
	val stringTreeRoot = buildNode(code)
	return Ast(
			mapAst(
					node = stringTreeRoot,
					symbolList = symbolList
			)
	)
}
