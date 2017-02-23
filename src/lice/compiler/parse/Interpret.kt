/**
 * Created by ice1000 on 2017/2/21.
 *
 * @author ice1000
 */
@file:JvmName("Parse")
@file:JvmMultifileClass

package lice.compiler.parse

import lice.compiler.model.*
import lice.compiler.util.*
import lice.compiler.util.ParseException.Factory.undefinedFunction
import java.io.File


/**
 * This is the core implementation of mapAst
 *
 * @param symbolList the symbol list
 * @param str the string to parse
 * @return parsed node
 */
fun parseValue(
		str: String,
		symbolList: SymbolList): Node {
	return when {
		str.isEmpty() || str.isBlank() ->
			EmptyNode
		str.isString() ->
			ValueNode(Value(str
					.substring(1, str.length - 1)
					.apply {
						// TODO replace \n, \t, etc.
					}))
		str.isInt() ->
			ValueNode(str.toInt())
		str.isHexInt() ->
			ValueNode(str.toHexInt())
		str.isBinInt() ->
			ValueNode(str.toBinInt())
		"true" == str ->
			ValueNode(true)
		"false" == str ->
			ValueNode(false)
// TODO() is float
// TODO() is double
// TODO() is type
//	str.debugOutput()
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
//		if (str.isString() and (node.list.size > 1)) JvmReflectionNode(
//				str.substring(1, str.length - 1),
//				mapAst(node.list[1], symbolList),
//				node
//						.list
//						.subList(2, node.list.size)
//						.map { strNode ->
//							mapAst(
//									node = strNode,
//									symbolList = symbolList
//							)
//						}
//		) else
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
		parseValue(
				str = node.str,
				symbolList = symbolList
		)
	else -> // empty
		EmptyNode
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
