package lice.repl

import lice.compiler.parse.createAst
import lice.compiler.util.SymbolList
import java.io.File
import java.util.*

val VERSION_CODE = "v1.0-SNAPSHOT"

/**
 * The entrance of the whole application
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 */

object Main {

	/**
	 * interpret code in a file
	 */
	fun interpret(file: File) {
		val ast = createAst(file)
		ast.root.eval()
	}

	@JvmStatic
	fun main(args: Array<String>) =
			if (args.isEmpty()) {
				val sl = SymbolList()
				val scanner = Scanner(System.`in`)
				val repl = Repl()
				while (true)
					repl.handle(scanner.nextLine(), sl)
			} else
				interpret(File(args[0]))
}
