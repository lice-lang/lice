/**
 * Created by ice1000 on 2017/2/26.
 *
 * @author ice1000
 */
package org.lice

import org.lice.compiler.parse.buildNode
import org.lice.compiler.parse.mapAst
import org.lice.compiler.util.SymbolList
import org.lice.repl.Main
import java.io.File

object Lice {
	@JvmOverloads
	@JvmStatic
	fun run(
			file: File,
			symbolList: SymbolList = SymbolList()
	) = Main.interpret(file, symbolList)

	@JvmOverloads
	@JvmStatic
	fun run(
			code: String,
			symbolList: SymbolList = SymbolList()) {
		mapAst(
				node = buildNode(code),
				symbolList = symbolList).eval()
	}
}
