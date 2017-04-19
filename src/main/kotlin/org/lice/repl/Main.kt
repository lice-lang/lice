package org.lice.repl

import org.lice.compiler.model.Value
import org.lice.compiler.parse.createRootNode
import org.lice.compiler.util.println
import org.lice.compiler.util.serr
import org.lice.core.SymbolList
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
		if (args.isEmpty()) {
			val sl = SymbolList()
			sl.provideFunctionWithMeta("help", { _, _ ->
				"""This is the repl for org.lice language.

				|You have 4 special commands which you cannot use in the language but the repl:

				|exit: exit the repl
				|pst: print the most recent stack trace
				|help: print this doc
				|version: check the version"""
						.trimMargin()
						.println()
			})
			sl.provideFunctionWithMeta("version", { _, _ ->
				"""Lice language interpreter $VERSION_CODE
				|by ice1000"""
						.trimMargin()
						.println()
			})
			sl.provideFunctionWithMeta("FILE_PATH", { _, _ -> Value(File("").absolutePath) })
			val scanner = Scanner(System.`in`)
			val repl = Repl()
			while (repl.handle(scanner.nextLine(), sl)) ;
		} else {
			interpret(File(args[0]).apply {
				if (!exists()) serr("file not found: ${args[0]}")
			})
		}
	}
}
