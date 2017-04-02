package org.lice.repl

import org.lice.compiler.parse.createRootNode
import org.lice.compiler.util.SymbolList
import org.lice.compiler.util.serr
import java.io.File
import java.util.*

/**
 * The entrance of the whole application
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 * @since v1.0.0
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
		if (args.isEmpty()) {
			val sl = SymbolList()
			val scanner = Scanner(System.`in`)
			val repl = Repl()
			while (repl.handle(scanner.nextLine(), sl));
		} else {
			interpret(File(args[0]).apply {
				if (!exists()) serr("file not found: ${args[0]}")
			})
		}
//		System.exit(0)
	}
}
