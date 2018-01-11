package org.lice.parse

import org.junit.Test

import org.junit.Assert.assertEquals

class LexIdentifierTest {

	@Test(timeout = 100)
	fun testLexIdentifier() {
		val srcCode = "Fuck@dentifier"
		val l = Lexer(srcCode)
		assertEquals(Token.TokenType.Identifier, l.currentToken().type)
		assertEquals("Fuck@dentifier", l.currentToken().strValue)
		assertEquals(Token.TokenType.EOI, l.peekOneToken().type)
	}

	@Test(timeout = 100)
	fun testLexIdentifiers() {
		val srcCode = "Fuck@dentifier _Yet1A2not->her@dentifier"
		val l = Lexer(srcCode)
		assertEquals(Token.TokenType.Identifier, l.currentToken().type)
		assertEquals("Fuck@dentifier", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.Identifier, l.currentToken().type)
		assertEquals("_Yet1A2not->her@dentifier", l.currentToken().strValue)
		assertEquals(Token.TokenType.EOI, l.peekOneToken().type)
	}

	@Test(timeout = 100)
	fun testLexIdentifiers2() {
		val srcCode = "Fuck@dentifier,_Yet1A2not->her@dentifier"
		val l = Lexer(srcCode)
		assertEquals(Token.TokenType.Identifier, l.currentToken().type)
		assertEquals("Fuck@dentifier", l.currentToken().strValue)
		l.nextToken()
		assertEquals(Token.TokenType.Identifier, l.currentToken().type)
		assertEquals("_Yet1A2not->her@dentifier", l.currentToken().strValue)
		assertEquals(Token.TokenType.EOI, l.peekOneToken().type)
	}
}
