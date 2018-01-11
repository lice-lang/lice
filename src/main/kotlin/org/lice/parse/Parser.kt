package org.lice.parse

import org.lice.util.ParseException
import java.util.*

object Parser {
	fun parseTokenStream(lexer: Lexer): ASTNode {
		val nodes = ArrayList<ASTNode>()
		while (lexer.currentToken().type != Token.TokenType.EOI) nodes.add(parseNode(lexer))
		return ASTRootNode(nodes)
	}

	private fun parseNode(l: Lexer): ASTNode {
		return if (l.currentToken().type == Token.TokenType.LispKwd && l.currentToken().strValue == "(") {
			parseList(l)
		} else {
			val ret = ASTAtomicNode(l.currentToken().metaData, l.currentToken())
			l.nextToken()
			ret
		}
	}

	private fun parseList(l: Lexer): ASTListNode {
		// assert(l.currentToken().type == Token.TokenType.LispKwd && l.currentToken().strValue == "(")
		l.nextToken()
		val leftParthToken = l.currentToken()
		val subNodes = ArrayList<ASTNode>()
		while (!(l.currentToken().type == Token.TokenType.LispKwd && l.currentToken().strValue == ")")) {
			subNodes.add(parseNode(l))
			if (l.currentToken().type == Token.TokenType.EOI)
				throw ParseException("Unexpected EOI, expected ')'", l.currentToken().metaData)
		}

		l.nextToken()
		return ASTListNode(leftParthToken.metaData, subNodes)
	}
}
