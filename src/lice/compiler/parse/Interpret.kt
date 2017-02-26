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
import lice.compiler.util.ParseException.Factory.undefinedFunction
import lice.compiler.util.SymbolList
import lice.compiler.util.serr
import java.io.File


/**
 * This is the core implementation of mapAst
 *
 * @param str the string to parse
 * @return parsed node
 */
fun parseValue(str: String): Node {
	return when {
		str.isEmpty() || str.isBlank() ->
			EmptyNode
		str.isString() ->
			ValueNode(Value(str
					.substring(1, str.length - 1)
					.apply {
						// TODO replace \n, \t, etc.
					}))
		str.isOctInt() ->
			ValueNode(str.toOctInt())
		str.isInt() ->
			ValueNode(str.toInt())
		str.isHexInt() ->
			ValueNode(str.toHexInt())
		str.isBinInt() ->
			ValueNode(str.toBinInt())
		str.isBigInt() ->
			ValueNode(str.toBigInt())
		"null" == str ->
			ValueNode(Nullptr)
		"true" == str ->
			ValueNode(true)
		"false" == str ->
			ValueNode(false)
// TODO() is float
// TODO() is double
		else -> {
			serr("error token: $str")
			EmptyNode // do nothing
		}
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
				symbolList,
				symbolList.getFunctionId(str)
						?: undefinedFunction(str),
				node
						.list
						.subList(1, node.list.size)
						.map { strNode ->
							mapAst(
									node = strNode,
									symbolList = symbolList
							)
						}
		)
	}
	is StringLeafNode ->
		parseValue(str = node.str)
	else -> // empty
		EmptyNode
}

fun createAst(
		file: File,
		symbolList: SymbolList = SymbolList(true)): Ast {
	val code = file.readText()
	val fp = "FILE_PATH"
	if (symbolList.getVariable(fp) == null)
		symbolList.setVariable(fp, ValueNode(file.absolutePath))
	val stringTreeRoot = buildNode(code)
	return Ast(
			mapAst(
					node = stringTreeRoot,
					symbolList = symbolList
			),
			symbolList
	)
}
