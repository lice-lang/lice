package org.lice.repl

import org.lice.compiler.parse.createRootNode
import org.lice.core.SymbolList
import org.lice.lang.Echoer
import org.lice.VERSION

import java.io.File
import java.util.*

/**
 * The entrance of the whole application
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 * @since 1.0.0
 */

object Main {

	/**
	 * interpret code in a file
	 */
	@JvmOverloads
	fun interpret(
			file: File,
			symbolList: SymbolList = SymbolList()
	) = createRootNode(file, symbolList).eval()

	@JvmStatic
	fun main(args: Array<String>) {
		Echoer.openOutput()
		if (args.isEmpty()) println("Please specify an input file.")
		else interpret(File(args.first()))
	}
}
