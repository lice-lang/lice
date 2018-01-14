package org.lice.parse

import org.lice.core.SymbolList
import org.lice.model.*
import org.lice.util.ParseException
import java.util.*

abstract class ASTNode internal constructor(val metaData: MetaData) {
	abstract fun accept(sema: Sema): Node
}

class ASTAtomicNode internal constructor(metaData: MetaData, val token: Token) : ASTNode(metaData) {
	override fun accept(sema: Sema) = when (token.type) {
		Token.TokenType.BinNumber -> ValueNode(token.strValue.toBinInt(), metaData)
		Token.TokenType.OctNumber -> ValueNode(token.strValue.toOctInt(), metaData)
		Token.TokenType.HexNumber -> ValueNode(token.strValue.toHexInt(), metaData)
		Token.TokenType.DecNumber -> ValueNode(token.strValue.toInt(), metaData)
		Token.TokenType.LongInteger -> ValueNode(token.strValue.toLong(), metaData)
		Token.TokenType.BigInt -> ValueNode(token.strValue.toBigInt(), metaData)
		Token.TokenType.BigDec -> ValueNode(token.strValue.toBigDec(), metaData)
		Token.TokenType.FloatNumber -> ValueNode(token.strValue.toFloat(), metaData)
		Token.TokenType.DoubleNumber -> ValueNode(token.strValue.toDouble(), metaData)
		Token.TokenType.StringLiteral -> ValueNode(token.strValue, metaData)
		Token.TokenType.Identifier -> SymbolNode(sema.symbolList, token.strValue, metaData)
		Token.TokenType.LispKwd, Token.TokenType.EOI ->
			throw ParseException("Unexpected token '${token.strValue}'", metaData)
	}
}

class ASTRootNode(private val subNodes: ArrayList<ASTNode>) : ASTNode(MetaData()) {
	override fun accept(sema: Sema) = ExpressionNode(
			SymbolNode(sema.symbolList, "", metaData), metaData, subNodes.map { it.accept(sema) })
}

class ASTListNode(lParthMetaData: MetaData, private val subNodes: ArrayList<ASTNode>) : ASTNode(lParthMetaData) {
	override fun accept(sema: Sema) = if (subNodes.size > 0) {
		val first = subNodes[0]
		val mapFirstResult = first.accept(sema)
		if (mapFirstResult is ValueNode) mapFirstResult
		else {
			val mappedNodes = (1 until subNodes.size).map { subNodes[it].accept(sema) }
			ExpressionNode(mapFirstResult, metaData, mappedNodes)
		}
	} else ValueNode(null, metaData)
}

class Sema @JvmOverloads constructor(var symbolList: SymbolList = SymbolList())
