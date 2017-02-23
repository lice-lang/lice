package lice.compiler

import lice.compiler.model.Ast
import lice.compiler.parse.buildNode
import lice.compiler.parse.createAst
import lice.compiler.parse.mapAst
import lice.compiler.util.*
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Font
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.util.*
import javax.swing.JFrame
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.WindowConstants

val VERSION_CODE = "v1.0-SNAPSHOT"

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
	fun startRepl(scan: () -> String) {
		DEBUGGING = false
		VERBOSE = false
		val hint = "Lice > "
		var stackTrace: Throwable? = null
		while (true) {
			print(hint)
			val str = scan()
			when (str) {
				"exit" -> {
					"Have a nice day :)".println()
					System.exit(0)
				}
				"show-full-message" ->
					if (stackTrace != null) stackTrace.printStackTrace()
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
		}
	}

	/**
	 * interpret code in a file
	 */
	fun interpret(file: File) {
		val ast = createAst(file)
		ast.root.eval()
	}

	fun openGUI() {
		val frame = JFrame("Lice language interpreter $VERSION_CODE")
		frame.layout = BorderLayout()
		val output = JTextArea()
		output.isEditable = true
		output.background = Color.LIGHT_GRAY
		val input = JTextField()
		output.tabSize = 2
		forceRun { output.font = Font("Consolas", 0, 16) }
		System.setOut(PrintStream(object : OutputStream() {
			override fun write(b: Int) =
					output.append(b.toChar().toString())

			override fun write(b: ByteArray) =
					output.append(java.lang.String(b).toString())
		}))
		frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
		frame.add(output, BorderLayout.CENTER)
		frame.add(input, BorderLayout.SOUTH)
		frame.setSize(500, 500)
		frame.isVisible = true
//		startRepl()
	}

	@JvmStatic
	fun main(args: Array<String>) {
		if (args.isEmpty()) {
			val scanner = Scanner(System.`in`)
			startRepl({ scanner.nextLine() })
		} else
			interpret(File(args[0]))
	}
}

object GUI {
	@JvmStatic
	fun main(args: Array<String>) {
		Main.openGUI()
	}
}
