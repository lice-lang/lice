package org.lice.parser

import org.lice.compiler.model.Node
import org.lice.compiler.model.StringNode
import org.lice.compiler.util.SymbolList
import java.io.Reader
import java.io.StringReader

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
		if (i == -1) return null

		if (i == '\n'.toInt()) line++
		
		return i.toChar()
	}


}

private interface Token {
	val value: String
}

private data class StringToken(override val value: String) : Token

