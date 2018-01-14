package org.lice.parse

import org.intellij.lang.annotations.Language
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.lice.util.ParseException

class LexStringTest {

	@Rule
	@JvmField
	var thrown: ExpectedException = ExpectedException.none()

	@Test
	fun testLexString() {
		@Language("Lice")
		val srcCode = """"String""""
		val l = Lexer(srcCode)
		assertEquals(Token.TokenType.StringLiteral, l.currentToken().type)
		assertEquals("String", l.currentToken().strValue)
		assertEquals(Token.TokenType.EOI, l.peekOneToken().type)
	}

	@Test
	fun testLexConversionSequence() {
		@Language("Lice")
		val srcCode = "\"Str\\ning\""
		val l = Lexer(srcCode)
		assertEquals(Token.TokenType.StringLiteral, l.currentToken().type)
		assertEquals("Str\ning", l.currentToken().strValue)
		assertEquals(Token.TokenType.EOI, l.peekOneToken().type)
	}

	@Test
	fun testLexConversionSequenceFailed() {
		thrown.expect(ParseException::class.java)
		@Language("Lice")
		val srcCode = "\"\\q\""
		val l = Lexer(srcCode)
	}

	@Test
	fun testMissingQuote() {
		thrown.expect(ParseException::class.java)
		val srcCode = "\"String without closing quote"
		val l = Lexer(srcCode)
	}
}
