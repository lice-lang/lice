package org.lice.repl

import org.lice.core.SymbolList
import org.lice.lang.Echoer
import org.lice.parse.*
import org.lice.util.InterpretException
import org.lice.util.ParseException
import java.io.File

/**
 * @author ice1000
 */

object Main {

	/**
	 * interpret code in a file
	 */
	fun interpret(file: File, symbolList: SymbolList): Any? {
		val code = file.readText()
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
	fun main(args: Array<String>) {
		Echoer.openOutput()
		if (args.isEmpty()) println("Please specify an input file.")
		else interpret(File(args.first()), SymbolList())
	}
}
