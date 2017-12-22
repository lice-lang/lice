package org.lice.repl

import org.lice.parse.createRootNode
import org.lice.core.SymbolList
import org.lice.lang.Echoer
import java.io.File

/**
 * @author ice1000
 */

object Main {

	/**
	 * interpret code in a file
	 */
	fun interpret(file: File, symbolList: SymbolList = SymbolList()) = createRootNode(file, symbolList).eval()

	@JvmStatic
	fun main(args: Array<String>) {
		Echoer.openOutput()
		if (args.isEmpty()) println("Please specify an input file.")
		else interpret(File(args.first()))
	}
}
