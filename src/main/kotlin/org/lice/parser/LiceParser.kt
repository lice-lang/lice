package org.lice.parser

import org.lice.compiler.model.Node
import java.io.Reader
import java.io.StringReader

/**
 * Created by glavo on 17-4-2.
 *
 * @author Glavo
 * @since 3.0
 */
class LiceParser(private val reader: Reader) : Parser {

	private var line = 1
	private var c: Char? = null
	private var eof = false

	val node: Ast by lazy { mkAst() }

	constructor(str: String) : this(StringReader(str))

	override fun mapAst(symbol: SymbolList): Node {
		TODO("not implemented")
	}

	fun mkAst(): Ast {
		val t = nextToken()
		return when (t) {
			Token.LP -> parserBlock()
			Token.RBE, Token.RBT, Token.RP -> throw ParseException("error: at line $line")
			is StringToken, is NumberToken, is SymbolToken -> Leaf(t)
			else -> TODO("")
		}
	}

	fun read(): Char? {
		val i = reader.read()
		if (i == -1) {
			c = null
			return null
		}

		c = i.toChar()
		if (c == '\n') line++

		return i.toChar()
	}

	fun readOrThrow(str: String) = read() ?: throw ParseException(str)

	fun skip() {
		while (c.isBlank()) read()
	}

	fun nextToken(): Token {
		skip()

		if (c == null) return EmptyToken

		val sb = StringBuilder()

		when (c) {
			'\"' -> {
				loop@ while (true) {
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
											.append(readOrThrow("error: unclosed string literal at line $line"))
											.append(readOrThrow("error: unclosed string literal at line $line"))
											.append(readOrThrow("error: unclosed string literal at line $line"))
											.append(readOrThrow("error: unclosed string literal at line $line"))
											.toString(), 16)
									sb.append(ii.toChar())
									continue@loop
								}
								null -> throw ParseException("error: unclosed string literal at line $line")
								else -> throw ParseException("error: invalid escape character: $c at line  at line $line")
							}
						}
						'\"' -> {
							read()
							return StringToken(sb.toString())
						}
						else -> sb.append(c ?: throw ParseException("error: unclosed string literal at line $line"))
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
											.append(readOrThrow("error: unclosed string literal"))
											.append(readOrThrow("error: unclosed string literal"))
											.append(readOrThrow("error: unclosed string literal"))
											.append(readOrThrow("error: unclosed string literal"))
											.toString(), 16)
									sb.append(ii.toChar())
									continue@loop
								}
								null -> throw ParseException("error: unclosed string literal")
								else -> throw ParseException("error: invalid escape character: " + c)
							}
						}
						'\'' -> {
							read()
							return StringToken(sb.toString())
						}
						else -> sb.append(c ?: throw ParseException("error: unclosed string literal"))
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
											.append(readOrThrow("error: unclosed string literal"))
											.append(readOrThrow("error: unclosed string literal"))
											.append(readOrThrow("error: unclosed string literal"))
											.append(readOrThrow("error: unclosed string literal"))
											.toString(), 16)
									sb.append(ii.toChar())
									continue@loop
								}
								null -> throw ParseException("error: unclosed string literal")
								else -> throw ParseException("error: invalid escape character: " + c)
							}
						}
						'”' -> {
							read()
							return StringToken(sb.toString())
						}
						else -> sb.append(c ?: throw ParseException("error: unclosed string literal"))
					}
				}
			}

			'(' -> {
				read()
				return Token.LP
			}

			')' -> {
				read()
				return Token.RP
			}

			'[' -> {
				read()
				return Token.LBT
			}

			']' -> {
				read()
				return Token.RBT
			}

			'{' -> {
				read()
				return Token.LBE
			}

			'}' -> {
				read()
				return Token.RBE
			}

			else -> {
				while (!c.isBlank() && !c.isBracket()) {
					sb.append(c ?: break)
					read()
				}

				return SymbolToken(sb.toString())
			}
		}

	}

	fun parserBlock(): Ast {
		val l = mutableListOf<Ast>()
		while (true) {
			val t = nextToken()
			when (t) {
				EmptyToken -> throw ParseException("error: ')' not found at line: $line")
				Token.LP -> l.add(parserBlock())
				Token.RP -> return Middle(l)
				else -> l.add(Leaf(t))
			}
		}
	}

	companion object TOKEN {
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

		data class StringToken(override val value: String) : Token

		data class SymbolToken(override val value: String) : Token

		data class NumberToken(override val value: String) : Token

		enum class NumberType {
			Integer,
			BigInteger
		}


		object EmptyToken : Token {
			override val value: String = ""
		}

		interface Ast

		data class Leaf(val value: Token) : Ast

		data class Middle(val values: MutableList<Ast>) : Ast
	}
}


