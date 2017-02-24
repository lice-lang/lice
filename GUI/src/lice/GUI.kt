/**
 * Created by ice1000 on 2017/2/23.
 *
 * @author ice1000
 */
package lice

import lice.repl.VERSION_CODE
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Font
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.io.OutputStream
import java.io.PrintStream
import javax.swing.*


fun main(args: Array<String>) {
	val sl = lice.compiler.util.SymbolList()
	val frame = JFrame("Lice language interpreter $VERSION_CODE")
	frame.layout = BorderLayout()
	val output = JTextArea()
	output.isEditable = false
	output.background = Color.LIGHT_GRAY
	val input = JTextField()
	val button = JButton()
	output.tabSize = 2
	lice.compiler.util.forceRun {
		output.font = Font("Consolas", 0, 12)
		input.font = Font("Consolas", 0, 16)
	}
	val printStream = PrintStream(object : OutputStream() {
		override fun write(b: Int) =
				output.append(b.toChar().toString())

		override fun write(b: ByteArray) =
				output.append(java.lang.String(b).toString())
	})
	System.setOut(printStream)
	System.setErr(printStream)
	val repl = lice.repl.Repl()
	input.addKeyListener(object : KeyListener {
		override fun keyTyped(e: KeyEvent?) = Unit
		override fun keyReleased(e: KeyEvent?) = Unit
		override fun keyPressed(e: KeyEvent?) {
//				println("${e?.keyCode}, ${KeyEvent.VK_ENTER}")
			if (e != null && e.keyCode == KeyEvent.VK_ENTER) {
				output.append("${input.text}\n")
				repl.handle(input.text, sl)
				input.text = ""
			}
		}
	})
	frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
	frame.add(JScrollPane(output), BorderLayout.CENTER)
	frame.add(button, BorderLayout.NORTH)
	frame.add(input, BorderLayout.SOUTH)
	frame.setLocation(100, 100)
	frame.setSize(360, 360)
	frame.isVisible = true
	input.requestFocus()
}