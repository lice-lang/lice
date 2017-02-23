/**
 * Created by ice1000 on 2017/2/21.
 *
 * @author ice1000
 */
@file:JvmName("Parse")
@file:JvmMultifileClass

package lice.compiler.parse

import lice.compiler.model.*
import lice.compiler.util.ParseException.Factory.undefinedFunction
import lice.compiler.util.SymbolList
import lice.compiler.util.serr
import java.io.File


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
				.substring(1, str.length - 1)
				.apply {
					// TODO replace \n, \t, etc.
				}))
	if (str.isInt())
		return ValueNode(str.toInt())
	if (str.isHexInt())
		return ValueNode(str.toHexInt())
	if (str.isBinInt())
		return ValueNode(str.toBinInt())
	if (str == "true")
		return ValueNode(true)
	if (str == "false")
		return ValueNode(false)
	// TODO() is float
	// TODO() is double
	// TODO() is type
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
		serr("error token: $str")
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
		symbolList: SymbolList = SymbolList()): Node = when (node) {
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
//		ls.size.verboseOutput()
//		node
//				.list[0]
//				.strRepr
//				.verboseOutput()
		ExpressionNode(
				symbolList,
				symbolList.getFunctionId(node.list[0].strRepr)
						?: undefinedFunction(node.list[0].strRepr),
				ls
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
