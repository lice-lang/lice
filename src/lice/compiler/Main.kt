package lice.compiler

import lice.compiler.model.Ast
import lice.compiler.parse.buildNode
import lice.compiler.parse.createAst
import lice.compiler.parse.mapAst
import lice.compiler.util.DEBUGGING
import lice.compiler.util.SymbolList
import lice.compiler.util.VERBOSE
import lice.compiler.util.serr
import java.io.File
import java.util.*

/**
 * The entrance of the whole application
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 */

object Main {

	/**
	 * starting the read-eval-print-loop machine
	 */
	fun startRepl() {
		val scanner = Scanner(System.`in`)
		DEBUGGING = false
		VERBOSE = false
		val hint = "Lice > "
		while (true) {
			print(hint)
			val str = scanner.nextLine()
			if ("exit" == str) {
				println("Have a nice day :)")
				System.exit(0)
			}
			val symbolList = SymbolList(true)
			try {
				Ast(mapAst(buildNode(str), symbolList), symbolList)
						.root
						.eval()
			} catch (e: Throwable) {
				serr(e.message ?: "")
			}
		}
	}

	/**
	 * interpret code in a file
	 */
	fun interpret(file: File) {
		val ast = createAst(file)
	}

	@JvmStatic
	fun main(args: Array<String>) {
		if (args.isEmpty())
			startRepl()
		else
			interpret(File(args[0]))
	}
}

