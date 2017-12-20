/**
 * Created by ice1000 on 2017/2/26.
 *
 * @author ice1000
 * @since 1.0.0
 */
package org.lice

import org.lice.compiler.parse.buildNode
import org.lice.compiler.parse.mapAst
import org.lice.core.SymbolList
import org.lice.repl.Main
import java.io.File

const val VERSION: String = "v3.1.1"

object Lice {
	@JvmOverloads
	@JvmStatic
	fun run(file: File, symbolList: SymbolList = SymbolList()) =
			Main.interpret(file, symbolList).o

	@JvmOverloads
	@JvmStatic
	fun run(code: String, symbolList: SymbolList = SymbolList()) =
			mapAst(node = buildNode(code), symbolList = symbolList).eval().o
}
