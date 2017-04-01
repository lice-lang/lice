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
 * @version 1.0.0
 */
class LiceParser(val reader: Reader) : Parser {
	private var line = 1
	private var c = 0.toChar()

	constructor(str: String) : this(StringReader(str))

	override fun stringNode(): StringNode {
		TODO("not implemented")
	}

	override fun mapAst(symbol: SymbolList): Node {
		TODO("not implemented")
	}
}