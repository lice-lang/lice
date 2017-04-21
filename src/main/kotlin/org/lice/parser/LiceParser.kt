package org.lice.parser

import org.lice.compiler.model.Node
import java.io.Reader
import java.io.StringReader
import java.util.regex.*

/**
 * Created by glavo on 17-4-2.
 *
 * @author Glavo
 * @since 3.0
 */
class LiceParser(private val reader: Reader) : Parser {
    override fun mapAst(symbol: SymbolList): Node {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var line = 1


	companion object TOKEN {
        val string: Pattern =
                Pattern.compile(""""([^"\x00-\x1F\x7F\\]|\\[\\'"bnrt]|\\u[a-fA-F0-9]{4})*"""")


		interface Token {
			val value: String

			companion object BraceTokens {
				val RP = CharToken(')')
				val LP = CharToken('(')
				val RP_C = CharToken('）')
				val LP_C = CharToken('（')
				val LBT = CharToken('[')
				val RBT = CharToken(']')
				val LBT_C = CharToken('【')
				val RBT_C = CharToken('】')
				val LBE = CharToken('{')
				val RBE = CharToken('}')
				val BRACES_CHARS = listOf(RP, LP, RP_C, LP_C, LBT, RBT, LBT_C, RBT_C, LBE, RBE)
//						.map { it.charValue }
			}
		}

		class CharToken(val charValue: Char) : Token {
			override val value = charValue.toString()
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


