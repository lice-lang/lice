/**
 * Created by ice1000 on 2017/2/26.
 *
 * @author ice1000
 */
package lice

import lice.compiler.model.Ast
import lice.compiler.parse.buildNode
import lice.compiler.parse.mapAst
import lice.compiler.util.SymbolList
import lice.repl.Main
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
		Ast(mapAst(
				node = buildNode(code),
				symbolList = symbolList)
		).root.eval()
	}
}
