package lice.compiler

import lice.compiler.ast.createAst
import java.io.File
import java.util.*

/**
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 */

fun nn(in0: Double) = { in1: Double -> { w0: Double -> { w1: Double -> Math.max(in0 * w0, in1 * w1) } } }

object Main {
	fun startRepl() {
		val scanner = Scanner(System.`in`)
		while (scanner.hasNext()) {
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
