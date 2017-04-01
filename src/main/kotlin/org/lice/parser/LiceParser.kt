package org.lice.parser

import org.lice.compiler.model.Node
import org.lice.compiler.model.StringNode
import org.lice.compiler.util.SymbolList
import java.io.Reader
import java.io.StringReader
import kotlin.text.isBlank

/**
 * Created by glavo on 17-4-2.
 *
 * @author Glavo
 * @version v3.0
 */
class LiceParser(private val reader: Reader) : Parser {
	private var line: Int = 1
	private var c: Char = 0.toChar()
	private var eof: Boolean = false

	private val node: StringNode by lazy {


		TODO("")
	}

	constructor(str: String) : this(StringReader(str))

	override fun stringNode(): StringNode = node

	fun read(): Char? {
		val i = reader.read()
		if (i == -1) {
			c = 0.toChar()
			return null
		}
		if (i == '\n'.toInt()) line++
		c = i.toChar()
		return i.toChar()
	}

	fun skip(): Unit {
		while (c == ' ' || c == '\n' || c == '\b' || c == 'r' || c == '\t') read()
	}
}

private interface Token {
	val value: String
}

private data class StringToken(override val value: String) : Token

private data class SymbolToken(override val value: String) : Token


