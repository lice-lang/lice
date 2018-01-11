/**
 * Created by ice1000 on 2017/2/26.
 *
 * @author ice1000
 * @since 1.0.0
 */
package org.lice

import org.lice.core.SymbolList
import org.lice.parse.*
import org.lice.repl.Main
import org.lice.util.InterpretException
import org.lice.util.ParseException
import java.io.File

object Lice {
	@JvmOverloads
	@JvmStatic
	fun run(file: File, symbolList: SymbolList = SymbolList()) =
			Main.interpret(file, symbolList)

	@JvmOverloads
	@JvmStatic
	fun run(code: String, symbolList: SymbolList = SymbolList()): Any? {
		try {
			return Parser.parseTokenStream(Lexer(code)).accept(Sema(symbolList)).eval()
		} catch (e: ParseException) {
			e.prettyPrint(code.split("\n"))
		} catch (e: InterpretException) {
			e.prettyPrint(code.split("\n"))
		}
		return null
	}
}
