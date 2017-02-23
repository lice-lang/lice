package lice.compiler

import lice.compiler.parse.createAst
import lice.compiler.util.forceRun
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Font
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.io.File
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
	 * interpret code in a file
	 */
	fun interpret(file: File) {
		val ast = createAst(file)
		ast.root.eval()
	}

	@JvmStatic
	fun main(args: Array<String>) =
			if (args.isEmpty()) {
				val scanner = Scanner(System.`in`)
				val repl = Repl()
				while (true)
					repl.handle(scanner.nextLine())
			} else
				interpret(File(args[0]))
}

fun main(args: Array<String>) {
	val frame = JFrame("Lice language interpreter $VERSION_CODE")
	frame.layout = BorderLayout()
	val output = JTextArea()
	output.isEditable = false
	output.background = Color.LIGHT_GRAY
	val input = JTextField()
	output.tabSize = 2
	forceRun {
		output.font = Font("Consolas", 0, 12)
		input.font = Font("Consolas", 0, 16)
	}
	System.setOut(PrintStream(object : OutputStream() {
		override fun write(b: Int) =
				output.append(b.toChar().toString())

		override fun write(b: ByteArray) =
				output.append(java.lang.String(b).toString())
	}))
	val repl = Repl()
	input.addKeyListener(object : KeyListener {
		override fun keyTyped(e: KeyEvent?) = Unit
		override fun keyReleased(e: KeyEvent?) = Unit
		override fun keyPressed(e: KeyEvent?) {
//				println("${e?.keyCode}, ${KeyEvent.VK_ENTER}")
			if (e != null && e.keyCode == KeyEvent.VK_ENTER) {
				output.append("\n${input.text}\n")
				repl.handle(input.text)
				input.text = ""
			}
		}
	})
	frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
	frame.add(output, BorderLayout.CENTER)
	frame.add(input, BorderLayout.SOUTH)
	frame.setSize(360, 360)
	frame.isVisible = true
	input.requestFocus()
}