package org.lice.repl

import org.lice.compiler.parse.createRootNode
import org.lice.core.SymbolList
import org.lice.lang.Echoer
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
			Echoer.closeOutput()
			val sl = SymbolList()
			Echoer.openOutput()
			sl.provideFunction("help", {
				"""This is the repl for org.lice language.

				|see: https://github.com/lice-lang/lice""".trimMargin()
			})
			sl.provideFunction("version", {
				"""Lice language interpreter $VERSION_CODE
				|by ice1000""".trimMargin()
			})
			sl.provideFunction("FILE_PATH", { File("").absolutePath })
			val scanner = Scanner(System.`in`)
			val repl = Repl(sl)
			while (repl.handle(scanner.nextLine())) Unit
		} else {
			interpret(File(args[0]).apply {
				if (!exists()) Echoer.echolnErr("file not found: ${args[0]}")
			})
		}
	}
}
