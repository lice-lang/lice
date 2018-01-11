package org.lice.parse

import org.intellij.lang.annotations.Language
import org.junit.Test

import org.junit.Assert.assertEquals

class LexCommentTest {
	@Test(timeout = 100)
	fun testLexComment() {
		@Language("Lice")
		val src = "; Simply comments"
		val l = Lexer(src)
		assertEquals(Token.TokenType.EOI, l.currentToken().type)
	}

	@Test(timeout = 100)
	fun testLexComment2() {
		@Language("Lice")
		val src = """; Simply comments
@dentifier"""
		val l = Lexer(src)
		assertEquals(Token.TokenType.Identifier, l.currentToken().type)
		assertEquals("@dentifier", l.currentToken().strValue)
		assertEquals(Token.TokenType.EOI, l.peekOneToken().type)
	}
}
