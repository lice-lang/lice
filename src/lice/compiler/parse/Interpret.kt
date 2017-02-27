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
import lice.lang.Symbol
import java.io.File


/**
 * This is the core implementation of mapAst
 *
 * @param str the string to parse
 * @return parsed node
 */
fun parseValue(
		str: String,
		lineNumber: Int): Node {
	return when {
		str.isEmpty() or str.isBlank() ->
			EmptyNode(lineNumber)
		str.isString() ->
			ValueNode(str
					.substring(
							startIndex = 1,
							endIndex = str.length - 1
					)
					.apply {
						// TODO replace \n, \t, etc.
					}, lineNumber)
		str.isOctInt() ->
			ValueNode(str.toOctInt(), lineNumber)
		str.isInt() ->
			ValueNode(str.toInt(), lineNumber)
		str.isHexInt() ->
			ValueNode(str.toHexInt(), lineNumber)
		str.isBinInt() ->
			ValueNode(str.toBinInt(), lineNumber)
		str.isBigInt() ->
			ValueNode(str.toBigInt(), lineNumber)
		"null" == str ->
			ValueNode(Nullptr, lineNumber)
		"true" == str ->
			ValueNode(true, lineNumber)
		"false" == str ->
			ValueNode(false, lineNumber)
// TODO() is float
// TODO() is double
		else ->
			ValueNode(Symbol(str), lineNumber)
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
