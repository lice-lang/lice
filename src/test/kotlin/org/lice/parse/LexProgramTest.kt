package org.lice.parse

import org.intellij.lang.annotations.Language
import org.junit.Assert.assertEquals
import org.junit.Test

class LexProgramTest {
	@Test
	fun test1() {
		@Language("Lice")
		val src = """
			(def add a b (+ a b))
			(add 12.45M 13.14M)
			""".trimIndent()
		val l = Lexer(src)
		assertEquals(Token.TokenType.LispKwd, l.currentToken().type)
		assertEquals("(", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.Identifier, l.currentToken().type)
		assertEquals("def", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.Identifier, l.currentToken().type)
		assertEquals("add", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.Identifier, l.currentToken().type)
		assertEquals("a", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.Identifier, l.currentToken().type)
		assertEquals("b", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.LispKwd, l.currentToken().type)
		assertEquals("(", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.Identifier, l.currentToken().type)
		assertEquals("+", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.Identifier, l.currentToken().type)
		assertEquals("a", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.Identifier, l.currentToken().type)
		assertEquals("b", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.LispKwd, l.currentToken().type)
		assertEquals(")", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.LispKwd, l.currentToken().type)
		assertEquals(")", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.LispKwd, l.currentToken().type)
		assertEquals("(", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.Identifier, l.currentToken().type)
		assertEquals("add", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.BigDec, l.currentToken().type)
		assertEquals("12.45", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.BigDec, l.currentToken().type)
		assertEquals("13.14", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.LispKwd, l.currentToken().type)
		assertEquals(")", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.EOI, l.currentToken().type)
		l.nextToken()
	}

	@Test
	fun test2() {
		@Language("Lice")
		val src = """
			(string-add "String" ; comments
			"Liter
			al")
		""".trimIndent()
		val l = Lexer(src)
		assertEquals(Token.TokenType.LispKwd, l.currentToken().type)
		assertEquals("(", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.Identifier, l.currentToken().type)
		assertEquals("string-add", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.StringLiteral, l.currentToken().type)
		assertEquals("String", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.StringLiteral, l.currentToken().type)
		assertEquals("Liter\nal", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.LispKwd, l.currentToken().type)
		assertEquals(")", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.EOI, l.currentToken().type)
		l.nextToken()
	}
}
