package org.lice.parser

import org.lice.compiler.model.StringNode
import java.io.Reader
import java.io.StringReader

/**
 * Created by glavo on 17-4-2.
 *
 * @author Glavo
 * @since 3.0
 */
class LiceParser(private val reader: Reader) : Parser {
	private var line: Int = 1
	private var c: Char? = null
	private var eof: Boolean = false

	private val node: StringNode by lazy {
		mkNode()
	}

	constructor(str: String) : this(StringReader(str))

	override fun stringNode(): StringNode = node

	private fun mkNode(): StringNode {
		val t = nextToken()
		when (t) {
			Token.LP -> {

			}
		}
	}

	private fun read(): Char? {
		val i = reader.read()
		if (i == -1) {
			c = null
			return null
		}

		c = i.toChar()
		if (c == '\n')
			line++

		return i.toChar()
	}

	private fun skip(): Unit {
		while (c.isBlank())
			read()
	}

	private fun nextToken(): Token {
		skip()

		if (c == null)
			return EmptyToken

		val sb = StringBuilder()

		when (c) {
			'\"' -> {
				loop@
				while (true) {
					when (read()) {
						'\\' -> {
							when (read()) {
								'\\' -> {
									sb.append('\\')
									continue@loop
								}
								'\"' -> {
									sb.append('\"')
									continue@loop
								}
								'\'' -> {
									sb.append('\'')
									continue@loop
								}

								'/' -> {
									sb.append('/')
									continue@loop
								}
								'b' -> {
									sb.append('\b')
									continue@loop
								}
								'n' -> {
									sb.append('\n')
									continue@loop
								}
								'r' -> {
									sb.append('\r')
									continue@loop
								}
								't' -> {
									sb.append('\t')
									continue@loop
								}
								'u' -> {
									val ii = Integer.parseInt(StringBuilder()
										.append(read()
											?: throw ParserException("error: unclosed string literal"))
										.append(read()
											?: throw ParserException("error: unclosed string literal"))
										.append(read()
											?: throw ParserException("error: unclosed string literal"))
										.append(read()
											?: throw ParserException("error: unclosed string literal"))
										.toString(), 16)
									sb.append(ii.toChar())
									continue@loop
								}
								null -> throw ParserException("error: unclosed string literal")

								else -> throw ParserException("error: invalid escape character: " + c)
							}
						}
						'\"' -> {
							read()
							return StringToken(sb.toString())
						}
						else -> {
							sb.append(c ?: throw ParserException("error: unclosed string literal"))
						}
					}
				}

			}

			'\'' -> {
				loop@
				while (true) {
					when (read()) {
						'\\' -> {
							when (read()) {
								'\\' -> {
									sb.append('\\')
									continue@loop
								}
								'\"' -> {
									sb.append('\"')
									continue@loop
								}
								'\'' -> {
									sb.append('\'')
									continue@loop
								}

								'/' -> {
									sb.append('/')
									continue@loop
								}
								'b' -> {
									sb.append('\b')
									continue@loop
								}
								'n' -> {
									sb.append('\n')
									continue@loop
								}
								'r' -> {
									sb.append('\r')
									continue@loop
								}
								't' -> {
									sb.append('\t')
									continue@loop
								}
								'u' -> {
									val ii = Integer.parseInt(StringBuilder()
										.append(read()
											?: throw ParserException("error: unclosed string literal"))
										.append(read()
											?: throw ParserException("error: unclosed string literal"))
										.append(read()
											?: throw ParserException("error: unclosed string literal"))
										.append(read()
											?: throw ParserException("error: unclosed string literal"))
										.toString(), 16)
									sb.append(ii.toChar())
									continue@loop
								}
								null -> throw ParserException("error: unclosed string literal")

								else -> throw ParserException("error: invalid escape character: " + c)
							}
						}
						'\'' -> {
							read()
							return StringToken(sb.toString())
						}
						else -> {
							sb.append(c ?: throw ParserException("error: unclosed string literal"))
						}
					}
				}

			}

			'“' -> {
				loop@
				while (true) {
					when (read()) {
						'\\' -> {
							when (read()) {
								'\\' -> {
									sb.append('\\')
									continue@loop
								}
								'\"' -> {
									sb.append('\"')
									continue@loop
								}
								'\'' -> {
									sb.append('\'')
									continue@loop
								}

								'/' -> {
									sb.append('/')
									continue@loop
								}
								'b' -> {
									sb.append('\b')
									continue@loop
								}
								'n' -> {
									sb.append('\n')
									continue@loop
								}
								'r' -> {
									sb.append('\r')
									continue@loop
								}
								't' -> {
									sb.append('\t')
									continue@loop
								}
								'u' -> {
									val ii = Integer.parseInt(StringBuilder()
										.append(read()
											?: throw ParserException("error: unclosed string literal"))
										.append(read()
											?: throw ParserException("error: unclosed string literal"))
										.append(read()
											?: throw ParserException("error: unclosed string literal"))
										.append(read()
											?: throw ParserException("error: unclosed string literal"))
										.toString(), 16)
									sb.append(ii.toChar())
									continue@loop
								}
								null -> throw ParserException("error: unclosed string literal")

								else -> throw ParserException("error: invalid escape character: " + c)
							}
						}
						'”' -> {
							read()
							return StringToken(sb.toString())
						}
						else -> {
							sb.append(c ?: throw ParserException("error: unclosed string literal"))
						}
					}
				}

			}

			'(' -> return Token.LP

			')' -> return Token.RP

			'[' -> return Token.LBT

			']' -> return Token.RBT

			'{' -> return Token.LBE

			'}' -> return Token.RBE

			else -> {
				while(!c.isBlank() && !c.isBracket()) {
					sb.append(c ?: break)
					read()
				}

				return SymbolToken(sb.toString())
			}
		}

	}
}

interface Token {
	val value: String

	companion object {
		val LP: Token = object : Token {
			override val value: String = "("
		}

		val RP: Token = object : Token {
			override val value: String = ")"
		}

		val LBT: Token = object : Token {
			override val value: String = "["
		}

		val RBT: Token = object : Token {
			override val value: String = "]"
		}

		val LBE: Token = object : Token {
			override val value: String = "{"
		}

		val RBE: Token = object : Token {
			override val value: String = "}"
		}
	}
}

private data class StringToken(override val value: String) : Token

private data class SymbolToken(override val value: String) : Token

private data class NumberToken(override val value: String) : Token {

}

private object EmptyToken : Token {
	override val value: String = ""
}

