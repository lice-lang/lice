package org.lice.repl

import org.lice.compiler.parse.buildNode
import org.lice.compiler.parse.mapAst
import org.lice.compiler.util.*
import org.lice.parser.Parser

/**
 * starting the read-eval-print-loop machine
 * Created by ice1000 on 2017/2/23.
 *
 * @author ice1000
 */
class Repl {
	var stackTrace: Throwable? = null

	init {
		println("""Lice language repl $VERSION_CODE
			|see: https://github.com/ice1000/org.lice

			|回首向来萧瑟处,也无风雨也无晴。
			|Stay young stay simple, and make yourself naive.

			|for help please input: help
			|""".trimMargin())
		print(HINT)
		DEBUGGING = false
		VERBOSE = false
	}

	@JvmOverloads
	fun handle(
			str: String,
			symbolList: SymbolList = SymbolList(true)) {
		when (str) {
			"exit" -> {
				"Have a nice day :)".println()
				System.exit(0)
			}
			"pst" ->
				if (stackTrace != null) stackTrace?.printStackTrace()
				else "No stack trace.".println()
			"help" -> """
				|This is the repl for org.lice language.

				|You have 4 special commands which you cannot use in the language but the repl:

				|exit: exit the repl
                |pst: print the most recent stack trace
                |help: print this doc
				|version: check the version""".trimMargin()
					.println()
			"version" -> """
				|Lice language interpreter $VERSION_CODE
				|by ice1000""".trimMargin()
			else -> try {
				val ast = Parser.defaultParser(str).mapAst(symbolList)
				ast.eval()
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
