package org.lice.parse

import org.lice.model.MetaData
import org.lice.util.ParseException

class Lexer(sourceCode: String) {

	private val sourceCode = sourceCode.toCharArray()
	private var line = 1
	private var col: Int
	private var charIndex: Int
	private var tokenBuffer: MutableList<Token> = arrayListOf()
	private var currentTokenIndex: Int = 0

	init {
		this.col = 1
		this.charIndex = 0

		doSplitTokens()
	}

	fun currentToken(): Token {
		assert(currentTokenIndex < this.tokenBuffer.size)
		return this.tokenBuffer[this.currentTokenIndex]
	}

	fun peekOneToken(): Token {
		assert(currentTokenIndex + 1 < this.tokenBuffer.size)
		return this.tokenBuffer[this.currentTokenIndex + 1]
	}

	fun nextToken() = this.currentTokenIndex++

	private fun doSplitTokens() {
		while (currentChar() != '\u0000') {
			when {
				currentChar() == '-' -> disambiguateIdentifierOrNegative()
				currentChar() in decDigits -> lexNumber()
				currentChar() in firstIdChars -> lexIdentifier()
				currentChar() in lispSymbols -> lexSingleCharToken()
				currentChar() == '"' -> lexString()
				currentChar() == ';' -> skipComment()
				currentChar() in blanks -> nextChar()
				else -> throw ParseException("Unknown character ${currentChar()}", MetaData(this.line, this.line, this.col, this.col + 1))
			}
		}
		tokenBuffer.add(Token(Token.TokenType.EOI, "", this.line, this.line, this.col, this.col + 1))
		this.currentTokenIndex = 0
	}

	private fun disambiguateIdentifierOrNegative() {
		if (peekOneChar() in decDigits) lexNumber()
		else lexIdentifier()
	}

	private fun lexIdentifier() {
		val line = this.line
		val startAtCol = this.col
		val str = scanFullString(idChars)
		this.tokenBuffer.add(Token(Token.TokenType.Identifier, str, this.line, this.line, startAtCol, this.col))
	}

	private fun lexNumber() {
		val line = this.line
		val startAtCol = this.col
		var isNegative = false
		var numberType: Token.TokenType
		var numberStr: String

		if (currentChar() == '-') {
			isNegative = true
			nextChar()
		}

		if (currentChar() != '0') {
			numberType = Token.TokenType.DecNumber
			numberStr = scanFullString(decDigits)
		} else {
			when (peekOneChar()) {
				'b', 'B' -> {
					nextChar()
					nextChar()
					numberType = Token.TokenType.BinNumber
					numberStr = "0b${scanFullString(binDigits)}"
				}
				'o', 'O' -> {
					nextChar()
					nextChar()
					numberType = Token.TokenType.OctNumber
					numberStr = "0o${scanFullString(octDigits)}"
				}
				'x', 'X' -> {
					nextChar()
					nextChar()
					numberType = Token.TokenType.HexNumber
					numberStr = "0x${scanFullString(hexDigits)}"
				}
				else -> {
					numberType = Token.TokenType.DecNumber
					numberStr = scanFullString(decDigits)
				}
			}
		}

		if (currentChar() == '.') {
			if (numberType !== Token.TokenType.DecNumber) throw ParseException("Only decimal floating numbers are allowed",
					MetaData(line, this.line, startAtCol, this.col))
			nextChar()
			numberStr = "$numberStr.${scanFullString(decDigits)}"
			numberType = if (numberStr.length <= 9) Token.TokenType.FloatNumber else Token.TokenType.DoubleNumber
		}

		when (currentChar()) {
			'f', 'F' -> {
				if (!numberType.isDecimal) throw ParseException("Only decimal floating numbers are allowed",
						MetaData(line, this.line, startAtCol, this.col))
				nextChar()
				numberType = Token.TokenType.FloatNumber
			}
			'd', 'D' -> {
				if (!numberType.isDecimal) throw ParseException("Only decimal floating numbers are allowed",
						MetaData(line, this.line, startAtCol, this.col))
				nextChar()
				numberType = Token.TokenType.DoubleNumber
			}
			'm', 'M' -> {
				if (!numberType.isDecimal) throw ParseException("'m' or 'M' is used for big decimals",
						MetaData(line, this.line, startAtCol, this.col))
				nextChar()
				numberType = Token.TokenType.BigDec
			}
			'n', 'N' -> {
				if (!numberType.isIntegral) throw ParseException("'m' or 'M' is used for big integers",
						MetaData(line, this.line, startAtCol, this.col))
				nextChar()
				numberType = Token.TokenType.BigInt
			}
			'l', 'L' -> {
				if (!numberType.isIntegral) throw ParseException("'l' or 'L' is only used for long integers",
						MetaData(line, this.line, startAtCol, this.col))
				nextChar()
				numberType = Token.TokenType.LongInteger
			}
		}

		if (currentChar() !in tokenDelimiters) throw ParseException("Unexpected character ${currentChar()}",
				MetaData(this.line, this.line, this.col, this.col + 1))

		if (isNegative) numberStr = "-$numberStr"
		tokenBuffer.add(Token(numberType, numberStr, line, this.line, startAtCol, this.col))
	}

