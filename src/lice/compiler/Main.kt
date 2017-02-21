package lice.compiler

import lice.compiler.model.Ast
import lice.compiler.parse.buildNode
import lice.compiler.parse.createAst
import lice.compiler.parse.mapAst
import lice.compiler.util.DEBUGGING
import lice.compiler.util.SymbolList
import lice.compiler.util.VERBOSE
import java.io.File
import java.util.*

/**
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 */

object Main {
	fun startRepl() {
		val scanner = Scanner(System.`in`)
		DEBUGGING= false
		VERBOSE = false
		print("Lice> ")
		while (scanner.hasNext()) {
			val str = scanner.nextLine()
			val symbolList = SymbolList(true)
			Ast(mapAst(buildNode(str), symbolList), symbolList)
					.root
					.eval()
			print("Lice> ")
		}
	}

	fun interpret(file: File) {
		val ast = createAst(file)
	}

	@JvmStatic
	fun main(args: Array<String>) {
		if (args.isEmpty()) startRepl() else interpret(File(args[0]))
	}
}

