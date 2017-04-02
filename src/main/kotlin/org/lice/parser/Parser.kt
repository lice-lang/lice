package org.lice.parser

import org.lice.compiler.model.Node
import org.lice.compiler.parse.buildNode
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
	fun stringNode(str: String) = stringNode()

	@Deprecated(
		message = "Since 3.0"
	)
	fun stringNode(): StringNode

	fun mapAst(symbol: SymbolList): Node = org.lice.compiler.parse.mapAst(stringNode())

	companion object Default {
		fun defaultParser(str: String): Parser {
			return object : Parser {
				val node: StringNode by lazy { buildNode(str) }
				override fun stringNode(): StringNode = node
			}
		}

		fun defaultParser(reader: Reader): Parser = defaultParser(reader.readText())
	}
}