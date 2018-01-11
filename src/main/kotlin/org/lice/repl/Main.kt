package org.lice.repl

import org.lice.core.SymbolList
import org.lice.lang.Echoer
import org.lice.parse.*
import org.lice.util.InterpretException
import org.lice.util.ParseException
import java.nio.file.*

/**
 * @author ice1000
 */

object Main {

	/**
	 * interpret code in a file
	 */
	fun interpret(file: Path, symbolList: SymbolList): Any? {
		val code = String(Files.readAllBytes(file))
		try {
			return Parser.parseTokenStream(Lexer(code)).accept(Sema(symbolList)).eval()
		} catch (e: ParseException) {
			e.prettyPrint(code.split("\n"))
		} catch (e: InterpretException) {
			e.prettyPrint(code.split("\n"))
		}
		return null
	}

	@JvmStatic
	fun main(vararg args: String) {
		Echoer.openOutput()
		if (args.isEmpty()) println("Please specify an input file.")
		else interpret(Paths.get(args.first()), SymbolList())
	}
}
