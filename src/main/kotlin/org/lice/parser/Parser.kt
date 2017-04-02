package org.lice.parser

import org.lice.compiler.model.Node
import org.lice.compiler.model.StringNode
import org.lice.compiler.parse.buildNode
import org.lice.compiler.util.SymbolList
import java.io.Reader

typealias MetaData = org.lice.compiler.model.MetaData
typealias StringNode = org.lice.compiler.model.StringNode


/**
 * Created by glavo on 17-3-31.
 *
 * @author Glavo
 * @since v2.5
 */
interface Parser {

	@Deprecated(
			"str is not used, so do not pass any parameter!",
			level = DeprecationLevel.ERROR,
			replaceWith = ReplaceWith("stringNode()")
	)
	fun stringNode(str: String) = stringNode()

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