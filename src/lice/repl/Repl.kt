package lice.repl

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
class Repl {
	var stackTrace: Throwable? = null

	init {
		println("Lice repl $VERSION_CODE")
		print(HINT)
		DEBUGGING = false
		VERBOSE = false
	}

	fun handle(str: String, symbolList: SymbolList = SymbolList(true)) {
		when (str) {
			"exit" -> {
				"Have a nice day :)".println()
				System.exit(0)
			}
			"show-full-message" ->
				if (stackTrace != null) stackTrace?.printStackTrace()
				else "No stack trace.".println()
			"gc" -> System.gc()
			"help" -> """
This is the repl for lice language.

You have 5 special commands which you cannot use in the language but the repl:

exit: exit the repl
show-full-message: print the most recent stack trace
gc: run garbage collection
help: print this doc
version: check the version"""
					.println()
			"version" -> """
Lice language interpreter $VERSION_CODE
by ice1000""".println()
			else -> try {
				Ast(mapAst(buildNode(str), symbolList), symbolList)
						.root
						.eval()
			} catch(e: Throwable) {
				stackTrace = e
				serr(e.message ?: "")
			}
		}
		print("\n$HINT")
	}

	companion object {
		val HINT = "Lice > "
	}
}
