package org.lice.parser

import org.lice.compiler.model.Node
import java.io.Reader

/**
 * Created by glavo on 17-3-31.
 *
 * @author Glavo
 * @since 2.5
 */
interface Parser {

	fun mapAst(symbol: SymbolList): Node

	companion object Default {
		fun defaultParser(str: String): Parser {
			TODO("")
		}

		fun defaultParser(reader: Reader) = defaultParser(reader.readText())
	}
}
