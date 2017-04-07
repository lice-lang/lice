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

	@Deprecated(
			"Since 2.5, str is not used, so do not pass any parameter!",
			level = DeprecationLevel.ERROR,
			replaceWith = ReplaceWith("stringNode()")
	)
	fun stringNode(str: String): StringNode = throw RuntimeException("")

	@Deprecated(
			message = "Since 3.0",
			level = DeprecationLevel.ERROR,
			replaceWith = ReplaceWith("stringNode()")
	)
	fun stringNode(): StringNode = throw RuntimeException("")

	fun mapAst(symbol: SymbolList): Node

	companion object Default {
		fun defaultParser(str: String): Parser {
			TODO("")
		}

		fun defaultParser(reader: Reader) = defaultParser(reader.readText())
	}
}