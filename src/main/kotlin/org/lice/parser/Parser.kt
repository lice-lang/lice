package org.lice.parser

import org.lice.compiler.model.Node
import org.lice.compiler.model.StringNode
import org.lice.compiler.parse.buildNode
import org.lice.compiler.util.SymbolList
import java.io.Reader

/**
 * Created by glavo on 17-3-31.
 *
 * @author Glavo
 * @version v2.5
 */
interface Parser {

	@Deprecated("", level = DeprecationLevel.WARNING, replaceWith = ReplaceWith("stringNode()"))
	fun stringNode(str: String): StringNode = stringNode()

	fun stringNode(): StringNode

	fun mapAst(symbol: SymbolList): Node = org.lice.compiler.parse.mapAst(stringNode())

	companion object {
		fun defaultParser(str: String): Parser {
			return object : Parser {
				val node: StringNode by lazy {
					buildNode(str)
				}

				override fun mapAst(symbol: SymbolList): Node = org.lice.compiler.parse.mapAst(node)

				override fun stringNode(): StringNode = node

			}
		}

		fun defaultParser(reader: Reader): Parser = defaultParser(reader.readText())

	}
}