	private fun scanFullString(allowedChars: String): String {
		val tokenStringBuilder = StringBuilder()
		while (currentChar() in allowedChars) {
			tokenStringBuilder.append(currentChar())
			nextChar()
		}
		return tokenStringBuilder.toString()
	}

	private fun lexSingleCharToken() {
		this.tokenBuffer.add(Token(Token.TokenType.LispKwd, Character.toString(currentChar()),
				this.line, this.line, this.col, this.col + 1))
		nextChar()
	}

	private fun lexString() {
		val atLine = this.line
		val startAtCol = this.col

		nextChar()

		val builder = StringBuilder()
		while (currentChar() != '"' && currentChar() != '\u0000') {
			if (currentChar() != '\\') {
				builder.append(currentChar())
				nextChar()
			} else {
				when (peekOneChar()) {
					'n' -> builder.append('\n')
					'f' -> builder.append('\u000C')
					't' -> builder.append('\t')
					'\\' -> builder.append('\\')
					'"' -> builder.append('\"')
					else -> throw ParseException("Illegal conversion sequence \\${peekOneChar()}",
							MetaData(this.line, this.line, this.col, this.col + 2))
				}
				nextChar()
				nextChar()
			}
		}

		if (currentChar() == '\u0000') throw ParseException("Unexpected EndOfInput.",
				MetaData(this.line, this.line, this.col, this.col + 1))
		nextChar()

		this.tokenBuffer.add(Token(Token.TokenType.StringLiteral, "$builder",
				atLine, this.line, startAtCol, this.col))
	}

	private fun skipComment() {
		assert(currentChar() == ';')
		while (currentChar() != '\n' && currentChar() != '\u0000') nextChar()
	}

	private fun currentChar() = if (charIndex >= sourceCode.size) '\u0000' else sourceCode[charIndex]
	private fun peekOneChar() = if (charIndex + 1 >= sourceCode.size) '\u0000' else sourceCode[charIndex + 1]

	private fun nextChar() {
		if (currentChar() == '\n') {
			line++
			col = 1
		} else col++
		charIndex++
	}

	companion object {
		private const val upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
		private const val lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz"
		private const val commonSymbols = "<>[]+-*/%|!@#$%^&*_:=.?\\{}~"
		private const val binDigits = "01"
		private const val octDigits = "01234567"
		private const val decDigits = "0123456789"
		private const val hexDigits = "0123456789ABCDEF"
		private const val blanks = " \u000C\n\t\r,"
		private const val lispSymbols = "()"
		private const val tokenDelimiters = blanks + lispSymbols + ";\u0000"
		private const val firstIdChars = upperCaseLetters + lowerCaseLetters + commonSymbols
		private const val idChars = firstIdChars + decDigits
	}
}
