package lice.compiler

import lice.compiler.model.Ast
import lice.compiler.parse.buildNode
import lice.compiler.parse.mapAst
import lice.compiler.util.*

/**
 * starting the read-eval-print-loop machine
 * Created by ice1000 on 2017/2/23.
 *
 * @author ice1000
 */
class Repl() {
	val hint = "Lice > "
	var stackTrace: Throwable? = null

	init {
		println("Lice repl $VERSION_CODE")
		print(hint)
		DEBUGGING = false
		VERBOSE = false
	}

	fun handle(str: String) {
		when (str) {
			"exit" -> {
				"Have a nice day :)".println()
				System.exit(0)
			}
			"show-full-message" ->
				if (stackTrace != null) stackTrace?.printStackTrace()
				else "No stack trace.".println()
			"help" -> """
This is the repl for lice language.

You have four special commands which you cannot use in the language but the repl:

exit: exit the repl
show-full-message: print the most recent stack trace
help: print this doc
version: check the version""".println()
			"version" -> """
Lice language interpreter $VERSION_CODE
by ice1000""".println()
			else -> try {
				val symbolList = SymbolList(true)
				Ast(mapAst(buildNode(str), symbolList), symbolList)
						.root
						.eval()
			} catch(e: Throwable) {
				stackTrace = e
				serr(e.message ?: "")
			}
		}
		print("\n$hint")
	}
}
