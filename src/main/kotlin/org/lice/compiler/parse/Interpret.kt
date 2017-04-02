/**
 * Created by ice1000 on 2017/2/21.
 *
 * @author ice1000
 * @since 1.0.0
 */
@file:JvmName("Parse")
@file:JvmMultifileClass

package org.lice.compiler.parse

import org.lice.compiler.model.*
import org.lice.compiler.model.Value.Objects.Nullptr
import org.lice.compiler.util.InterpretException
import org.lice.compiler.util.SymbolList
import org.lice.compiler.util.forceRun
import java.io.File


/**
 * This is the core implementation of mapAst
 *
 * @param str the string to parse
 * @return parsed node
 */
fun parseValue(
		str: String,
		meta: MetaData): Node? {
	if (str.isEmpty() or str.isBlank())
		return EmptyNode(meta)
	else if (str.isString()) return ValueNode(str
			.substring(
					startIndex = 1,
					endIndex = str.length - 1
			), meta)
	if (str.isOctInt())
		return ValueNode(str.toOctInt(), meta)
	if (str.isInt())
		return ValueNode(str.toInt(), meta)
	if (str.isHexInt())
		return ValueNode(str.toHexInt(), meta)
	if (str.isBinInt())
		return ValueNode(str.toBinInt(), meta)
	if (str.isBigInt())
		return ValueNode(str.toBigInt(), meta)
	if ("null" == str)
		return ValueNode(Nullptr, meta)
	if ("true" == str)
		return ValueNode(true, meta)
	if ("false" == str)
		return ValueNode(false, meta)
	forceRun {
		return if (str.length < 0xF)
			ValueNode(str.toFloat(), meta)
		else ValueNode(str.toDouble(), meta)
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
	is StringMiddleNode -> when (node.list[0]) {
		is StringLeafNode ->
			ExpressionNode(
					symbolList = symbolList,
					function = node.list[0].strRepr,
					meta = node.meta,
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
		// FIXME add lambda support
		else -> throw InterpretException("Using expression as lambda is not supported yet.", node.meta)
	}
	is StringLeafNode ->
		parseValue(
				str = node.str,
				meta = node.meta
		) ?: SymbolNode(
				symbolList = symbolList,
				name = node.str,
				meta = node.meta
		)
	else -> // empty
		EmptyNode(node.meta)
}

/**
 * create an abstract syntax tree
 *
 * @param file source code file
 * @param symbolList symbol list, with a default value
 * @return generated ast
 */
@Suppress("DEPRECATION")
@Deprecated(
		message = "",
		level = DeprecationLevel.WARNING,
		replaceWith = ReplaceWith("createRootNode")
)
fun createAst(
		file: File,
		symbolList: SymbolList = SymbolList(init = true)
): Ast {
	val code = file.readText()
	val fp = "FILE_PATH"
	if (symbolList.getVariable(name = fp) == null)
		symbolList.setVariable(
				name = fp,
				value = ValueNode(any = file.absolutePath)
		)
	val stringTreeRoot = buildNode(code)
	return Ast(
			mapAst(
					node = stringTreeRoot,
					symbolList = symbolList
			)
	)
}

/**
 * create an executable node
 *
 * @param file source code file
 * @param symbolList symbol list, with a default value
 * @return generated root node of the ast
 */
fun createRootNode(
		file: File,
		symbolList: SymbolList = SymbolList(init = true)
): Node {
	val code = file.readText()
	val fp = "FILE_PATH"
	if (symbolList.getVariable(name = fp) == null)
		symbolList.setVariable(
				name = fp,
				value = ValueNode(any = file.absolutePath)
		)
	val stringTreeRoot = buildNode(code)
	return mapAst(
			node = stringTreeRoot,
			symbolList = symbolList
	)
}