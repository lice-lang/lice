package org.lice.parser

import org.lice.compiler.model.Node
import org.lice.compiler.model.StringNode
import org.lice.compiler.util.SymbolList
import org.lice.compiler.parse.buildNode
import java.io.BufferedReader
import java.io.Reader

/**
 * Created by glavo on 17-3-31.
 *
 * @author Glavo
 * @version 1.0.0
 */
interface Parser {
	fun stringNode(str: String): StringNode

	fun mapAst(symbol: SymbolList): Node

	companion object {
		fun defaultParser(str: String): Parser {
			return object : Parser {
				val node: StringNode by lazy {
					buildNode(str)
				}

				override fun mapAst(symbol: SymbolList): Node = org.lice.compiler.parse.mapAst(node)

				override fun stringNode(str: String): StringNode  = node

			}
		}

		fun defaultParser(reader: Reader): Parser = defaultParser(reader.readText())

	}
}